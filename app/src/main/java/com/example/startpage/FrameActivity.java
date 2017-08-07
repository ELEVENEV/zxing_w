package com.example.startpage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;
import com.example.startpage.myfragment.FruitAdapter;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class FrameActivity extends AppCompatActivity{


    private MenuItem item;

    private DrawerLayout mDrawerLayout;

    private boolean isGetting = false;
    private boolean isOpen = false;

    private static final int CAMERA_PERMISSIONS = 1;
    private static final int STORAGE_AND_CAMERA_PERMISSIONS = 2;
    private Handler handler = new Handler(Looper.getMainLooper());



    private Fruit[]fruits = {new Fruit("Apple",R.drawable.apple),new Fruit("Banana",R.drawable.banana),
            new Fruit("Orange",R.drawable.orange), new Fruit("Watermelon", R.drawable.watermelon),
            new Fruit("Pear", R.drawable.pear), new Fruit("Grape", R.drawable.grape),
            new Fruit("Pineapple", R.drawable.pineapple), new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Cherry", R.drawable.cherry), new Fruit("Mango", R.drawable.mango)};


    private List<Fruit> fruitList = new ArrayList<>();

    private FruitAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private Menu menu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_test);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        StatusBarUtils.setColor(this,  getResources().getColor(R.color.colorPrimary));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAppInfo();
                Beta.checkUpgrade();
//                Snackbar.make(view,"Date delete",Snackbar.LENGTH_SHORT)
//                        .setAction("Undo",new View.OnClickListener(){
//                    public void onClick(View v){
//                        loadAppInfo();
//                        Beta.checkUpgrade();
//                        Toast.makeText(FrameActivity.this,"Date restored",Toast.LENGTH_SHORT).show();
//
//                    }
//                }).show();
            }
        });

        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            public boolean onNavigationItemSelected( MenuItem item){
                    mDrawerLayout.closeDrawers();
                switch(item.getItemId()){
                case R.id.feed_back:
                    isOpen = true;
                    checkForOpenOrGet(true);
                break;
                default:
                break;
                }
                return true;
            }

        });

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });
    }

    private void refreshFruits(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initFruits() {
        fruitList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){

            case R.id.id_cation_info:
                startActivity(new Intent(FrameActivity.this,InfoActivity.class));
                break;


            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.delete:
//                Toast.makeText(this,"点击更新",Toast.LENGTH_SHORT).show();
                loadAppInfo();
                Beta.checkUpgrade();
                break;
            case R.id.app_help:
                startActivity(new Intent(FrameActivity.this,AppHelpActivity.class));
                break;

            default:
        }return super.onOptionsItemSelected(item);
    }

//    public boolean onOptionsItemSelected(Menu menu){
//        switch(item.getItemId()){
//        case R.id.backup:
//            Toast.makeText(this,"You click Backup",Toast.LENGTH_SHORT).show();
//        break;
//            case R.id.id_cation_info:
//                Toast.makeText(this,"You click Updata",Toast.LENGTH_SHORT).show();
////                loadAppInfo();
//                Beta.checkUpgrade();
//                break;
//            case R.id.delete:
//                Toast.makeText(this,"You click Delete",Toast.LENGTH_SHORT).show();
//                break;
//        default:
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                setResult(1);
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void loadAppInfo() {
        try {
            StringBuilder info = new StringBuilder();
            PackageInfo packageInfo =
                    this.getPackageManager().getPackageInfo(this.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            info.append("appid: ").append(MyApplicationLike.APP_ID).append(" ")
                    .append("channel: ").append(MyApplicationLike.APP_CHANNEL).append(" ")
                    .append("version: ").append(versionName).append(".").append(versionCode);
//            appInfoTv.setText(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void checkForOpenOrGet(boolean isOpenFeedback) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}
                    , STORAGE_AND_CAMERA_PERMISSIONS);
        } else {
            openOrGet(isOpenFeedback);
        }
    }

    /**
     * @param isOpenFeedback 打开网页or获取未读数
     */
    private void openOrGet(final boolean isOpenFeedback) {
        //接入方不需要这样调用, 因为扫码预览, 同时为了服务器发布后能做到实时预览效果, 所有每次都init.
        //业务方默认只需要init一次, 然后直接openFeedbackActivity/getFeedbackUnreadCount即可
        FeedbackAPI.init(this.getApplication(), MyApplicationLike.DEFAULT_APPKEY, MyApplicationLike.DEFAULT_APPSECRET);
        final Activity context = this;
        //如果500ms内init未完成, openFeedbackActivity会失败, 可以延长时间>500ms
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOpenFeedback) {
                    FeedbackAPI.openFeedbackActivity();
                } else {
                    FeedbackAPI.getFeedbackUnreadCount(new IUnreadCountCallback() {
                        @Override
                        public void onSuccess(final int unreadCount) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(FrameActivity.this, "未读数：" + unreadCount,
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                    isGetting = false;
                                }
                            });
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }
                isGetting = false;
            }
        }, 500);
    }
}
