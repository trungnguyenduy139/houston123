package com.trungnguyen.android.houston123.ui.main.home;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trungnguyen.android.houston123.R;
import com.trungnguyen.android.houston123.base.AbsCommonAdapter;
import com.trungnguyen.android.houston123.base.BaseViewHolder;
import com.trungnguyen.android.houston123.databinding.HomeRowItemBinding;

import java.util.List;
import java.util.Objects;

/**
 * Created by trungnd4 on 19/08/2018.
 */
public class HomeAdapter extends AbsCommonAdapter<HomeAdapterListener, HomeItem> {

    public HomeAdapter(List<HomeItem> dataList) {
        super(dataList);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.home_row_item;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View rootView = LayoutInflater.from(context).inflate(getLayoutResource(), viewGroup, false);
        return new HomeAdapter.HomeViewHolder(Objects.requireNonNull(DataBindingUtil.bind(rootView)));
    }

    public class HomeViewHolder extends BaseViewHolder {

        private HomeItemViewModel homeItemViewModel;

        private HomeRowItemBinding mBinding;

        HomeViewHolder(@NonNull HomeRowItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            final HomeItem homeItem = mDataList.get(position);

            homeItemViewModel = new HomeItemViewModel(homeItem);

            mBinding.setViewModel(homeItemViewModel);

            mBinding.executePendingBindings();

        }
    }
}