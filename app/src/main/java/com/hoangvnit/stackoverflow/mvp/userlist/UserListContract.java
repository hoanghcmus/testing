package com.hoangvnit.stackoverflow.mvp.userlist;

import com.hoangvnit.stackoverflow.base.BaseContract;
import com.hoangvnit.stackoverflow.mvp.adapter.BaseAdapter;
import com.hoangvnit.stackoverflow.mvp.holder.UserViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;


/**
 * Contract for {@link UserListFragment} and {@link UserListPresenterImpl}
 *
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserListContract {

    public interface UserListPresenter extends BaseContract.BasePresenter<UserListView> {

        void init();

        void unSubscribe();

        void loadMore(int page);

        void  filterSofUser(boolean isFilter);
    }

    public interface UserListView extends BaseContract.BaseView {

        void adjustDisplayOfListUserSection(boolean isListUserEmpty);

        void setListUser(BaseAdapter<UserModel, UserViewHolder> mUserAdapter);

        void viewReputationDetail(UserModel model);

        void setMessage(String message);
    }
}
