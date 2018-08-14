package com.trungnguyen.android.houston123.ui.userdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.trungnguyen.android.houston123.R;
import com.trungnguyen.android.houston123.BR;

import com.trungnguyen.android.houston123.base.BaseToolbarActivity;
import com.trungnguyen.android.houston123.base.BaseUserModel;
import com.trungnguyen.android.houston123.databinding.ActivityLecturerBinding;
import com.trungnguyen.android.houston123.util.BundleConstants;

import java.util.ArrayList;
import java.util.List;

public class LecturerActivity extends BaseToolbarActivity<ActivityLecturerBinding, LecturerViewModel>
        implements ILecturerView {

    private List<ItemDetailModel> mItemDetailList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        BaseUserModel baseUserModel = null;
        if (bundle != null) {
            baseUserModel = (BaseUserModel) bundle.getSerializable(BundleConstants.KEY_USER_DETAIL);
        }
        if (baseUserModel != null) {
            viewModel.setLecturerModel(baseUserModel);
            mItemDetailList.addAll(viewModel.initDetailList(baseUserModel));
        }
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_lecturer;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @NonNull
    @Override
    public LecturerViewModel initViewModel() {
        return new LecturerViewModel(this);
    }
}
