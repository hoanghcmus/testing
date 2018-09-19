package com.hoangvnit.stackoverflow.mvp.userlist;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.hoangvnit.stackoverflow.R;
import com.hoangvnit.stackoverflow.mvp.adapter.BaseAdapter;
import com.hoangvnit.stackoverflow.mvp.holder.UserViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;
import com.hoangvnit.stackoverflow.mvp.pojo.UserResponseModel;
import com.hoangvnit.stackoverflow.rest.RestClient;
import com.hoangvnit.stackoverflow.rest.UserService;
import com.hoangvnit.stackoverflow.rx.SimpleSubscriber;
import com.hoangvnit.stackoverflow.utils.LogUtils;
import com.hoangvnit.stackoverflow.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserListPresenterImpl implements UserListContract.UserListPresenter {
    private UserListFragment mUserListView;
    private UserService mUserService;
    private Subscription mSubscription;
    private BaseAdapter<UserModel, UserViewHolder> mBaseAdapter;

    public UserListPresenterImpl(UserListFragment mUserListFragment) {
        this.mUserListView = mUserListFragment;
    }

    @Override
    public void onAttach(UserListContract.UserListView view) {

    }

    @Override
    public void onDetach(UserListContract.UserListView view) {

    }

    @Override
    public void init() {

        mUserService = RestClient.getInstance().getUserService();

        mBaseAdapter = new BaseAdapter<UserModel, UserViewHolder>(
                R.layout.item_user,
                UserViewHolder.class) {

            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, UserModel user, int position) {

                viewHolder.mTxtName.setText(user.getDisplay_name());
                viewHolder.mTxtReputation.setText("Rep: " + user.getReputation());
                if (user.is_employee()) {
                    viewHolder.mTxtSOFUser.setText(mUserListView.getContext().getString(R.string.sof_user));
                    viewHolder.mTxtSOFUser.setTextColor( ContextCompat.getColor(mUserListView.getContext(), R.color.colorPrimaryDark));
                } else {
                    viewHolder.mTxtSOFUser.setText(mUserListView.getContext().getString(R.string.not_sof_user));
                    viewHolder.mTxtSOFUser.setTextColor( ContextCompat.getColor(mUserListView.getContext(), R.color.grey));
                }
                Picasso.get().load(user.getUser_image())
                        .placeholder(R.drawable.user)
                        .into(viewHolder.mImgAvatar);
            }
        };

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
                                mBaseAdapter.setData(users);
                                mUserListView.setListUser(mBaseAdapter);
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
