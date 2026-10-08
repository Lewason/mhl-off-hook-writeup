package com.mobilehackinglab.dialer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Insets;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\r\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0014J\b\u0010\n\u001a\u00020\u0007H\u0014J-\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\r2\u000e\u0010\u000e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00100\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016¢\u0006\u0002\u0010\u0013J\b\u0010\u0014\u001a\u00020\u0007H\u0002J\u0018\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\rH\u0002J\b\u0010\u0018\u001a\u00020\u0007H\u0002J\b\u0010\u0019\u001a\u00020\u0007H\u0002J\u0010\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0010H\u0002J\b\u0010\u001c\u001a\u00020\u0007H\u0002J\b\u0010\u001d\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lcom/mobilehackinglab/dialer/MainActivity;", "Landroid/app/Activity;", "<init>", "()V", "numberDisplay", "Landroid/widget/TextView;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "onRequestPermissionsResult", "requestCode", "", "permissions", "", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "applySystemBarInsets", "showPanel", "panelId", "tabId", "ensureContactsAccess", "loadContacts", "appendDigit", "digit", "deleteDigit", "openDialFlow", "Companion", "dialer"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes.dex */
public final class MainActivity extends Activity {
    private static final int CONTACTS_REQUEST = 5201;
    private TextView numberDisplay;
    private static final List<Integer> PANEL_IDS = CollectionsKt.listOf((Object[]) new Integer[]{Integer.valueOf(R.id.recents_panel), Integer.valueOf(R.id.contacts_panel), Integer.valueOf(R.id.keypad_panel)});
    private static final List<Integer> TAB_IDS = CollectionsKt.listOf((Object[]) new Integer[]{Integer.valueOf(R.id.tab_recents), Integer.valueOf(R.id.tab_contacts), Integer.valueOf(R.id.tab_keypad)});
    private static final List<Pair<Integer, String>> DIGIT_BUTTONS = CollectionsKt.listOf((Object[]) new Pair[]{TuplesKt.to(Integer.valueOf(R.id.key_1), "1"), TuplesKt.to(Integer.valueOf(R.id.key_2), "2"), TuplesKt.to(Integer.valueOf(R.id.key_3), "3"), TuplesKt.to(Integer.valueOf(R.id.key_4), "4"), TuplesKt.to(Integer.valueOf(R.id.key_5), "5"), TuplesKt.to(Integer.valueOf(R.id.key_6), "6"), TuplesKt.to(Integer.valueOf(R.id.key_7), "7"), TuplesKt.to(Integer.valueOf(R.id.key_8), "8"), TuplesKt.to(Integer.valueOf(R.id.key_9), "9"), TuplesKt.to(Integer.valueOf(R.id.key_star), "*"), TuplesKt.to(Integer.valueOf(R.id.key_0), "0"), TuplesKt.to(Integer.valueOf(R.id.key_hash), "#")});

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySystemBarInsets();
        View findViewById = findViewById(R.id.number_display);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
        this.numberDisplay = (TextView) findViewById;
        Iterator<T> it = DIGIT_BUTTONS.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            int intValue = ((Number) pair.component1()).intValue();
            final String str = (String) pair.component2();
            ((Button) findViewById(intValue)).setOnClickListener(new View.OnClickListener() { // from class: com.mobilehackinglab.dialer.MainActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.this.appendDigit(str);
                }
            });
        }
        ((Button) findViewById(R.id.delete_button)).setOnClickListener(new View.OnClickListener() { // from class: com.mobilehackinglab.dialer.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.deleteDigit();
            }
        });
        ((Button) findViewById(R.id.call_button)).setOnClickListener(new View.OnClickListener() { // from class: com.mobilehackinglab.dialer.MainActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.openDialFlow();
            }
        });
        ((Button) findViewById(R.id.tab_recents)).setOnClickListener(new View.OnClickListener() { // from class: com.mobilehackinglab.dialer.MainActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.showPanel(R.id.recents_panel, R.id.tab_recents);
            }
        });
        ((Button) findViewById(R.id.tab_contacts)).setOnClickListener(new View.OnClickListener() { // from class: com.mobilehackinglab.dialer.MainActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$5(MainActivity.this, view);
            }
        });
        ((Button) findViewById(R.id.tab_keypad)).setOnClickListener(new View.OnClickListener() { // from class: com.mobilehackinglab.dialer.MainActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.showPanel(R.id.keypad_panel, R.id.tab_keypad);
            }
        });
        showPanel(R.id.keypad_panel, R.id.tab_keypad);
        ((Button) findViewById(R.id.add_contact_button)).setOnClickListener(new View.OnClickListener() { // from class: com.mobilehackinglab.dialer.MainActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.startActivity(new Intent("android.intent.action.INSERT", ContactsContract.Contacts.CONTENT_URI));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void onCreate$lambda$5(MainActivity mainActivity, View view) {
        mainActivity.showPanel(R.id.contacts_panel, R.id.tab_contacts);
        mainActivity.ensureContactsAccess();
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            loadContacts();
        }
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Integer firstOrNull;
        Intrinsics.checkNotNullParameter(permissions, "permissions");
        Intrinsics.checkNotNullParameter(grantResults, "grantResults");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACTS_REQUEST && (firstOrNull = ArraysKt.firstOrNull(grantResults)) != null && firstOrNull.intValue() == 0) {
            loadContacts();
        } else if (requestCode == CONTACTS_REQUEST) {
            ((TextView) findViewById(R.id.contacts_list)).setText("Contacts permission was not granted. Tap the Contacts tab to try again.");
        }
    }

    private final void applySystemBarInsets() {
        final View findViewById = findViewById(R.id.dialer_root);
        final View findViewById2 = findViewById(R.id.dialer_header);
        final int paddingTop = findViewById2.getPaddingTop();
        findViewById.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.mobilehackinglab.dialer.MainActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return MainActivity.applySystemBarInsets$lambda$8(findViewById2, paddingTop, findViewById, view, windowInsets);
            }
        });
        findViewById.requestApplyInsets();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final WindowInsets applySystemBarInsets$lambda$8(View view, int i, View view2, View view3, WindowInsets insets) {
        Intrinsics.checkNotNullParameter(view3, "<unused var>");
        Intrinsics.checkNotNullParameter(insets, "insets");
        Insets insets2 = insets.getInsets(WindowInsets.Type.systemBars());
        Intrinsics.checkNotNullExpressionValue(insets2, "getInsets(...)");
        view.setPadding(view.getPaddingLeft(), i + insets2.top, view.getPaddingRight(), view.getPaddingBottom());
        view2.setPadding(view2.getPaddingLeft(), 0, view2.getPaddingRight(), insets2.bottom);
        return insets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showPanel(int panelId, int tabId) {
        Iterator<T> it = PANEL_IDS.iterator();
        while (it.hasNext()) {
            findViewById(((Number) it.next()).intValue()).setVisibility(8);
        }
        findViewById(panelId).setVisibility(0);
        Iterator<T> it2 = TAB_IDS.iterator();
        while (it2.hasNext()) {
            int intValue = ((Number) it2.next()).intValue();
            Button button = (Button) findViewById(intValue);
            button.setAlpha(intValue == tabId ? 1.0f : 0.58f);
            button.setBackgroundTintList(ColorStateList.valueOf(intValue == tabId ? Color.rgb(221, 235, 231) : 0));
        }
    }

    private final void ensureContactsAccess() {
        if (checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            loadContacts();
        } else {
            ((TextView) findViewById(R.id.contacts_list)).setText("Allow contact access to show names and phone numbers here.");
            requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, CONTACTS_REQUEST);
        }
    }

    private final void loadContacts() {
        ArrayList arrayList = new ArrayList();
        String[] strArr = {"display_name", "data1"};
        Cursor query = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, strArr, null, null, "display_name COLLATE NOCASE");
        if (query != null) {
            Cursor cursor = query;
            try {
                Cursor cursor2 = cursor;
                int columnIndexOrThrow = cursor2.getColumnIndexOrThrow(strArr[0]);
                int columnIndexOrThrow2 = cursor2.getColumnIndexOrThrow(strArr[1]);
                while (cursor2.moveToNext()) {
                    arrayList.add(cursor2.getString(columnIndexOrThrow) + "\n" + cursor2.getString(columnIndexOrThrow2));
                }
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(cursor, null);
            } finally {
            }
        }
        ((TextView) findViewById(R.id.contacts_list)).setText(arrayList.isEmpty() ? "No contacts yet. Tap Add contact to create one." : CollectionsKt.joinToString$default(arrayList, "\n\n", null, null, 0, null, null, 62, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void appendDigit(String digit) {
        TextView textView = this.numberDisplay;
        TextView textView2 = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("numberDisplay");
            textView = null;
        }
        TextView textView3 = this.numberDisplay;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("numberDisplay");
        } else {
            textView2 = textView3;
        }
        textView.setText(((Object) textView2.getText()) + digit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void deleteDigit() {
        TextView textView = this.numberDisplay;
        TextView textView2 = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("numberDisplay");
            textView = null;
        }
        String obj = textView.getText().toString();
        if (obj.length() > 0) {
            TextView textView3 = this.numberDisplay;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("numberDisplay");
            } else {
                textView2 = textView3;
            }
            textView2.setText(StringsKt.dropLast(obj, 1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void openDialFlow() {
        TextView textView = this.numberDisplay;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("numberDisplay");
            textView = null;
        }
        String obj = textView.getText().toString();
        if (StringsKt.isBlank(obj)) {
            return;
        }
        startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + Uri.encode(obj))));
    }
}
