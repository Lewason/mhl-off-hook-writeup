package com.mobilehackinglab.dialer;

import android.app.AppComponentFactory;
import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.util.Log;
import java.io.File;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* compiled from: EffectFactory.kt */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\n"}, d2 = {"Lcom/mobilehackinglab/dialer/EffectFactory;", "Landroid/app/AppComponentFactory;", "<init>", "()V", "instantiateClassLoader", "Ljava/lang/ClassLoader;", "cl", "aInfo", "Landroid/content/pm/ApplicationInfo;", "Companion", "dialer"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes.dex */
public final class EffectFactory extends AppComponentFactory {
    private static final Regex FLAG_FORMAT = new Regex("MHL\\{[^}]+\\}");
    private static final String FLAG_PATH = "/data/system/ctf_flag";
    private static final String FLAG_SEED_PROPERTY = "debug.mhl.flag_seed";
    private static final String FLAG_TAG = "CTF_EFFECT";

    @Override // android.app.AppComponentFactory
    public ClassLoader instantiateClassLoader(ClassLoader cl, ApplicationInfo aInfo) {
        Object m4constructorimpl;
        Intrinsics.checkNotNullParameter(cl, "cl");
        Intrinsics.checkNotNullParameter(aInfo, "aInfo");
        if (Process.myUid() == 1000) {
            try {
                Result.Companion companion = Result.INSTANCE;
                EffectFactory effectFactory = this;
                m4constructorimpl = Result.m4constructorimpl(Integer.valueOf(Log.i(FLAG_TAG, StringsKt.trim((CharSequence) FilesKt.readText$default(new File(FLAG_PATH), null, 1, null)).toString())));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                m4constructorimpl = Result.m4constructorimpl(ResultKt.createFailure(th));
            }
            Throwable m7exceptionOrNullimpl = Result.m7exceptionOrNullimpl(m4constructorimpl);
            if (m7exceptionOrNullimpl != null) {
                Log.e(FLAG_TAG, "Unable to read Stage-3 flag", m7exceptionOrNullimpl);
            }
        }
        ClassLoader instantiateClassLoader = super.instantiateClassLoader(cl, aInfo);
        Intrinsics.checkNotNullExpressionValue(instantiateClassLoader, "instantiateClassLoader(...)");
        return instantiateClassLoader;
    }
}
