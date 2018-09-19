package com.hoangvnit.stackoverflow.mvp.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.Filter;
import android.widget.Filterable;

import com.hoangvnit.stackoverflow.R;
import com.hoangvnit.stackoverflow.mvp.holder.UserViewHolder;
import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter<UserModel, UserViewHolder> implements Filterable {

    private boolean isFilterSofUser = false;

    public UserAdapter(Context context, int modelLayout, Class viewHolderClass) {
        super(context, modelLayout, viewHolderClass);
    }

    public UserAdapter(int modelLayout, Class viewHolderClass) {
        super(modelLayout, viewHolderClass);
    }


    @Override
    protected void populateViewHolder(UserViewHolder viewHolder, UserModel model, int position) {
        viewHolder.mTxUserId.setText("" + model.getUser_id());
        viewHolder.mTxtName.setText(model.getDisplay_name());
        viewHolder.mTxtAge.setText("" + model.getAge());
        viewHolder.mTxtLocation.setText("Addr:\t" + model.getLocation());
        viewHolder.mTxtReputation.setText("" + model.getReputation());
        if (model.is_employee()) {
            viewHolder.mTxtSOFUser.setText(mContext.getString(R.string.sof_user));
            viewHolder.mTxtSOFUser.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        } else {
            viewHolder.mTxtSOFUser.setText(mContext.getString(R.string.not_sof_user));
            viewHolder.mTxtSOFUser.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        }
        Picasso.get().load(model.getUser_image())
                .placeholder(R.drawable.ic_user)
                .into(viewHolder.mImgAvatar);
    }

    public void filterSofUser(boolean isFilter) {
        this.isFilterSofUser = isFilter;
        getFilter().filter("");
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                List<UserModel> mUserListFiltered;
                if (isFilterSofUser) {
                    List<UserModel> filteredList = new ArrayList<>();
                    for (UserModel row : mListDataOriginal) {
                        if (row.is_employee()) {
                            filteredList.add(row);
                        }
                    }
                    mUserListFiltered = filteredList;
                } else {
                    mUserListFiltered = mListDataOriginal;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mUserListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListData = (ArrayList<UserModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
