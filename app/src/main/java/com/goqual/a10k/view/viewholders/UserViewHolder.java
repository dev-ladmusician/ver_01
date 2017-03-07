package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ItemSwitchBinding;
import com.goqual.a10k.databinding.ItemUserBinding;
import com.goqual.a10k.model.entity.Switch;
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

    public void bindView(int position, User item, OnRecyclerItemClickListener listener) {
        mListener = listener;
        mPosition = position;
        binding.setUser(item);
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }
}
