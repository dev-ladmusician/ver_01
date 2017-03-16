package com.goqual.a10k.presenter.impl;

import android.content.Context;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.PagenationWrapper;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.SwitchService;
import com.goqual.a10k.presenter.SwitchPresenter;
import com.goqual.a10k.util.HttpResponseCode;
import com.goqual.a10k.util.LogUtil;

import org.apache.commons.lang3.NotImplementedException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.goqual.a10k.view.activities.ActivityMain.TAG;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public class SwitchPresenterImpl implements SwitchPresenter {
    private View<Switch> mView;
    private FragmentStatePagerAdapter mAdapter;
    private SwitchService mSwitchService = null;
    private Context mContext = null;

    public SwitchPresenterImpl(Context ctx, SwitchPresenter.View<Switch> view, FragmentStatePagerAdapter adapter) {
        mContext = ctx;
        mView = view;
        mAdapter = adapter;
    }

    @Override
    @Deprecated
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
                        mView::onError,
                        mView::refresh);
    }

    @Override
    public void loadItems(int page) {
        getSwitchService().getSwitchApi().gets(page)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(PagenationWrapper::getResult)
                .filter(items -> items != null && !items.isEmpty())
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((item) -> {
                            SwitchManager.getInstance().addItem(item);
                            mView.addItem(item);
                        },
                        mView::onError,
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

    @Override
    public void getByBsid(int _bsid) {
        getSwitchService().getSwitchApi().getSwitchByBsid(_bsid)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .filter(items -> items != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mView.addItem(resultDTO);
                        },
                        Throwable::printStackTrace,
                        () -> mAdapter.notifyDataSetChanged());
    }

    @Override
    public void add(String macaddr, String title, int count) {
        getSwitchService().getSwitchApi().add(macaddr, title, count)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((x) -> {
                            mView.loadingStop();
                        },
                        e -> mView.onError(e),
                        mView::loadingStop
                );
    }

    public SwitchService getSwitchService() {
        if (mSwitchService == null)
            mSwitchService = new SwitchService(mContext);

        return mSwitchService;
    }
}
