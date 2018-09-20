package com.hoangvnit.stackoverflow.mvp.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoangvnit.stackoverflow.R;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class UserReputationViewHolder extends BaseViewHolder {
    @BindView(R.id.reputation_type)
    public TextView mTxtReputationType;

    @BindView(R.id.reputation_change)
    public TextView mTxtReputationChange;

    @BindView(R.id.post_ids)
    public TextView mTxtPostId;

    @BindView(R.id.create_date)
    public TextView mTxtCreateDate;

    @BindView(R.id.img_reputation_type)
    public ImageView mImgReputationType;

    public UserReputationViewHolder(View itemView) {
        super(itemView);
    }
}
