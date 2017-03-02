package com.goqual.a10k.binding;

import android.databinding.BindingConversion;
import android.view.View;

import com.goqual.a10k.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by TedPark on 2017. 2. 16..
 */

public class BindingConversions {
    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }
}
