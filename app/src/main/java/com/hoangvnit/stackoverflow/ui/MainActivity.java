package com.hoangvnit.stackoverflow.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.hoangvnit.stackoverflow.R;
import com.hoangvnit.stackoverflow.base.BaseActivity;
import com.hoangvnit.stackoverflow.common.FRAGMENT_ID;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;
import com.hoangvnit.stackoverflow.mvp.reputation.UserReputationFragment;
import com.hoangvnit.stackoverflow.mvp.userlist.UserListFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements UserListFragment.OnFragmentInteractionListener, UserReputationFragment.OnFragmentInteractionListener {

    @BindView(R.id.fragment_list_user)
    FrameLayout mUserListFrameLayout;

    @BindView(R.id.fragment_user_reputation)
    FrameLayout mUserReputationFrameLayout;

    private FragmentManager fragmentManager;
    private boolean isShowingDetail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);

        UserListFragment userListFragment = (UserListFragment) getFragment(FRAGMENT_ID.USER_LIST_FRAGMENT);
        UserReputationFragment userReputationFragment = (UserReputationFragment) getFragment(FRAGMENT_ID.USER_REPUTATION_FRAGMENT);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_list_user, userListFragment, FRAGMENT_ID.USER_LIST_FRAGMENT.getKey());
        fragmentTransaction.add(R.id.fragment_user_reputation, userReputationFragment, FRAGMENT_ID.USER_REPUTATION_FRAGMENT.getKey());

        fragmentTransaction.commit();
        showDetail(false);
    }

    public void showDetail(boolean isShow) {
        isShowingDetail = isShow;
        mUserReputationFrameLayout.setVisibility(isShowingDetail ? View.VISIBLE : View.INVISIBLE);
        mUserListFrameLayout.setVisibility(isShowingDetail ? View.INVISIBLE : View.VISIBLE);
        setActionBarTitle(isShowingDetail ? getStr(R.string.title_user_reputation) : getStr(R.string.title_user_list));
    }

    @Override
    public void onBackPressed() {
        if (isShowingDetail) {
            showDetail(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentReputationInteraction(UserModel userModel) {

    }

    @Override
    public void onFragmentUserListInteraction(UserModel userModel) {
        UserReputationFragment userReputationFragment = (UserReputationFragment) fragmentManager.findFragmentById(R.id.fragment_user_reputation);
        if (userReputationFragment != null) {
            userReputationFragment.onShowReputation(userModel);
        }
    }
}
