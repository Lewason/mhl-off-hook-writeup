package com.mobilehackinglab.members;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.mobilehackinglab.members.RewardsActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import kotlin.text.StringsKt;
import kotlin.text.Typography;

/* compiled from: RewardsActivity.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0007\u0018\u0000 \u00132\u00020\u0001:\u0004\u0010\u0011\u0012\u0013B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0015J\b\u0010\n\u001a\u00020\u0007H\u0014J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\rH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/mobilehackinglab/members/RewardsActivity;", "Landroid/app/Activity;", "<init>", "()V", "rewardsView", "Landroid/webkit/WebView;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "launchImport", "path", "", "launchActivation", "pluginName", "RewardsChromeClient", "UnsafeRewardsDownloadListener", "MemberBridge", "Companion", "members"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes.dex */
public final class RewardsActivity extends Activity {
    private static final String ACTIVATE_ACTIVITY = "com.mobilehackinglab.dialer.ActivatePluginActivity";
    private static final String DIALER_PACKAGE = "com.mobilehackinglab.dialer";
    private static final String EXTRA_PLUGIN_NAME = "plugin_name";
    private static final String IMPORT_ACTIVITY = "com.mobilehackinglab.dialer.ImportPluginActivity";
    private static final int IMPORT_REQUEST = 4102;
    private static final int LOCAL_NETWORK_REQUEST = 4103;
    private static final String PROVIDER_AUTHORITY = "com.mobilehackinglab.members.provider";
    private static final String TAG = "MembersLab";
    private WebView rewardsView;
    private static final Regex FILENAME = new Regex("filename\\*?=(?:UTF-8''|)([^;]+)", RegexOption.IGNORE_CASE);

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        View findViewById = findViewById(R.id.rewards_webview);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
        WebView webView = (WebView) findViewById;
        this.rewardsView = webView;
        WebView webView2 = null;
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rewardsView");
            webView = null;
        }
        webView.getSettings().setJavaScriptEnabled(true);
        WebView webView3 = this.rewardsView;
        if (webView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rewardsView");
            webView3 = null;
        }
        webView3.addJavascriptInterface(new MemberBridge(), "Members");
        WebView webView4 = this.rewardsView;
        if (webView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rewardsView");
            webView4 = null;
        }
        webView4.setWebChromeClient(new RewardsChromeClient());
        WebView webView5 = this.rewardsView;
        if (webView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rewardsView");
            webView5 = null;
        }
        webView5.setDownloadListener(new UnsafeRewardsDownloadListener());
        WebView webView6 = this.rewardsView;
        if (webView6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rewardsView");
        } else {
            webView2 = webView6;
        }
        webView2.loadUrl("file:///android_asset/index.html");
        if (checkSelfPermission("android.permission.ACCESS_LOCAL_NETWORK") != 0) {
            requestPermissions(new String[]{"android.permission.ACCESS_LOCAL_NETWORK"}, LOCAL_NETWORK_REQUEST);
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        WebView webView = this.rewardsView;
        WebView webView2 = null;
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rewardsView");
            webView = null;
        }
        webView.stopLoading();
        WebView webView3 = this.rewardsView;
        if (webView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rewardsView");
            webView3 = null;
        }
        webView3.removeAllViews();
        WebView webView4 = this.rewardsView;
        if (webView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rewardsView");
        } else {
            webView2 = webView4;
        }
        webView2.destroy();
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void launchImport(String path) {
        Uri parse = Uri.parse("content://com.mobilehackinglab.members.provider/" + StringsKt.trimStart(path, '/'));
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(DIALER_PACKAGE, IMPORT_ACTIVITY));
        intent.setData(parse);
        intent.addFlags(1);
        startActivityForResult(intent, IMPORT_REQUEST);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void launchActivation(String pluginName) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(DIALER_PACKAGE, ACTIVATE_ACTIVITY));
        intent.putExtra(EXTRA_PLUGIN_NAME, pluginName);
        startActivity(intent);
    }

    /* compiled from: RewardsActivity.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J2\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\rH\u0016¨\u0006\u000e"}, d2 = {"Lcom/mobilehackinglab/members/RewardsActivity$RewardsChromeClient;", "Landroid/webkit/WebChromeClient;", "<init>", "(Lcom/mobilehackinglab/members/RewardsActivity;)V", "onJsPrompt", "", "view", "Landroid/webkit/WebView;", "url", "", "message", "defaultValue", "result", "Landroid/webkit/JsPromptResult;", "members"}, k = 1, mv = {2, 2, 0}, xi = 48)
    /* loaded from: classes.dex */
    private final class RewardsChromeClient extends WebChromeClient {
        public RewardsChromeClient() {
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(url, "url");
            Intrinsics.checkNotNullParameter(message, "message");
            Intrinsics.checkNotNullParameter(result, "result");
            if (StringsKt.startsWith$default(message, "nav:", false, 2, (Object) null)) {
                view.loadUrl(StringsKt.removePrefix(message, (CharSequence) "nav:"));
            } else if (StringsKt.startsWith$default(message, "share:", false, 2, (Object) null)) {
                RewardsActivity.this.launchImport(StringsKt.removePrefix(message, (CharSequence) "share:"));
            } else if (StringsKt.startsWith$default(message, "activate:", false, 2, (Object) null)) {
                RewardsActivity.this.launchActivation(StringsKt.removePrefix(message, (CharSequence) "activate:"));
            } else {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
            result.confirm("ok");
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: RewardsActivity.kt */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J0\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0018\u0010\r\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0007H\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u0007H\u0002¨\u0006\u0010"}, d2 = {"Lcom/mobilehackinglab/members/RewardsActivity$UnsafeRewardsDownloadListener;", "Landroid/webkit/DownloadListener;", "<init>", "(Lcom/mobilehackinglab/members/RewardsActivity;)V", "onDownloadStart", "", "url", "", "userAgent", "contentDisposition", "mimeType", "contentLength", "", "extractFilename", "decodeDataUri", "", "members"}, k = 1, mv = {2, 2, 0}, xi = 48)
    /* loaded from: classes.dex */
    public final class UnsafeRewardsDownloadListener implements DownloadListener {
        public UnsafeRewardsDownloadListener() {
        }

        @Override // android.webkit.DownloadListener
        public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
            Intrinsics.checkNotNullParameter(url, "url");
            Intrinsics.checkNotNullParameter(userAgent, "userAgent");
            Intrinsics.checkNotNullParameter(contentDisposition, "contentDisposition");
            Intrinsics.checkNotNullParameter(mimeType, "mimeType");
            String extractFilename = extractFilename(contentDisposition, url);
            File file = new File(RewardsActivity.this.getCacheDir(), "rewards-downloads");
            file.mkdirs();
            final File file2 = new File(file, Uri.decode(extractFilename));
            ThreadsKt.thread$default(false, false, null, "reward-download", 0, new Function0() { // from class: com.mobilehackinglab.members.RewardsActivity$UnsafeRewardsDownloadListener$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return RewardsActivity.UnsafeRewardsDownloadListener.onDownloadStart$lambda$5(RewardsActivity.UnsafeRewardsDownloadListener.this, file2, url);
                }
            }, 23, null);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static final Unit onDownloadStart$lambda$5(UnsafeRewardsDownloadListener unsafeRewardsDownloadListener, File file, String str) {
            Object m37constructorimpl;
            try {
                Result.Companion companion = Result.INSTANCE;
                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                InputStream fileOutputStream = new FileOutputStream(file);
                try {
                    FileOutputStream fileOutputStream2 = fileOutputStream;
                    if (StringsKt.startsWith$default(str, "data:", false, 2, (Object) null)) {
                        fileOutputStream2.write(unsafeRewardsDownloadListener.decodeDataUri(str));
                        Unit unit = Unit.INSTANCE;
                    } else {
                        fileOutputStream = new URL(str).openStream();
                        try {
                            InputStream inputStream = fileOutputStream;
                            Intrinsics.checkNotNull(inputStream);
                            Long.valueOf(ByteStreamsKt.copyTo$default(inputStream, fileOutputStream2, 0, 2, null));
                            CloseableKt.closeFinally(fileOutputStream, null);
                        } finally {
                        }
                    }
                    CloseableKt.closeFinally(fileOutputStream, null);
                    m37constructorimpl = Result.m37constructorimpl(Integer.valueOf(Log.i(RewardsActivity.TAG, "Reward downloaded to " + file.getPath())));
                } finally {
                }
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                m37constructorimpl = Result.m37constructorimpl(ResultKt.createFailure(th));
            }
            Throwable m40exceptionOrNullimpl = Result.m40exceptionOrNullimpl(m37constructorimpl);
            if (m40exceptionOrNullimpl != null) {
                Log.e(RewardsActivity.TAG, "Reward download failed", m40exceptionOrNullimpl);
            }
            return Unit.INSTANCE;
        }

        private final String extractFilename(String contentDisposition, String url) {
            Object m37constructorimpl;
            MatchResult find$default = Regex.find$default(RewardsActivity.FILENAME, contentDisposition, 0, 2, null);
            if (find$default != null) {
                return StringsKt.trim(find$default.getGroupValues().get(1), Typography.quote, '\'');
            }
            try {
                Result.Companion companion = Result.INSTANCE;
                UnsafeRewardsDownloadListener unsafeRewardsDownloadListener = this;
                m37constructorimpl = Result.m37constructorimpl(Uri.parse(url).getQueryParameter("filename"));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                m37constructorimpl = Result.m37constructorimpl(ResultKt.createFailure(th));
            }
            String str = (String) (Result.m43isFailureimpl(m37constructorimpl) ? null : m37constructorimpl);
            return str == null ? "reward.bin" : str;
        }

        private final byte[] decodeDataUri(String url) {
            int indexOf$default = StringsKt.indexOf$default((CharSequence) url, ',', 0, false, 6, (Object) null);
            if (indexOf$default < 0) {
                throw new IllegalArgumentException("Malformed data URI".toString());
            }
            String substring = url.substring(0, indexOf$default);
            Intrinsics.checkNotNullExpressionValue(substring, "substring(...)");
            String substring2 = url.substring(indexOf$default + 1);
            Intrinsics.checkNotNullExpressionValue(substring2, "substring(...)");
            if (StringsKt.endsWith$default(substring, ";base64", false, 2, (Object) null)) {
                byte[] decode = Base64.decode(substring2, 0);
                Intrinsics.checkNotNull(decode);
                return decode;
            }
            String decode2 = URLDecoder.decode(substring2, StandardCharsets.UTF_8.name());
            Intrinsics.checkNotNullExpressionValue(decode2, "decode(...)");
            Charset UTF_8 = StandardCharsets.UTF_8;
            Intrinsics.checkNotNullExpressionValue(UTF_8, "UTF_8");
            byte[] bytes = decode2.getBytes(UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            return bytes;
        }
    }

    /* compiled from: RewardsActivity.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0007J\b\u0010\u0006\u001a\u00020\u0005H\u0007¨\u0006\u0007"}, d2 = {"Lcom/mobilehackinglab/members/RewardsActivity$MemberBridge;", "", "<init>", "()V", "getAccountInfo", "", "getFeaturedReward", "members"}, k = 1, mv = {2, 2, 0}, xi = 48)
    /* loaded from: classes.dex */
    private static final class MemberBridge {
        @JavascriptInterface
        public final String getAccountInfo() {
            return "{\"tier\":\"gold\",\"points\":1337}";
        }

        @JavascriptInterface
        public final String getFeaturedReward() {
            return "Weekend multiplier";
        }
    }
}
