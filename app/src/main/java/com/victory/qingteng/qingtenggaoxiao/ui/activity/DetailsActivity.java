package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.StringListMessage;
import com.victory.qingteng.qingtenggaoxiao.MyApplication;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.presenter.DetailsPresenter;
import com.victory.qingteng.qingtenggaoxiao.ui.fragment.DetailsFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class DetailsActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener{

    //public static final String DETAILS_LIST_RECEIVE = "details_list_receive";
    public static final String DETAILS_TYPE_RECEIVE = "details_type_receive";

    @BindView(R.id.details_toolbar)
    Toolbar toolbar;

    private boolean isCollect;

    private MenuItem collectItem;

    private int type = DBHelper.TYPE_SCHOOL;

    private DetailsPresenter detailsPresenter;

    public static void open(Context context, int type){
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(DETAILS_TYPE_RECEIVE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    protected void init() {
        initToolbar();
        if(null == detailsPresenter){
            detailsPresenter = new DetailsPresenter(this);
        }
        detailsPresenter.setReady(true);
        detailsPresenter.getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        collectItem = menu.findItem(R.id.menu_details_collect);
        collectItem.setIcon(isCollect ? R.drawable.setting_collect : R.drawable.uncollect);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_details_collect:
                isCollect = !isCollect;
                invalidateOptionsMenu();
                return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        detailsPresenter.updateCollect(type, isCollect);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterEventBus(this);
        MyApplication.get().getRefWatcher(this).watch(this);
    }

    @Override
    protected void handleIntent(Intent intent){
        type = intent.getIntExtra(DETAILS_TYPE_RECEIVE, DBHelper.TYPE_SCHOOL);
    }

    private void initToolbar(){
        super.initToolbar(toolbar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getList(StringListMessage message){
        if(null == detailsPresenter){
            detailsPresenter = new DetailsPresenter(this);
        }
        detailsPresenter.setData(message.getList());
        detailsPresenter.getData();
    }

    @Override
    public void showData(Object data) {
        List<String> list = (List<String>) data;
        actionBar.setTitle(list.get(0));
        toolbar.setSubtitle(list.get(1) +" "+ list.get(2));
        isCollect = "1".equals(list.get(list.size() -1));
        Fragment fragment = DetailsFragment.getInstance(type);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.details_fragment_container, fragment)
                .commit();
    }
}
