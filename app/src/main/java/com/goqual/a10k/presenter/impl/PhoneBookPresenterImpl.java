 package com.goqual.a10k.presenter.impl;

 import android.content.ContentResolver;
 import android.content.Context;
 import android.database.Cursor;
 import android.net.Uri;
 import android.provider.ContactsContract;
 import android.support.annotation.Nullable;
 import android.telephony.PhoneNumberUtils;

 import com.goqual.a10k.model.entity.Phone;
 import com.goqual.a10k.model.entity.User;
 import com.goqual.a10k.model.remote.service.InviteService;
 import com.goqual.a10k.presenter.PhoneBookPresenter;
 import com.goqual.a10k.util.LogUtil;
 import com.goqual.a10k.view.adapters.model.AdapterDataModel;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.Locale;

 import rx.android.schedulers.AndroidSchedulers;
 import rx.schedulers.Schedulers;

 /**
 * Created by ladmusician on 2017. 1. 17..
 */

public class PhoneBookPresenterImpl implements PhoneBookPresenter {
    private static final String TAG = PhoneBookPresenterImpl.class.getName();
    private View mView;
    private Context mContext;
    private InviteService mInviteService;
    private AdapterDataModel<Phone> mAdapter;
    private String countryCode;
    private List<Phone> mUserList;

    private static final String[] PROJECTION = { "contact_id", "display_name", "data1", "photo_thumb_uri" };

    public PhoneBookPresenterImpl(Context ctx, View view, AdapterDataModel<Phone> model) {
        this.mView = view;
        this.mContext = ctx;
        this.mAdapter = model;
        this.mUserList = new ArrayList<>();
        countryCode = Locale.getDefault().getCountry();
    }

    @Override
    public void loadItems(List<User> connectedUser, @Nullable String queryString) {
        mView.loadingStart();
        mAdapter.clear();
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri localUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String selection = null;
        String[] selectionArgs = null;
        if (queryString != null) {
            selection = "display_name LIKE ? OR data1 LIKE ?";
            selectionArgs = new String[]{("%" + queryString + "%"), ("%" + queryString + "%")};
        }

        Cursor cursor = contentResolver.query(localUri, PROJECTION, selection, selectionArgs, "display_name ASC");
        if (cursor != null) {
            try {
                int i = cursor.getColumnIndex("display_name");
                int j = cursor.getColumnIndex("data1");
                int k = cursor.getColumnIndex("photo_thumb_uri");

                while (cursor.moveToNext()) {
                    String name = cursor.getString(i);
                    String number = cursor.getString(j);
                    String photo = cursor.getString(k);
                    number = PhoneNumberUtils.formatNumber(number, countryCode);

                    boolean isConnected = false;

                    for(User each : connectedUser) {
                        if (each.getNum().equals(number.replace("-",""))) isConnected = true;
                    }

                    mAdapter.addItem(new Phone(name, number.replace("-", "."), photo, isConnected));
                    mUserList.add(new Phone(name, number.replace("-", ""), photo, isConnected));
                }
            } finally {
                cursor.close();
                mView.refresh();
            }
        }
    }

    @Override
    public void checkUser(int bsId, int position) {
        mView.loadingStart();
        Phone user = mAdapter.getItem(position);
        getInviteService().getInviteApi().checkUserIsJoinedAndConnected(user.getNumber(), bsId)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            LogUtil.e(TAG, resultDTO.toString());
                            mView.successInvite(position);
                        },
                        (e)-> {mView.errorInvite(position);},
                        () -> {
                            mView.refresh();
                        });
    }

    private InviteService getInviteService() {
        if (mInviteService == null)
            mInviteService = new InviteService(mContext);

        return mInviteService;
    }
}
