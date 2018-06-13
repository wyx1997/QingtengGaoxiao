package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.victory.qingteng.qingtenggaoxiao.Constants;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.presenter.ArticlePresenter;
import com.victory.qingteng.qingtenggaoxiao.ui.widget.LoadingProgressBar;

import butterknife.BindView;

public class ArticleActivity extends BaseActivity {

    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String TITLE = "title";

    @BindView(R.id.article_text)
    TextView textView;

    @BindView(R.id.article_toolbar)
    Toolbar toolbar;

    private ArticlePresenter articlePresenter;

    private String id;

    private String title;

    public static void open(Context context, int type,  String id, String title){
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(TYPE, type);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article;
    }

    @Override
    protected void handleIntent(Intent intent) {
        id = intent.getStringExtra(ID);
        articlePresenter = new ArticlePresenter(this, intent.getIntExtra(TYPE, Constants.TALK_ARTICLE));
        title = intent.getStringExtra(TITLE);
    }

    @Override
    protected void init() {
        initToolbar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.dividerColor));
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.white));
        }
        articlePresenter.getData(id);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showData(Object data) {
        if(null != textView){
            textView.setText((CharSequence) data);
        }
    }
}
