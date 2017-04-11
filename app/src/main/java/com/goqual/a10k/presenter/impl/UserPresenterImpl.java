package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.UserService;
import com.goqual.a10k.presenter.UserPresenter;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.adapters.AdapterUser;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public class UserPresenterImpl implements UserPresenter {
    private static final String TAG = UserPresenterImpl.class.getSimpleName();

    private Context mContext;
    private View<User> mView;
    private AdapterDataModel<User> mAdapterDataModel;
    private UserService mUserService;

    public UserPresenterImpl(Context ctx, View<User> mView, AdapterDataModel<User> dataModel) {
        this.mContext = ctx;
        this.mView = mView;
        this.mAdapterDataModel = dataModel;
    }

    @Override
    public void changeAdmin(int position) {
        final AdapterUser adapter = (AdapterUser)mAdapterDataModel;
        LogUtil.e(TAG, "TEST : " + adapter.getAdmin().get_connectionid());
        getUserService().getUserApi().changeAdmin(adapter.getAdmin().get_connectionid(),
                (mAdapterDataModel.getItem(position)).get_connectionid())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mView.handleChangeAdmin(position);
                        },
                        mView::onError,
                        mView::refresh);
    }

    @Override
    public void loadItems(int switchId) {
        getUserService().getUserApi().gets(switchId)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .filter(items -> items != null && !items.isEmpty())
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::addItem,
                        mView::onError,
                        mView::refresh);
    }

    @Override
    public void delete(int position) {
        User item = mAdapterDataModel.getItem(position);
        getUserService().getUserApi().delete(item.get_connectionid())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mAdapterDataModel.deleteItem(position);
                        },
                        Throwable::printStackTrace,
                        () -> mView.refresh());
    }

    private UserService getUserService() {
        if (mUserService == null)
            mUserService = new UserService(mContext);

        return mUserService;
    }
}
