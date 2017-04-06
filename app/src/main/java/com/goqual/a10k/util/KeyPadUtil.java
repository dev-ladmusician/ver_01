package com.goqual.a10k.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by ladmusician on 2017. 4. 5..
 */

public class KeyPadUtil {
    public static void KeyPadUp(Context context, EditText editText){
        editText.requestFocus();
        editText.setSelection(editText.length());
        InputMethodManager imm = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void KeyPadDown(Context context, EditText editText) {
        editText.setFocusable(false);
        InputMethodManager immhide = (InputMethodManager)context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        immhide.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
