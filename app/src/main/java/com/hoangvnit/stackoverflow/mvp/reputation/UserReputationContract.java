package com.hoangvnit.stackoverflow.mvp.reputation;

import com.hoangvnit.stackoverflow.base.BaseContract;
import com.hoangvnit.stackoverflow.mvp.adapter.BaseAdapter;
import com.hoangvnit.stackoverflow.mvp.holder.UserViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;


/**
 * Contract for {@link UserReputationFragment} and {@link UserReputationPresenterImpl}
 *
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserReputationContract {

    public interface UserListPresenter extends BaseContract.BasePresenter<UserListView> {

        void init();

        void unSubscribe();

        void loadMore(int page);

        void  filterSofUser(boolean isFilter);
    }

    public interface UserListView extends BaseContract.BaseView {

        void adjustDisplayOfListUserSection(boolean isListUserEmpty);

        void setListUser(BaseAdapter<UserModel, UserViewHolder> mUserAdapter);

        void setMessage(String message);
    }
}
