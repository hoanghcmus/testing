package com.hoangvnit.stackoverflow.mvp.userlist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hoangvnit.stackoverflow.R;
import com.hoangvnit.stackoverflow.base.BaseFragment;
import com.hoangvnit.stackoverflow.mvp.adapter.BaseAdapter;
import com.hoangvnit.stackoverflow.mvp.holder.UserViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;
import com.hoangvnit.stackoverflow.utils.LogUtils;

import butterknife.BindView;

/**
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserListFragment extends BaseFragment
        implements UserListContract.UserListView {

    @BindView(R.id.empty_view)
    TextView mTxtMessage;

    @BindView(R.id.user_list_recycler_view)
    RecyclerView mRclUserList;

    private UserListContract.UserListPresenter mUserListPresenter;
    private LinearLayoutManager mLinearLayoutManager;

    private NetworkStateReceiver mNetworkStateReceiver;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserListPresenter = new UserListPresenterImpl(this);
        IntentFilter intentFilterNetworkChange = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkStateReceiver = new NetworkStateReceiver(mUserListPresenter);
        getActivity().registerReceiver(mNetworkStateReceiver, intentFilterNetworkChange);
    }

    @Override
    protected void initView() {
        initListView();
        if (mUserListPresenter != null) {
            mUserListPresenter.init();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(mNetworkStateReceiver);
        } catch (Exception e) {
            LogUtils.e("Unregister network receiver exception: " + e.getMessage());
        }
        if (mUserListPresenter != null) {
            mUserListPresenter.unSubscribe();
        }
    }

    private void initListView() {
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRclUserList.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void adjustDisplayOfListUserSection(boolean isListUserEmpty) {
        mTxtMessage.setVisibility(isListUserEmpty ? View.VISIBLE : View.GONE);
        mRclUserList.setVisibility(isListUserEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setListUser(BaseAdapter<UserModel, UserViewHolder> mUserAdapter) {
        mRclUserList.setAdapter(mUserAdapter);
        boolean isListUserEmpty = mUserAdapter.getItemCount() == 0;
        adjustDisplayOfListUserSection(isListUserEmpty);
        setMessage(getStr(isListUserEmpty ? R.string.msg_info_list_user_empty : R.string.empty));
    }

    @Override
    public void setMessage(String message) {
        mTxtMessage.setText(message);
    }

    public static class NetworkStateReceiver extends BroadcastReceiver {
        private UserListContract.UserListPresenter mUserListPresenter;
        private static boolean isFirstTime = true;

        public NetworkStateReceiver(UserListContract.UserListPresenter userListPresenter) {
            this.mUserListPresenter = userListPresenter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                    if (!isFirstTime) {
                        LogUtils.e("Network connected");
                        Toast.makeText(context,
                                context.getResources().getString(R.string.msg_network_connected),
                                Toast.LENGTH_SHORT).show();
                        if (mUserListPresenter != null) {
                            mUserListPresenter.init();
                        }
                    }
                    isFirstTime = false;
                } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                    LogUtils.e("Network disconnected");
                    Toast.makeText(context,
                            context.getResources().getString(R.string.msg_network_dis_connected),
                            Toast.LENGTH_SHORT).show();
                    isFirstTime = false;
                }
            }
        }
    }
}
