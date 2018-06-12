package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.victory.qingteng.qingtenggaoxiao.MyApplication;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.utils.ExcelUtils;
import com.victory.qingteng.qingtenggaoxiao.utils.SharedPfUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

import static com.victory.qingteng.qingtenggaoxiao.MyApplication.KEY_INIT;

public class StartActivity extends BaseActivity {

    @BindView(R.id.start_progress)
    ProgressBar progressBar;

    private int max;

    private int progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((boolean) SharedPfUtils.getFromSPF(this, KEY_INIT, false)){
            MainActivity.open(this);
            finish();
        }
        registerEventBus(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void init() {
        progressBar.setVisibility(View.VISIBLE);
        max = progressBar.getMax();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterEventBus(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setProgress(Object progress){
        this.progress = (Integer) progress;
        progressBar.setProgress(this.progress);
        if(this.progress == max){
            MainActivity.open(this);
            finish();
        }
    }

    @Override
    public void showData(Object data) {

    }
}
