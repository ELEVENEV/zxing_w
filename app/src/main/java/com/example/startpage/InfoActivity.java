package com.example.startpage;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.startpage.base.RxBaseActivity;
import com.example.startpage.utils.SystemBarHelper;

import butterknife.BindView;

/**
 * Created by hcc on 2016/10/16 13:49
 * 100332338@qq.com
 * <p>
 * 大会员界面
 */

public class InfoActivity extends RxBaseActivity {

    public static final String LOAD_URL = "http://fusion.qq.com/cgi-bin/qzapps/unified_jump?appid=52473687&from=mqq&actionFlag=0&params=pname%3Dcn.eagleview.glasssync%26versioncode%3D12%26channelid%3D%26actionflag%3D0";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
//    @BindView(R.id.webView)
//    WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_info;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        final FloatingActionButton fab_load = (FloatingActionButton) findViewById(R.id.fab_load);
        fab_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(LOAD_URL));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });
//        mWebView.loadUrl(ConstantUtil.VIP_URL);
    }


    @Override
    public void initToolBar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle(getString(R.string.app_name));
        mTvVersion.setText("版本："+getVersion());
        //设置StatusBar透明
        SystemBarHelper.immersiveStatusBar(this);
        SystemBarHelper.setHeightAndPadding(this, mToolbar);
    }

    private String getVersion() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return getString(R.string.about_version);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
