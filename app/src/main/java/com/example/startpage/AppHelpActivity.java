package com.example.startpage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.BundleCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;
import com.example.startpage.adapter.ExpandableListAdapter;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1 0001.
 */

public class AppHelpActivity extends AppCompatActivity{

    private static final int DOWNLOAD_RETRY = 1;
    private static final int DOWNLOAD_DEL = 2;
    private static final int DOWNLOAD_START = 3;
    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;
    private HashMap<String, List<String>> hashMap;
    private ArrayList<String> header;
    private int mId = -1;
    private int mGroupPosition;
    private int mChildPosition;
    private Toolbar toolbar;

    private boolean isGetting = false;
    private boolean isOpen = false;
    private static final int CAMERA_PERMISSIONS = 1;
    private static final int STORAGE_AND_CAMERA_PERMISSIONS = 2;
    private Handler handler = new Handler(Looper.getMainLooper());


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apphelp);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("App使用帮助");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        final Drawable upBack = getResources().getDrawable(R.drawable.icon_chevron_left);
        upBack.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upBack);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        expandableListView = (ExpandableListView) findViewById(R.id.simple_expandable_listview);
        setItems();//设置数据
        longClickItem();
        // 设置group条目的图片
        expandableListView.setGroupIndicator(null);
        //第二级条目中的子条目的点击监听，必须在ExpandableListAdapter中的isChildSelectable（）中返回true，否则child点击无效
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // 设置选中背景
                //setItemChecked(groupPosition, childPosition);
                List<String> strings = hashMap.get(header.get(groupPosition));
                Log.i("child", strings.get(childPosition));
//                Toast.makeText(AppHelpActivity.this, strings.get(childPosition), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //group条目点击事件，设置返回true后会child条目不会展开和收缩，配合全部展开，可以用来阻止条目收缩
        /*expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				Log.i("onGroupClick","onGroupClick");
				return true;
			}
		});*/
        //当条目折叠后的监听
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.i("onGroupCollapse", "onGroupCollapse");//折叠监听


            }
        });
        /*//条目展开后的监听
		expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				Log.i("onGroupExpand","onGroupExpand");//展开监听
				Toast.makeText(MainActivity.this,header.get(groupPosition),Toast.LENGTH_SHORT).show();
				}
		});*/

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        switch (id){
            case R.id.app_help1:
                isOpen = true;
                checkForOpenOrGet(true);
                break;

            default:
        }return super.onOptionsItemSelected(item);
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // TODO Auto-generated method stub
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    // 设置 headers 和 childs ExpandableListView
    void setItems() {
        //header的list数据集合
        header = new ArrayList<String>();

        //child items数据集合
        List<String> child1 = new ArrayList<String>();
        List<String> child2 = new ArrayList<String>();
        List<String> child3 = new ArrayList<String>();
        List<String> child4 = new ArrayList<String>();
        List<String> child5 = new ArrayList<String>();


        // Hashmap 存储 header 和 child对应集合
        hashMap = new HashMap<String, List<String>>();

        // 添加headers 数据到集合
//        for (int i = 0; i < 5; i++) {
        header.add(getString(R.string.header_child1));
//        }
        // 添加child数据到集合
//        for (int i = 0; i < 5; i++) {
        child1.add(getString(R.string.child1));
//        }
        header.add(getString(R.string.header_child2));
//        for (int i = 0; i < 7; i++) {
        child2.add(getString(R.string.child2));
//        }
        header.add(getString(R.string.header_child3));
//        for (int i = 0; i < 3; i++) {
        child3.add(getString(R.string.child3));
//        }
        header.add(getString(R.string.header_child4));
//        for (int i = 0; i < 8; i++) {
        child4.add(getString(R.string.child4));
//        }
        header.add(getString(R.string.header_child5));
//        for (int i = 0; i < 6; i++) {
        child5.add(getString(R.string.child5));
//        }


        // header和childs集合数据到hashmap
        hashMap.put(header.get(0), child1);
        hashMap.put(header.get(1), child2);
        hashMap.put(header.get(2), child3);
        hashMap.put(header.get(3), child4);
        hashMap.put(header.get(4), child5);


        adapter = new ExpandableListAdapter(AppHelpActivity.this, header, hashMap);
        // 给expandablelistview设置adapter
        expandableListView.setAdapter(adapter);

		/*1、首次加载全部展开：

		for (int i = 0; i < header.size(); i++) {
			expandableListView.expandGroup(i);//默认展开某个group也用这个方法
		}
		*/

        // 2.这里是控制只有一个group展开的效果
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.i("onGroupExpand", "onGroupExpand");//展开监听
//                Toast.makeText(AppHelpActivity.this, header.get(groupPosition), Toast.LENGTH_SHORT).show();
                int childCount = adapter.getChildrenCount(groupPosition);
                Log.i("childCount", childCount + "");
                //展开后取消所有条目选择状态，防止出现不必要的条目被选择
                for (int i = 0; i < childCount; i++) {
                    //i就是childposition
                    expandableListView.setItemChecked(groupPosition + i, false);
                }
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });

    }

    /*
    长按条目的处理方法
     */
    private void longClickItem() {
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickListIteam(view);
                return true;
            }
        });
    }
    //长按处理方法
    private void longClickListIteam(View view) {
        final int groupPos = (Integer) view.getTag(R.id.child);
        final int childPos = (Integer) view.getTag(R.id.ll_child);
        Log.i("yu", "groupPos:" + groupPos + ",childPos:" + childPos);
        if (childPos == -1) {         //如果得到child位置的值为-1，则是操作group
            Log.i("yu", "操作group组件");
        } else {
            Log.i("yu", "操作child组件");     //否则，操作child
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
                                    Toast toast = Toast.makeText(AppHelpActivity.this, "未读数：" + unreadCount,
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
