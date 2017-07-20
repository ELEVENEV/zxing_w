package com.example.startpage;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.view.Window;
import android.widget.Toast;

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
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_test);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        StatusBarUtils.setColor(this,  getResources().getColor(R.color.myactivitytext_normal));
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
            public boolean onNavigationItemSelected(MenuItem item){
                    mDrawerLayout.closeDrawers();
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


    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar,menu);
        this.menu = menu;
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.app_updata:
                Toast.makeText(this,"点击更新",Toast.LENGTH_SHORT).show();
                loadAppInfo();
                Beta.checkUpgrade();
            default:
        }return super.onOptionsItemSelected(item);
    }

    public boolean onOptionsItemSelected(Menu menu){
        switch(item.getItemId()){
        case R.id.backup:
            Toast.makeText(this,"You click Backup",Toast.LENGTH_SHORT).show();
        break;
            case R.id.updata:
                Toast.makeText(this,"You click Updata",Toast.LENGTH_SHORT).show();
//                loadAppInfo();
                Beta.checkUpgrade();
                break;
            case R.id.delete:
                Toast.makeText(this,"You click Delete",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this,"You click Setting",Toast.LENGTH_SHORT).show();
                break;
        default:
        }
        return super.onOptionsItemSelected(item);
    }

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
}
