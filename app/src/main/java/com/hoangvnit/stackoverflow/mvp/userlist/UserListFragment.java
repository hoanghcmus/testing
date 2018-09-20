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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hoangvnit.stackoverflow.R;
import com.hoangvnit.stackoverflow.base.BaseFragment;
import com.hoangvnit.stackoverflow.common.EndlessRecyclerViewScrollListener;
import com.hoangvnit.stackoverflow.common.Setting;
import com.hoangvnit.stackoverflow.mvp.adapter.BaseAdapter;
import com.hoangvnit.stackoverflow.mvp.holder.UserViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;
import com.hoangvnit.stackoverflow.ui.MainActivity;
import com.hoangvnit.stackoverflow.utils.LogUtils;
import com.hoangvnit.stackoverflow.utils.PreferencesUtils;

import butterknife.BindView;

/**
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserListFragment extends BaseFragment
        implements UserListContract.UserListView {

    @BindView(R.id.filter_sof_usr_switch)
    Switch mSwFilterSofUser;

    @BindView(R.id.empty_view)
    TextView mTxtMessage;

    @BindView(R.id.user_list_recycler_view)
    RecyclerView mRclUserList;

    private UserListContract.UserListPresenter mUserListPresenter;
    private LinearLayoutManager mLinearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private BaseAdapter<UserModel, UserViewHolder> mUserAdapter;

    private NetworkStateReceiver mNetworkStateReceiver;
    private boolean isFilterSofUser = false;

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

        isFilterSofUser = PreferencesUtils.getInstance(getActivity().getApplicationContext()).getBooleanValue(Setting.IS_FILTER_SOF_USER_KEY, false);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected void initView() {
        mSwFilterSofUser.setChecked(isFilterSofUser);
        mSwFilterSofUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isFilterSofUser = isChecked;
                PreferencesUtils.getInstance(getActivity().getApplicationContext()).setBooleanValue(Setting.IS_FILTER_SOF_USER_KEY, isChecked);
                if (mUserListPresenter != null) {
                    mUserListPresenter.filterSofUser(isFilterSofUser);
                }
            }
        });

        setActionBarTitle(getStr(R.string.title_user_list));

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
        scrollListener = getScrollListener();
        mRclUserList.addOnScrollListener(scrollListener);
    }

    private EndlessRecyclerViewScrollListener getScrollListener() {
        return new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!isFilterSofUser) {
                    if (mUserListPresenter != null) {
                        mUserListPresenter.loadMore(page + 1);
                    }
                }
            }
        };
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
        mSwFilterSofUser.setVisibility(isListUserEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setListUser(BaseAdapter<UserModel, UserViewHolder> userAdapter) {
        if (mUserAdapter == null) {
            mUserAdapter = userAdapter;
            mRclUserList.setAdapter(mUserAdapter);
        }
        boolean isListUserEmpty = mUserAdapter.getItemCount() == 0;
        adjustDisplayOfListUserSection(isListUserEmpty);
        setMessage(getStr(isListUserEmpty ? R.string.msg_info_list_user_empty : R.string.empty));

        if (mUserListPresenter != null) {
            mUserListPresenter.filterSofUser(isFilterSofUser);
        }
    }

    @Override
    public void viewReputationDetail(UserModel model) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.onFragmentUserListInteraction(model);
        mainActivity.showDetail(true);
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
                        Toast.makeText(context,
                                context.getResources().getString(R.string.msg_network_connected),
                                Toast.LENGTH_SHORT).show();
                        if (mUserListPresenter != null) {
                            mUserListPresenter.init();
                        }
                    }
                    isFirstTime = false;
                } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                    Toast.makeText(context,
                            context.getResources().getString(R.string.msg_network_dis_connected),
                            Toast.LENGTH_SHORT).show();
                    isFirstTime = false;
                }
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentUserListInteraction(UserModel userModel);
    }
}
