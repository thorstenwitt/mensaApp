package de.thorstenwitt.mensaapp.layouts;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by dev on 03.01.17.
 */

public class ChinLayout extends LinearLayout {

    public ChinLayout(Context context) {
        super(context);
    }

    public ChinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChinLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int chin = insets.getSystemWindowInsetBottom();
        setPadding(0, 0, 0, chin);
        return insets;
    }
}
