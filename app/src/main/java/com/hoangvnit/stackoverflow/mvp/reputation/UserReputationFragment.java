package com.hoangvnit.stackoverflow.mvp.reputation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hoangvnit.stackoverflow.R;
import com.hoangvnit.stackoverflow.base.BaseFragment;
import com.hoangvnit.stackoverflow.common.EndlessRecyclerViewScrollListener;
import com.hoangvnit.stackoverflow.mvp.adapter.BaseAdapter;
import com.hoangvnit.stackoverflow.mvp.holder.UserReputationViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.ReputationModel;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserReputationFragment extends BaseFragment
        implements UserReputationContract.UserReputationView {

    @BindView(R.id.name)
    public TextView mTxtName;

    @BindView(R.id.location)
    public TextView mTxtLocation;

    @BindView(R.id.avatar)
    public CircleImageView mImgAvatar;

    @BindView(R.id.empty_view)
    TextView mTxtMessage;

    @BindView(R.id.user_reputation_list_recycler_view)
    RecyclerView mRclUserReputationList;

    private UserReputationContract.UserReputationPresenter mUserReputationPresenter;
    private LinearLayoutManager mLinearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private BaseAdapter<ReputationModel, UserReputationViewHolder> mUserReputationAdapter;


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_user_reputation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserReputationPresenter = new UserReputationPresenterImpl(this);
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
        setActionBarTitle(getStr(R.string.title_user_reputation));

        initListView();
    }

    public void onShowReputation(UserModel userModel) {
        int userId = 0;
        if (userModel != null) {
            userId = userModel.getUser_id();
            updateViewData(userModel);
        }
        if (mUserReputationPresenter != null) {
            mUserReputationPresenter.init(userId);
        }
    }

    private void updateViewData(UserModel userModel) {
        mTxtName.setText(userModel.getDisplay_name());
        mTxtLocation.setText("Address: \t" + userModel.getLocation());
        Picasso.get().load(userModel.getUser_image())
                .into(mImgAvatar);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mUserReputationPresenter != null) {
            mUserReputationPresenter.unSubscribe();
        }
    }

    private void initListView() {
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRclUserReputationList.setLayoutManager(mLinearLayoutManager);
        scrollListener = getScrollListener();
        mRclUserReputationList.addOnScrollListener(scrollListener);
    }

    private EndlessRecyclerViewScrollListener getScrollListener() {
        return new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (mUserReputationPresenter != null) {
                    mUserReputationPresenter.loadMore(page + 1);
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
    public void adjustDisplayOfListUserReputationSection(boolean isListUserReputationEmpty) {
        mTxtMessage.setVisibility(isListUserReputationEmpty ? View.VISIBLE : View.GONE);
        mRclUserReputationList.setVisibility(isListUserReputationEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setListReputation(BaseAdapter<ReputationModel, UserReputationViewHolder> userReputationAdapter) {
        if (mUserReputationAdapter == null) {
            mUserReputationAdapter = userReputationAdapter;
            mRclUserReputationList.setAdapter(mUserReputationAdapter);
        }
        boolean isListReputationEmpty = mUserReputationAdapter.getItemCount() == 0;
        adjustDisplayOfListUserReputationSection(isListReputationEmpty);
        setMessage(getStr(isListReputationEmpty ? R.string.msg_info_list_user_empty : R.string.empty));
    }

    @Override
    public void setMessage(String message) {
        mTxtMessage.setText(message);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentReputationInteraction(UserModel userModel);
    }

}
