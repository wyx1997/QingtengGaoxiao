package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.utils.ToastUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;

public class AboutUsActivity extends BaseActivity{

    @BindView(R.id.about_us_toolbar)
    Toolbar toolbar;

    @BindView(R.id.about_us_function)
    ListView listView;

    @BindArray(R.array.about_us_details)
    String[] details;

    @BindArray(R.array.about_us_link)
    String[] titleLinks;

    public static void open(Context context){
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void init() {
        initToolbar(toolbar);
        listView.setDivider(null);
        listView.setClickable(true);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_about_us_lv, details));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                    case 1:
                        WebViewActivity.open(AboutUsActivity.this, titleLinks[position]);
                        break;
                    case 2:
                        ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        manager.setText(titleLinks[position]);
                        ToastUtils.showToast(R.string.qqgp_number_to_clipboard);
                        break;
                }
            }
        });
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        actionBar.setTitle(R.string.about_us);
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

    }
}
