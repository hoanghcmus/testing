package com.hoangvnit.stackoverflow.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hoangvnit.stackoverflow.R;
import com.hoangvnit.stackoverflow.base.BaseActivity;
import com.hoangvnit.stackoverflow.base.BaseFragment;
import com.hoangvnit.stackoverflow.common.FRAGMENT_ID;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseFragment goldPriceListFragment = getFragment(FRAGMENT_ID.USER_LIST_FRAGMENT);
        attachFragment(goldPriceListFragment, getContainerID(), FRAGMENT_ID.USER_LIST_FRAGMENT.getKey());
    }
}
