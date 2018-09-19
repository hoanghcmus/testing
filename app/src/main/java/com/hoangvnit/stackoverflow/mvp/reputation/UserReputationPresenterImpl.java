package com.hoangvnit.stackoverflow.mvp.reputation;

import com.hoangvnit.stackoverflow.R;
import com.hoangvnit.stackoverflow.mvp.adapter.UserAdapter;
import com.hoangvnit.stackoverflow.mvp.holder.UserViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;
import com.hoangvnit.stackoverflow.mvp.pojo.UserResponseModel;
import com.hoangvnit.stackoverflow.rest.RestClient;
import com.hoangvnit.stackoverflow.rest.UserService;
import com.hoangvnit.stackoverflow.rx.SimpleSubscriber;
import com.hoangvnit.stackoverflow.utils.LogUtils;
import com.hoangvnit.stackoverflow.utils.NetworkUtils;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserReputationPresenterImpl implements UserReputationContract.UserListPresenter {
    private UserReputationFragment mUserListView;
    private UserService mUserService;
    private Subscription mSubscription;
    private UserAdapter mUserAdapter;

    public UserReputationPresenterImpl(UserReputationFragment mUserListFragment) {
        this.mUserListView = mUserListFragment;
    }

    @Override
    public void onAttach(UserReputationContract.UserListView view) {

    }

    @Override
    public void onDetach(UserReputationContract.UserListView view) {

    }

    @Override
    public void init() {

        mUserService = RestClient.getInstance().getUserService();

        mUserAdapter = new UserAdapter(mUserListView.getContext(), R.layout.item_user, UserViewHolder.class);

        if (mUserListView != null) {
            if (NetworkUtils.isNetworkConnected(mUserListView.getContext())) {
                mUserListView.showProgressDialog();
                fetchUserList(1, 30);
            } else {
                mUserListView.adjustDisplayOfListUserSection(true);
                String message = mUserListView.getStr(R.string.msg_error_fail_to_load_user_list_without_network_connection);
                mUserListView.setMessage(message);
            }
        }
    }

    @Override
    public void loadMore(int page) {
        LogUtils.i("hcmus - load page: " + page);
        fetchUserList(page, 30);

    }

    @Override
    public void filterSofUser(boolean isFilter) {
        mUserAdapter.filterSofUser(isFilter);
    }

    private void fetchUserList(int page, int amount) {
        unSubscribe();
        mSubscription = mUserService.getUsers(page, amount, "stackoverflow")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<UserResponseModel>() {
                    @Override
                    public void onNext(UserResponseModel userResponse) {
                        if (mUserListView != null) {
                            mUserListView.hideProgressDialog();

                            if (userResponse != null) {
                                List<UserModel> users = userResponse.getItems();
                                mUserAdapter.setData(users);
                                mUserListView.setListUser(mUserAdapter);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mUserListView != null) {
                            mUserListView.hideProgressDialog();
                            String message = mUserListView.getStr(R.string.msg_error_fail_to_load_user_list);
                            mUserListView.adjustDisplayOfListUserSection(true);
                            mUserListView.setMessage(message + e.toString());
                        }
                        LogUtils.e(e.toString());
                    }
                });
    }

    @Override
    public void unSubscribe() {
        if (mSubscription != null) {
            if (!mSubscription.isUnsubscribed()) {
                mSubscription.unsubscribe();
            }
            mSubscription = null;
        }
    }


}
