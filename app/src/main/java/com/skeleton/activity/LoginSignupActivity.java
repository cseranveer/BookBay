package com.skeleton.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;

import com.skeleton.R;
import com.skeleton.util.customview.CustomViewPager;

/**
 * LoginSignupActivity
 */
public class LoginSignupActivity extends BaseActivity {
    private CustomViewPager vpPager;
    private com.skeleton.adapter.PagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        init();
    }

    /**
     * initialization
     */
    private void init() {
        vpPager = (CustomViewPager) findViewById(R.id.vpPager);
        pagerAdapter = new com.skeleton.adapter.PagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(pagerAdapter);
        //tab Layout
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nest_scrollview);
        scrollView.setFillViewport(true);

    }

    public ViewPager getViewPager() {
      return vpPager;
    }
}
