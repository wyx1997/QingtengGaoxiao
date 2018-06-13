package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.ArrayFragmentMessage;
import com.victory.qingteng.qingtenggaoxiao.ui.fragment.ArrayFragment;
import com.victory.qingteng.qingtenggaoxiao.ui.fragment.MainFragment;
import com.victory.qingteng.qingtenggaoxiao.ui.fragment.SettingFragment;
import com.victory.qingteng.qingtenggaoxiao.ui.fragment.TalkFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    private static final String FRAGMENT1 = "fragment1";
    private static final String FRAGMENT2 = "fragment2";
    private static final String FRAGMENT3 = "fragment3";

    @BindView(R.id.bottom_navigation_bar_container)
    BottomNavigationBar navigationBar;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_toolbar_title)
    TextView title;

    private FragmentManager fm = getSupportFragmentManager();

    private MainFragment mainFragment;

    private SettingFragment settingFragment;

    private TalkFragment talkFragment;

    private ArrayFragment arrayFragment;

    private int currPos = 1;

    private BottomNavigationBar.OnTabSelectedListener onTabSelectedListener =
            new BottomNavigationBar.OnTabSelectedListener() {
                @Override
                public void onTabSelected(int position) {
                    currPos = position;
                    toolbarReset();
                    FragmentTransaction transaction = fm.beginTransaction();
                    hideAllFragment(transaction);
                    switch (position){
                        case 0:{
                            title.setText(R.string.main_toolbar_talk_title);
                            if(null == talkFragment) {
                                talkFragment = TalkFragment.getInstance();
                                transaction.add(R.id.main_fragment_container, talkFragment, FRAGMENT1);
                            } else {
                                transaction.show(talkFragment);
                            }
                        }
                        break;
                        case 1:{
                            title.setText(R.string.main_toolbar_school_title);
                            if(null == mainFragment){
                                mainFragment = MainFragment.getInstance();
                                transaction.add(R.id.main_fragment_container, mainFragment, FRAGMENT2);
                            } else {
                                transaction.show(mainFragment);
                            }

                        }
                        break;
                        case 2:{
                            title.setText(R.string.main_toolbar_setting_title);
                            if(null == settingFragment) {
                                settingFragment = SettingFragment.getInstance();
                                transaction.add(R.id.main_fragment_container, settingFragment, FRAGMENT3);
                            } else {
                                transaction.show(settingFragment);
                            }
                        }
                    }
                    transaction.commit();
                }

                @Override
                public void onTabUnselected(int position) {
                }

                @Override
                public void onTabReselected(int position) {

                }
            };

    public static void open(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initToolbar(toolbar);
        initNavi();
        if(null == mainFragment){
            mainFragment = MainFragment.getInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, mainFragment, FRAGMENT2)
                .commit();
        title.setText(R.string.main_toolbar_school_title);
    }

    @Override
    public void onBackPressed() {
        toolbarReset();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterEventBus(this);
    }

    private void initNavi(){
        navigationBar.addItem(new BottomNavigationItem(R.drawable.left, R.string.main_navi_talk_title))
                .addItem(new BottomNavigationItem(R.drawable.home, R.string.main_navi_school_title))
                .addItem(new BottomNavigationItem(R.drawable.setting, R.string.main_navi_setting_title))
                .setFirstSelectedPosition(1)
                .initialise();
        navigationBar.setMode(BottomNavigationBar.MODE_CLASSIC);
        navigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        navigationBar.setTabSelectedListener(onTabSelectedListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        removeAllEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void addArrayFragment(ArrayFragmentMessage message){
        arrayFragment = ArrayFragment.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        transaction.add(R.id.main_fragment_container, arrayFragment).addToBackStack(null).commit();
        handleMegToolbar(message.getTitle(), message.getSubTitle());
    }

    private void hideFragment(FragmentTransaction transaction){
        switch (currPos){
            case 0: transaction.hide(talkFragment); break;
            case 1: transaction.hide(mainFragment); break;
            case 2: transaction.hide(settingFragment);
        }
    }

    private void hideAllFragment(FragmentTransaction transaction){
        if(null != talkFragment) transaction.hide(talkFragment);
        if(null != mainFragment) transaction.hide(mainFragment);
        if(null != settingFragment) transaction.hide(settingFragment);
        if(null != arrayFragment) transaction.hide(arrayFragment);
    }

    private void toolbarReset(){
        title.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    private void handleMegToolbar(String mainTitle, String subTitle){
        actionBar.setDisplayHomeAsUpEnabled(true);
        title.setVisibility(View.GONE);
        toolbar.setTitle(mainTitle);
        toolbar.setSubtitle(subTitle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStack();
                    toolbarReset();
                }
            }
        });
    }

    @Override
    public void showData(Object data) {

    }
}
