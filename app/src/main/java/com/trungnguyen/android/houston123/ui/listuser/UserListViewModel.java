package com.trungnguyen.android.houston123.ui.listuser;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.trungnguyen.android.houston123.base.BaseModel;
import com.trungnguyen.android.houston123.base.BaseUserModel;
import com.trungnguyen.android.houston123.base.BaseViewModel;
import com.trungnguyen.android.houston123.repository.userlist.UserListRepository;
import com.trungnguyen.android.houston123.repository.userlist.UserListStore;
import com.trungnguyen.android.houston123.rx.SchedulerHelper;
import com.trungnguyen.android.houston123.util.BundleBuilder;
import com.trungnguyen.android.houston123.util.BundleConstants;
import com.trungnguyen.android.houston123.util.Constants;
import com.trungnguyen.android.houston123.util.Lists;
import com.trungnguyen.android.houston123.util.Navigator;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by trungnd4 on 13/07/2018.
 */
public class UserListViewModel extends BaseViewModel<IUserListView> implements UserListListener {

    private Navigator mNavigator;
    private UserListStore.Repository mUserListRepository;
    private UserListAdapter<BaseModel> mAdapter;


    @NonNull
    private final MutableLiveData<List<BaseViewModel>> mUserListLiveData;

    @Inject
    UserListViewModel(Context context, Navigator navigator, UserListRepository userListRepository) {
        super(context);
        mUserListLiveData = new MutableLiveData<>();
        this.mNavigator = navigator;
        this.mUserListRepository = userListRepository;
    }

    @NonNull
    public MutableLiveData<List<BaseViewModel>> getUserListLiveData() {
        return mUserListLiveData;
    }

    public void attachAdapter(@NonNull UserListAdapter<BaseModel> adapter) {
        adapter.setListener(this);
        mAdapter = adapter;
    }

    @Override
    public void onItemClick(@NonNull BaseModel baseUserModel, int position) {
        if (!(mView instanceof UserListActivity)) {
            return;
        }
        UserListActivity activity = ((UserListActivity) mView);
        if (activity.isFinishing()) {
            return;
        }
        Bundle bundle = new BundleBuilder()
                .putValue(BundleConstants.KEY_USER_DETAIL, baseUserModel)
                .putValue(BundleConstants.KEY_CODE_DETAIL, mView == null ? Constants.DEFAULT_CODE_VALUE : mView.getCurrentUserCode())
                .putValue(BundleConstants.KEY_POSITION_USER, position)
                .build();
        mNavigator.startDetailActivity(activity, bundle == null ? new Bundle() : bundle);
    }

    @Override
    public void onItemLongClick(int position) {
        if (mView == null) {
            return;
        }
        mView.showConfirmDeleteUserDialog(position);
    }

    public void onSearchTextChanged(CharSequence text) {
        if (mView != null) {
            String textSearch = text.toString();
            mView.doSearchAction(textSearch);
        }
    }

    public void refreshList(int code) {
        Disposable subscription = mUserListRepository.handleUserServiceFlow(code, Constants.FIRST_PAGE_REQUEST)
                .filter(models -> !Lists.isEmptyOrNull(models))
                .compose(SchedulerHelper.applySchedulers())
                .doOnSubscribe(disposable -> showLoading())
                .doOnTerminate(() -> {
                    hideLoading();
                    if (mView != null) {
                        mView.setRefreshing(false);
                    }
                })
                .subscribe(usersModels -> {
                    if (mView != null) {
                        mView.doRefreshList(usersModels);
                    }
                });
        mSubscription.add(subscription);
    }

    public void nextPage(int code) {
        Observable<Integer> localPageObservable = mUserListRepository.getPageFromLocal()
                .map(integer -> ++integer)
                .firstOrError()
                .toObservable();

        Disposable subscription = localPageObservable
                .doOnNext(integer -> Timber.d("current page is [%s]", integer))
                .flatMap(integer -> mUserListRepository.handleUserServiceFlow(code, integer))
                .compose(SchedulerHelper.applySchedulersLoadingAction(this::showLoading, this::hideLoading))
                .subscribe(baseResponse -> {
                    if (mView != null) {
                        mView.doLoadMore(baseResponse);
                    }
                });

        mSubscription.add(subscription);
    }

    public void doRemoveUser(int code, int position, BaseModel baseUserModel) {
        if (!(baseUserModel instanceof BaseUserModel)) {
            return;
        }

        Disposable subscription = mUserListRepository.handleRemoveUserFlow(code, ((BaseUserModel)baseUserModel).getUserId())
                .compose(SchedulerHelper.applySchedulersLoadingAction(this::showLoading, this::hideLoading))
                .subscribe(baseResponse -> {
                    if (mView != null) {
                        mView.successToDeleteUser(position);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.failedToDeleteUser();
                    }
                });
        mSubscription.add(subscription);
    }

    public void doSearchAction(String searchSequence) {
        Disposable subscription = mAdapter.searchAction(searchSequence)
                .compose(SchedulerHelper.applySchedulers())
                .subscribe(val -> {
                    if (mView != null) {
                        mView.handleSearchResult();
                    }
                });
        mSubscription.add(subscription);
    }
}
