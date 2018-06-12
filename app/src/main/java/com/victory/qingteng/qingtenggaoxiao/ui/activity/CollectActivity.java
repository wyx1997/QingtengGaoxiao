package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.presenter.CollectPresenter;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.CollectRVAdapter;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.listener.OnDeleteEnsureListener;
import com.victory.qingteng.qingtenggaoxiao.ui.fragment.CollectDeleteDialogFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.Iterator;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import io.reactivex.functions.Action;

public class CollectActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener{

    public static final String TAG = "CollectActivity";

    //0-普通状态，显示编辑，1-正在编辑，显示全选
    private static final int NORMAL = 0;
    private static final int EDITING = 1;

    @BindView(R.id.collect_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.collect_toolbar)
    Toolbar toolbar;

    @BindView(R.id.collect_floating_bn)
    FloatingActionButton fab;

    @BindString(R.string.collect_school_num)
    String schoolNum;

    @BindString(R.string.collect_major_num)
    String majorNum;

    private CollectPresenter collectPresenter;

    private CollectRVAdapter adapter;

    private MenuItem allChooseItem;

    private MenuItem editItem;

    private int currState = NORMAL;

    private boolean isAllChoosing = false;

    private int type = DBHelper.TYPE_SCHOOL;

    private List<String> toDeleteItems;

    private CollectDeleteDialogFragment deleteDialogFragment;

    private int dataSize;

    public static void open(Context context){
        Intent intent = new Intent(context, CollectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    protected void init() {
        collectPresenter = new CollectPresenter(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToNormal(dataSize);
                if(type == DBHelper.TYPE_SCHOOL){
                    type = DBHelper.TYPE_MAJOR;
                    fab.setImageResource(R.drawable.major);
                } else {
                    type = DBHelper.TYPE_SCHOOL;
                    fab.setImageResource(R.drawable.school);
                }
                adapter.setType(type);
                collectPresenter.query4Collect(type);
            }
        });
        initToolbar();
        initRV();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        allChooseItem = menu.findItem(R.id.collect_all_choose);
        editItem = menu.findItem(R.id.collect_edit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.collect_edit:
                handleEditClick(item);
                return true;
            case R.id.collect_all_choose:
                handleChooseClick(item);
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(hasRVItemChecked(dataSize)){
                backToNormal(dataSize);
            } else {
                switch (currState){
                    case NORMAL:
                        finish();
                        break;
                    default:
                        backToNormal(dataSize);
                        break;
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe()

    private void initToolbar(){
        super.initToolbar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        actionBar.setTitle(R.string.collect_title);
        toolbar.setSubtitle(R.string.school);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void initRV(){
        adapter = new CollectRVAdapter(CollectActivity.this, null);
        TextView textView = new TextView(CollectActivity.this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText(R.string.collect_no_data);
        adapter.setEmptyView(textView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CollectActivity.this));
        recyclerView.setAdapter(adapter);
        collectPresenter.query4Collect(type);
    }

    private void handleChooseClick(MenuItem item){
        item.setTitle(isAllChoosing ? R.string.collect_all_choose : R.string.collect_all_choose_cancel);
        isAllChoosing = !isAllChoosing;
        setRvItemChecked(isAllChoosing, dataSize);
    }

    private void handleEditClick(final MenuItem item){
        if(dataSize == 0) return;
        switch (currState){
            case NORMAL: { //点击后变成删除，同时所有checkBox显现出来，全选菜单显现
                item.setTitle(R.string.collect_all_delete);
                setRvItemCheckBoxVisible(View.VISIBLE, dataSize);
                setRvItemChecked(false, dataSize);
                allChooseItem.setVisible(true);
                currState = EDITING;
                break;
            }
            case EDITING: { //点击后变成编辑，删除选中项
                toDeleteItems = adapter.getToDeleteItems();
                if (null != toDeleteItems && toDeleteItems.size() > 0) {
                    if(null == deleteDialogFragment){
                        deleteDialogFragment = CollectDeleteDialogFragment.getInstance();
                        deleteDialogFragment.setDeleteEnsureListener(deleteEnsureListener);
                    }
                    deleteDialogFragment.show(getSupportFragmentManager(), null);
                }
                break;
            }
        }
    }

    private OnDeleteEnsureListener deleteEnsureListener = new OnDeleteEnsureListener() {

        @Override
        public void ensureDelete(boolean ensure) {
            if(dataSize == 0) return;
            if(ensure){
                editItem.setTitle(R.string.collect_edit);
                allChooseItem.setVisible(false);
                isAllChoosing = false;
                currState = NORMAL;
                invalidateOptionsMenu();
                setRvItemCheckBoxVisible(View.GONE, dataSize);
                collectPresenter.updateCollect(toDeleteItems, type);
            } else {
                backToNormal(dataSize);
            }

        }
    };

    private void backToNormal(int size){
        editItem.setTitle(R.string.collect_edit);
        allChooseItem.setVisible(false);
        isAllChoosing = false;
        currState = NORMAL;
        invalidateOptionsMenu();
        if(size == 0) return;
        setRvItemChecked(false, size);
        setRvItemCheckBoxVisible(View.GONE, size);
    }

    private void setRvItemChecked(boolean isChecked, int size){
        for (int i = 0; i < size; i++) {
            ((CheckBox)recyclerView.getChildAt(i).findViewById(R.id.collect_check_box)).setChecked(isChecked);
        }
    }

    private void setRvItemCheckBoxVisible(int state, int size){
        for (int i = 0; i < size; i++) {
            recyclerView.getChildAt(i).findViewById(R.id.collect_check_box).setVisibility(state);
        }
    }

    public boolean hasRVItemChecked(int size){
        boolean flag = false;
        for(int i=0; i<size; i++){
            flag = ((CheckBox)recyclerView.getChildAt(i).findViewById(R.id.collect_check_box)).isChecked();
            if(flag) break;
        }
        return flag;
    }

    public int getType() {
        return type;
    }

    @Override
    public void showData(Object data) {
        List<String> list = (List<String>) data;
        if(null != toDeleteItems) toDeleteItems.clear();
        dataSize = list.size();
        adapter.setNewData(list);
        toolbar.setSubtitle(String.format(type == DBHelper.TYPE_SCHOOL ? schoolNum : majorNum, dataSize));
    }
}
