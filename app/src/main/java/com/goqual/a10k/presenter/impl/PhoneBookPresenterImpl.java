package com.goqual.a10k.presenter.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

import com.goqual.a10k.model.entity.Phone;
import com.goqual.a10k.model.remote.service.SwitchService;
import com.goqual.a10k.presenter.PhoneBookPresenter;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;

import java.util.Locale;

/**
 * Created by ladmusician on 2017. 1. 17..
 */

public class PhoneBookPresenterImpl implements PhoneBookPresenter {
    private static final String TAG = PhoneBookPresenterImpl.class.getName();
    private View mView;
    private Context mContext;
    private SwitchService mSwitchService = null;
    private AdapterDataModel<Phone> mAdapter;
    private String countryCode;

    private static final String[] PROJECTION = { "contact_id", "display_name", "data1", "photo_thumb_uri" };

    public PhoneBookPresenterImpl(Context ctx, View view, AdapterDataModel<Phone> model) {
        this.mView = view;
        this.mContext = ctx;
        this.mAdapter = model;
        countryCode = Locale.getDefault().getCountry();
    }

    @Override
    public void loadItems(@Nullable String queryString) {
        mView.loadingStart();
        mAdapter.clear();
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri localUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String selection = null;
        String[] selectionArgs = null;
        if (queryString != null)
        {
            selection = "display_name LIKE ? OR data1 LIKE ?";
            selectionArgs = new String[]{("%" + queryString + "%"), ("%" + queryString + "%")};
        }
        Cursor cursor = contentResolver.query(localUri, PROJECTION, selection, selectionArgs, "display_name ASC");
        if (cursor != null) {
            try
            {
                int i = cursor.getColumnIndex("display_name");
                int j = cursor.getColumnIndex("data1");
                int k = cursor.getColumnIndex("photo_thumb_uri");
                while (cursor.moveToNext())
                {
                    String name = cursor.getString(i);
                    String number = cursor.getString(j);
                    String photo = cursor.getString(k);
                    number = PhoneNumberUtils.formatNumber(number, countryCode);
                    mAdapter.addItem(new Phone(name, number, photo));
                }
            }
            finally
            {
                cursor.close();
                mView.loadingStop();
            }
        }
    }
}
