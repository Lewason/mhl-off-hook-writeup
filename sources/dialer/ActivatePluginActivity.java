package com.mobilehackinglab.dialer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.io.File;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: ActivatePluginActivity.kt */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \b2\u00020\u0001:\u0001\bB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0014¨\u0006\t"}, d2 = {"Lcom/mobilehackinglab/dialer/ActivatePluginActivity;", "Landroid/app/Activity;", "<init>", "()V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "Companion", "dialer"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes.dex */
public final class ActivatePluginActivity extends Activity {
    public static final String EXTRA_PLUGIN_NAME = "plugin_name";
    private static final String TAG = "DialerActivate";

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        Object m4constructorimpl;
        String stringExtra;
        super.onCreate(savedInstanceState);
        try {
            Result.Companion companion = Result.INSTANCE;
            ActivatePluginActivity activatePluginActivity = this;
            stringExtra = getIntent().getStringExtra(EXTRA_PLUGIN_NAME);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            m4constructorimpl = Result.m4constructorimpl(ResultKt.createFailure(th));
        }
        if (stringExtra == null) {
            throw new IllegalArgumentException("Required value was null.".toString());
        }
        if (!Intrinsics.areEqual(stringExtra, new File(stringExtra).getName()) || !StringsKt.endsWith$default(stringExtra, ".so", false, 2, (Object) null)) {
            throw new IllegalArgumentException("Invalid plugin name".toString());
        }
        File file = new File(new File(getFilesDir(), "plugins"), stringExtra);
        if (!file.isFile()) {
            throw new IllegalArgumentException("Plugin does not exist".toString());
        }
        if (!file.setReadOnly()) {
            throw new IllegalArgumentException("Unable to seal plugin read-only".toString());
        }
        Log.i(TAG, "Loading sealed plugin " + file.getPath());
        System.load(file.getAbsolutePath());
        m4constructorimpl = Result.m4constructorimpl(Unit.INSTANCE);
        Throwable m7exceptionOrNullimpl = Result.m7exceptionOrNullimpl(m4constructorimpl);
        if (m7exceptionOrNullimpl != null) {
            Log.e(TAG, "Plugin activation failed", m7exceptionOrNullimpl);
        }
        finish();
    }
}
