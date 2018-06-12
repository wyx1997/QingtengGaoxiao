package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.victory.qingteng.qingtenggaoxiao.MyApplication;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.presenter.SearchPresenter;
import com.victory.qingteng.qingtenggaoxiao.ui.fragment.CursorFragment;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;

public class SearchActivity extends BaseActivity {

    public static final String TAG = "MainActivityLog";
    private static final String TYPE = "type";

    @BindView(R.id.search_toolbar)
    Toolbar toolbar;

    @BindString(R.string.area_search_hint)
    String schoolHint;

    @BindString(R.string.major_search_hint)
    String majorHint;

    private SearchView searchView;

    private CursorFragment cursorFragment;

    private int currType = DBHelper.TYPE_SCHOOL;

    private SearchPresenter searchPresenter;

    public static void open(Context context, int type){
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void init() {
        initToolbar();
        cursorFragment = CursorFragment.getInstance(currType);
        searchPresenter = new SearchPresenter(cursorFragment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cursor_fragment_container, cursorFragment)
                .commit();
    }

    @Override
    protected void handleIntent(Intent intent) {
        currType = intent.getIntExtra(TYPE, DBHelper.TYPE_SCHOOL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        initSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.get().getRefWatcher(this).watch(this);
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
    }

    private void initSearchView(final SearchView searchView){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("CheckResult")
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQuery("", false);
                searchPresenter.queryDetails(SearchActivity.this, currType, query);
                return true;
            }

            @SuppressLint("CheckResult")
            @Override
            public boolean onQueryTextChange(String newText) {
                if(searchView.getQuery().length() != 0){
                    searchPresenter.searchName(currType, newText);
                }
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    searchView.onActionViewCollapsed();
                }
            }
        });
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setQueryHint(currType == DBHelper.TYPE_SCHOOL ? schoolHint : majorHint);
    }

    @Override
    public void showData(Object data) {

    }
}
