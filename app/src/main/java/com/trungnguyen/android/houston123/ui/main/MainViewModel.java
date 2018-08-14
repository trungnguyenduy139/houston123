package com.trungnguyen.android.houston123.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.trungnguyen.android.houston123.anotation.OnClick;
import com.trungnguyen.android.houston123.base.BaseViewModel;
import com.trungnguyen.android.houston123.util.Navigator;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel<IMainView> {

    private Context mContext;

    @Inject
    Navigator mNavigator;

    public MainViewModel(Context context) {

        super(context);
        mContext = context;
        getDataManagerComponent().inject(this);
    }

    @OnClick
    public void onTextClick() {
        Toast.makeText(mContext, "Click click boom", Toast.LENGTH_SHORT).show();
        mNavigator.startUserListActivity(mContext, new Bundle());
    }
}
