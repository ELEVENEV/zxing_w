package com.example.startpage;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.example.startpage.base.RxBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/7 0007.
 */

public class WeiboActivity extends RxBaseActivity {

    public static final String VIP_URL = "http://vip.bilibili.com/site/vip-faq-h5.html";
    public static final String VIP_URL1 = "http://vip.bilibili.com";

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;
    @BindView(R.id.webView_weibo)
    WebView mwebViewWeibo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_weibo;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mwebViewWeibo.loadUrl(VIP_URL);


    }

    @Override
    public void initToolBar() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
