package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.adapters.view.AdapterDataView;
import com.goqual.a10k.view.viewholders.UserViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ladmusician on 2016. 12. 9..
 */

public class AdapterUser extends RecyclerView.Adapter<UserViewHolder>
    implements AdapterDataModel<User>, AdapterDataView {

    private Context mContext = null;
    private List<User> mItemList = new ArrayList<>();
    private User mAdmin = null;
    private OnRecyclerItemClickListener mItemClickListener = null;
    private boolean mIsBorderView = true;

    public AdapterUser(Context mContext) {
        this.mContext = mContext;
    }

    public AdapterUser(Context mContext, boolean isBorderView) {
        this.mContext = mContext;
        this.mIsBorderView = isBorderView;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.bindView(position, mItemList.get(position), mItemClickListener, mIsBorderView);
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public void addItem(User item) {
        if (item.isadmin()) mAdmin = item;
        else mItemList.add(item);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void deleteItem(int position) {
        mItemList.remove(position);
    }

    @Override
    public int getSize() {
        return mItemList.size();
    }

    @Override
    public void clear() {
        mItemList.clear();
        refresh();
    }

    @Override
    public User getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public List<User> getItems() {
        return this.mItemList;
    }

    @Override
    public void updateItem(int position, User item) {
        mItemList.set(position, item);
    }

    @Override
    public void updateItems(List<User> items) {
        clear();
        mItemList.addAll(items);
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.mItemClickListener = onRecyclerItemClickListener;
    }

    public User getAdmin() {
        return mAdmin;
    }

    public void setAdmin(User admin) {
        this.mAdmin = admin;
    }

    public List<User> getmItemList() {
        return mItemList;
    }

    public void setNonChecked() {
        for(User each : mItemList)
            each.setmChecked(false);
    }

    public void handleChangeAdmin(int position) {
        User admin = mAdmin;
        mAdmin = getItem(position);
        mItemList.remove(position);
        mItemList.add(admin);
    }

    public void setDeletable(boolean state) {
        for(User item : mItemList) {
            item.setmIsDeletable(state);
        }

        refresh();
    }
}
