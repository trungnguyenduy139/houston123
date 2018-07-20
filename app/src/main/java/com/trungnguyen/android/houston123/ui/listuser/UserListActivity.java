package com.trungnguyen.android.houston123.ui.listuser;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.trungnguyen.android.houston123.R;
import com.trungnguyen.android.houston123.BR;
import com.trungnguyen.android.houston123.base.BaseToolbarActivity;
import com.trungnguyen.android.houston123.databinding.ActivityUserListBinding;
import com.trungnguyen.android.houston123.ui.userdetail.lecturer.LecturerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungnd4 on 13/07/2018.
 */
public class UserListActivity extends BaseToolbarActivity<ActivityUserListBinding, UserListViewModel> {

    private UserListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new UserListAdapter<>(initTemporaryData());

        viewModel.attachAdapter(mListAdapter);

        binding.userListRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.userListRecycler.setAdapter(mListAdapter);

        setTitle(getResources().getString(R.string.user_list));

        viewModel.getUserListLiveData().observe(this, o -> {

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListAdapter.releaseListener();
    }

    private List<LecturerModel> initTemporaryData() {
        List<LecturerModel> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LecturerModel userModel = new LecturerModel();
            userModel.setName("Samantha "+i);
            userModel.setPosition("Senior QC Engineer");
            userList.add(userModel);
        }
        return userList;
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_user_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public UserListViewModel initViewModel() {
        return new UserListViewModel(this, 1);
    }

}
