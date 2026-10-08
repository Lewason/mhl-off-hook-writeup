# MHL Off-Hook — Writeup

Writeup and source artifacts for the Mobile Hacking Lab challenge **Off-Hook**
(Android 17 / API 37), based on **CVE-2026-49881** — a logic flaw in
`InCallController.serviceClassExists` that yields arbitrary code execution as
`system_server` (UID 1000) from an unprivileged app.

## Flag

```
MHL{t3lecom_tr1gger_s3rv3d}
```

## Layout

- [`WALKTHROUGH.md`](WALKTHROUGH.md) — full start-to-finish exploitation walkthrough.
- [`sources/dialer/`](sources/dialer/) — decompiled `com.mobilehackinglab.dialer` + manifest.
- [`sources/members/`](sources/members/) — decompiled `com.mobilehackinglab.members`, manifest, WebView asset, FileProvider paths.
- [`exploit/`](exploit/) — the plugin `.so` source (`evil.c`) and the repackaging trigger.

## TL;DR

1. **Foothold:** DOM-XSS in the members app WebView + filename path-traversal in
   `UnsafeRewardsDownloadListener` + shared-signature `MANAGE_PLUGINS` trust.
2. **Trigger:** load a plugin `.so` into the dialer whose `JNI_OnLoad` calls
   `Exploit.trigger()` → `TelecomManager.addCall()`.
3. **Bug:** system_server's `serviceClassExists` does
   `createPackageContextAsUser(...IGNORE_SECURITY).getClassLoader()`, running the
   dialer's `EffectFactory.instantiateClassLoader()` as UID 1000, which reads and
   logs `/data/system/ctf_flag` under tag `CTF_EFFECT`.

See `WALKTHROUGH.md` for the complete chain and both exploitation routes.
