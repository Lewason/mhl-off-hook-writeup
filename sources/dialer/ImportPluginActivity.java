package com.mobilehackinglab.dialer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: ImportPluginActivity.kt */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \b2\u00020\u0001:\u0001\bB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0014¨\u0006\t"}, d2 = {"Lcom/mobilehackinglab/dialer/ImportPluginActivity;", "Landroid/app/Activity;", "<init>", "()V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "Companion", "dialer"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes.dex */
public final class ImportPluginActivity extends Activity {
    private static final String DEFAULT_PLUGIN = "evil.so";
    private static final String TAG = "DialerImport";

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        Object m4constructorimpl;
        String str;
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
        if (data == null) {
            Log.w(TAG, "Import launched without a content URI");
            finish();
            return;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            ImportPluginActivity importPluginActivity = this;
            String lastPathSegment = data.getLastPathSegment();
            if (lastPathSegment == null || (str = StringsKt.substringAfterLast$default(lastPathSegment, '/', (String) null, 2, (Object) null)) == null) {
                str = DEFAULT_PLUGIN;
            }
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            m4constructorimpl = Result.m4constructorimpl(ResultKt.createFailure(th));
        }
        if (!Intrinsics.areEqual(str, new File(str).getName()) || !StringsKt.endsWith$default(str, ".so", false, 2, (Object) null)) {
            throw new IllegalArgumentException("Invalid plugin name".toString());
        }
        File file = new File(getFilesDir(), "plugins");
        file.mkdirs();
        File file2 = new File(file, str);
        FileOutputStream openInputStream = getContentResolver().openInputStream(data);
        try {
            InputStream inputStream = openInputStream;
            if (inputStream == null) {
                throw new IllegalArgumentException(("Unable to open " + data).toString());
            }
            openInputStream = new FileOutputStream(file2);
            try {
                ByteStreamsKt.copyTo$default(inputStream, openInputStream, 0, 2, null);
                CloseableKt.closeFinally(openInputStream, null);
                CloseableKt.closeFinally(openInputStream, null);
                file2.setWritable(true, true);
                m4constructorimpl = Result.m4constructorimpl(Integer.valueOf(Log.i(TAG, "Imported writable plugin " + file2.getPath())));
                Throwable m7exceptionOrNullimpl = Result.m7exceptionOrNullimpl(m4constructorimpl);
                if (m7exceptionOrNullimpl != null) {
                    Log.e(TAG, "Plugin import failed", m7exceptionOrNullimpl);
                }
                finish();
            } finally {
            }
        } finally {
        }
    }
}
