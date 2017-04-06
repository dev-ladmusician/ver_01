package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemUserBinding;
import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by ladmusician on 2016. 12. 9..
 */

public class UserViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    ItemUserBinding binding;

    private int mPosition;
    private OnRecyclerItemClickListener mListener;

    public UserViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
        binding.setHolder(this);
    }

    public void bindView(int position, User item, OnRecyclerItemClickListener listener, boolean isBqorderView) {
        mListener = listener;
        mPosition = position;
        binding.setUser(item);
        binding.setBorderView(isBqorderView);

        binding.itemUserDelete.setOnClickListener(v -> {
            listener.onItemClick(binding.itemUserDelete.getId(), position);
        });

        binding.itemUserContainer.setOnClickListener(v -> {
            listener.onItemClick(binding.itemUserDelete.getId(), position);
        });
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }
}
