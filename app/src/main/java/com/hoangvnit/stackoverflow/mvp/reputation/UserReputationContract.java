package com.hoangvnit.stackoverflow.mvp.reputation;

import android.view.View;

import com.hoangvnit.stackoverflow.base.BaseContract;
import com.hoangvnit.stackoverflow.mvp.adapter.BaseAdapter;
import com.hoangvnit.stackoverflow.mvp.holder.UserReputationViewHolder;
import com.hoangvnit.stackoverflow.mvp.holder.UserViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.ReputationModel;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;


/**
 * Contract for {@link UserReputationFragment} and {@link UserReputationPresenterImpl}
 *
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserReputationContract {

    public interface UserReputationPresenter extends BaseContract.BasePresenter<UserReputationView> {

        void init(int userId);

        void unSubscribe();

        void loadMore(int page);
    }

    public interface UserReputationView extends BaseContract.BaseView {

        public void adjustDisplayOfListUserReputationSection(boolean isListUserEmpty);

        public void setListReputation(BaseAdapter<ReputationModel, UserReputationViewHolder> userReputationAdapter);

        public void setMessage(String message);
    }
}
