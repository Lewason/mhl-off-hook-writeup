package com.mobilehackinglab.members;

import android.app.Activity;
import android.graphics.Insets;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AccountDetailsActivity.kt */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0014J\b\u0010\b\u001a\u00020\u0005H\u0002¨\u0006\t"}, d2 = {"Lcom/mobilehackinglab/members/AccountDetailsActivity;", "Landroid/app/Activity;", "<init>", "()V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "applySystemBarInsets", "members"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes.dex */
public final class AccountDetailsActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        applySystemBarInsets();
        findViewById(R.id.account_back).setOnClickListener(new View.OnClickListener() { // from class: com.mobilehackinglab.members.AccountDetailsActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AccountDetailsActivity.this.finish();
            }
        });
    }

    private final void applySystemBarInsets() {
        final View findViewById = findViewById(R.id.account_root);
        final View findViewById2 = findViewById(R.id.account_header);
        final int paddingTop = findViewById2.getPaddingTop();
        findViewById.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.mobilehackinglab.members.AccountDetailsActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return AccountDetailsActivity.applySystemBarInsets$lambda$1(findViewById2, paddingTop, findViewById, view, windowInsets);
            }
        });
        findViewById.requestApplyInsets();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final WindowInsets applySystemBarInsets$lambda$1(View view, int i, View view2, View view3, WindowInsets insets) {
        Intrinsics.checkNotNullParameter(view3, "<unused var>");
        Intrinsics.checkNotNullParameter(insets, "insets");
        Insets insets2 = insets.getInsets(WindowInsets.Type.systemBars());
        Intrinsics.checkNotNullExpressionValue(insets2, "getInsets(...)");
        view.setPadding(view.getPaddingLeft(), i + insets2.top, view.getPaddingRight(), view.getPaddingBottom());
        view2.setPadding(view2.getPaddingLeft(), 0, view2.getPaddingRight(), insets2.bottom);
        return insets;
    }
}
