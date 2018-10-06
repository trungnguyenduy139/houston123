package com.trungnguyen.android.houston123.ui.listuser;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trungnguyen.android.houston123.R;
import com.trungnguyen.android.houston123.anotation.InfinityAdapterType;
import com.trungnguyen.android.houston123.anotation.ToastType;
import com.trungnguyen.android.houston123.base.BaseInfinityAdapter;
import com.trungnguyen.android.houston123.base.BaseUserModel;
import com.trungnguyen.android.houston123.base.BaseViewHolder;
import com.trungnguyen.android.houston123.databinding.UserListItemBinding;
import com.trungnguyen.android.houston123.widget.ToastCustom;

import java.util.List;
import java.util.Objects;

/**
 * Created by trungnd4 on 13/07/2018.
 */

public class UserListAdapter<U extends BaseUserModel> extends BaseInfinityAdapter<UserListListener> {

    private List<U> mListUser;

    UserListAdapter(List<U> listUser) {
        mListUser = listUser;
    }

    @Override
    public boolean enableInfinity() {
        return true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.user_list_item;
    }

    @Override
    public int getDataSize() {
        if (mListUser == null) {
            return 0;
        }
        return mListUser.size();
    }

    public void addItems(@Nullable List<U> lecturerModels) {
        if (lecturerModels == null) {
            return;
        }
        mListUser.addAll(lecturerModels);
        notifyDataSetChanged();
    }

    public void removeUser(int position) {
        mListUser.remove(position);
        notifyDataSetChanged();
    }

    public class UserListViewHolder extends BaseViewHolder implements UserItemViewModel.OnUserClickListener {

        private UserItemViewModel userItemViewModel;

        private UserListItemBinding mBinding;

        UserListViewHolder(@NonNull UserListItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            final BaseUserModel user = mListUser.get(position);

            userItemViewModel = new UserItemViewModel(user, this);

            mBinding.setViewModel(userItemViewModel);

            mBinding.executePendingBindings();

        }

        @Override
        public void onItemClick() {
            ToastCustom.makeText(context, "Clicked position " +
                    getAdapterPosition(), ToastCustom.LENGTH_SHORT, ToastType.TYPE_GENERAL);
            if (mListener == null) {
                return;
            }
            mListener.onItemClick(mListUser.get(getAdapterPosition()));
        }

        @Override
        public boolean onUserLongClick() {
            if (mListener == null) {
                return false;
            }
            int position = getAdapterPosition();
            mListener.onItemLongClick(position);
            return true;
        }
    }

    @UiThread
    public void clearItems() {
        if (mListUser == null) {
            return;
        }
        mListUser.clear();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View rootView;
        switch (viewType) {
            case InfinityAdapterType.TYPE_NORMAL:
                rootView = LayoutInflater.from(context).inflate(getLayoutResource(), parent, false);
                break;
            case InfinityAdapterType.TYPE_LOADING:
                return getLoadingViewHolder(parent);
            default:
                rootView = LayoutInflater.from(context).inflate(getLayoutResource(), parent, false);
                break;
        }
        return new UserListViewHolder(Objects.requireNonNull(DataBindingUtil.bind(rootView)));
    }
}
