package com.hoangvnit.stackoverflow.mvp.reputation;

import android.support.v4.content.ContextCompat;

import com.hoangvnit.stackoverflow.R;
import com.hoangvnit.stackoverflow.mvp.adapter.BaseAdapter;
import com.hoangvnit.stackoverflow.mvp.holder.UserReputationViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.ReputationModel;
import com.hoangvnit.stackoverflow.mvp.pojo.UserReputationResponseModel;
import com.hoangvnit.stackoverflow.rest.RestClient;
import com.hoangvnit.stackoverflow.rest.UserService;
import com.hoangvnit.stackoverflow.rx.SimpleSubscriber;
import com.hoangvnit.stackoverflow.utils.LogUtils;
import com.hoangvnit.stackoverflow.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserReputationPresenterImpl implements UserReputationContract.UserReputationPresenter {
    private UserReputationFragment mUserReputationListView;
    private UserService mUserService;
    private Subscription mSubscription;
    private BaseAdapter<ReputationModel, UserReputationViewHolder> mUserReputationAdapter;

    private int mUserId = 0;

    public UserReputationPresenterImpl(UserReputationFragment mUserReputationListFragment) {
        this.mUserReputationListView = mUserReputationListFragment;
    }

    @Override
    public void onAttach(UserReputationContract.UserReputationView view) {

    }

    @Override
    public void onDetach(UserReputationContract.UserReputationView view) {

    }

    @Override
    public void init(int userId) {

        this.mUserId = userId;

        mUserService = RestClient.getInstance().getUserService();

        mUserReputationAdapter = new BaseAdapter<ReputationModel, UserReputationViewHolder>(
                R.layout.item_reputation,
                UserReputationViewHolder.class
        ) {
            @Override
            protected void populateViewHolder(UserReputationViewHolder viewHolder, ReputationModel model, int position) {
                String type = model.getReputation_history_type().equals("post_upvoted") ? "Up voting" : "Down voting";
                viewHolder.mTxtReputationType.setText(type);
                viewHolder.mTxtReputationChange.setText("" + model.getReputation_change());
                viewHolder.mTxtPostId.setText("Post ID:\t" + model.getPost_id());

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(model.getCreation_date() * 1000);

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String date = String.format("%d/%d/%d", day, month, year);
                viewHolder.mTxtCreateDate.setText("Create date:\t" + date);

                int imageId = model.getReputation_history_type().equals("post_upvoted") ? R.drawable.ic_like : R.drawable.ic_unlike;
                Picasso.get().load(imageId).into(viewHolder.mImgReputationType);

            }
        };

        if (mUserReputationListView != null) {
            if (NetworkUtils.isNetworkConnected(mUserReputationListView.getContext())) {
                mUserReputationListView.showProgressDialog();
                fetchUserReputationList(mUserId, 1, 30);
            } else {
                mUserReputationListView.adjustDisplayOfListUserReputationSection(true);
                String message = mUserReputationListView.getStr(R.string.msg_error_fail_to_load_user_list_without_network_connection);
                mUserReputationListView.setMessage(message);
            }
        }
    }

    @Override
    public void loadMore(int page) {
        LogUtils.i("hcmus - load page: " + page);
        fetchUserReputationList(mUserId, page, 30);

    }


    private void fetchUserReputationList(int userId, int page, int amount) {
        unSubscribe();
        mSubscription = mUserService.getUserReputations(userId, page, amount, "stackoverflow")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<UserReputationResponseModel>() {
                    @Override
                    public void onNext(UserReputationResponseModel userReputationResponse) {
                        if (mUserReputationListView != null) {
                            mUserReputationListView.hideProgressDialog();

                            if (userReputationResponse != null) {
                                List<ReputationModel> reputations = userReputationResponse.getItems();
                                mUserReputationAdapter.setData(reputations);
                                mUserReputationListView.setListReputation(mUserReputationAdapter);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mUserReputationListView != null) {
                            mUserReputationListView.hideProgressDialog();
                            String message = mUserReputationListView.getStr(R.string.msg_error_fail_to_load_user_list);
                            mUserReputationListView.adjustDisplayOfListUserReputationSection(true);
                            mUserReputationListView.setMessage(message + e.toString());
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
