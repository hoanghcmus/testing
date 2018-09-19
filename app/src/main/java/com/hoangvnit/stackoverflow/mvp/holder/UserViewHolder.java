package com.hoangvnit.stackoverflow.mvp.holder;

import android.view.View;
import android.widget.TextView;

import com.hoangvnit.stackoverflow.R;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserViewHolder extends BaseViewHolder {
    @BindView(R.id.userId)
    public TextView mTxUserId;

    @BindView(R.id.name)
    public TextView mTxtName;

    @BindView(R.id.age)
    public TextView mTxtAge;

    @BindView(R.id.location)
    public TextView mTxtLocation;

    @BindView(R.id.reputation)
    public TextView mTxtReputation;

    @BindView(R.id.sof)
    public TextView mTxtSOFUser;

    @BindView(R.id.avatar)
    public CircleImageView mImgAvatar;

    public UserViewHolder(View itemView) {
        super(itemView);
    }
}
