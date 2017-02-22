package com.goqual.a10k.presenter.impl;

import android.content.Context;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.SwitchService;
import com.goqual.a10k.presenter.SwitchPresenter;
import com.goqual.a10k.util.Errors;
import com.goqual.a10k.util.LogUtil;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.goqual.a10k.view.activities.ActivityMain.TAG;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public class SwitchPresenterImpl implements SwitchPresenter {
    private View mView;
    private FragmentStatePagerAdapter mAdapter;
    private SwitchService mSwitchService = null;
    private Context mContext = null;

    public SwitchPresenterImpl(Context ctx, SwitchPresenter.View view, FragmentStatePagerAdapter adapter) {
        mContext = ctx;
        mView = view;
        mAdapter = adapter;
    }
    @Override
    public void loadItems() {
        getSwitchService().getSwitchApi().gets()
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .filter(items -> items != null && !items.isEmpty())
                .flatMap(items -> Observable.from(items))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((item) -> {
                            SwitchManager.getInstance().addItem(item);
                            mView.addItem(item);
                        },
                        (e) -> {
                            if (e instanceof HttpException) {
                                HttpException error = (HttpException) e;
                                if (error.code() == Errors.ERROR_UNAUTHORIZED) {
                                    LogUtil.e(TAG, "unauthorized exception error");
                                    mView.onError(e);
                                }
                            } else {
                                e.printStackTrace();
                                LogUtil.e(TAG, "anonymous error");
                            }
                        },
                        mView::refresh);
    }

    @Override
    public void deleteItem(int position) {
        getSwitchService().getSwitchApi().delete(SwitchManager.getInstance().getItem(position).get_connectionid())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            //mView.leaveSocketRoom(position);
                            SwitchManager.getInstance().delete(position);
                            //mView.removeSwitchEach(position);
                            //mView.initIndicator();
                            mView.onSuccessDeleteSwitch(position);
                        },
                        (e)-> {
                            e.printStackTrace();
                            //mView.onFailDelete(position);
                        },
                        () -> mAdapter.notifyDataSetChanged());
    }

    @Override
    public void rename(int position, String title) {
        getSwitchService().getSwitchApi().rename(
                SwitchManager.getInstance().getItem(position).get_connectionid(), title)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            SwitchManager.getInstance().changeTitle(position, title);
                            mView.onSuccessRenameSwitch(position, title);
                        },
                        Throwable::printStackTrace,
                        () -> mAdapter.notifyDataSetChanged());
    }

    public SwitchService getSwitchService() {
        if (mSwitchService == null)
            mSwitchService = new SwitchService(mContext);

        return mSwitchService;
    }
}
