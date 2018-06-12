package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.SearchActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.MainRVAdapter;
import com.victory.qingteng.qingtenggaoxiao.model.entity.FirstLevel;
import com.victory.qingteng.qingtenggaoxiao.model.entity.SecondLevel;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.listener.OnToTopListener;
import com.victory.qingteng.qingtenggaoxiao.utils.SharedPfUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class TabFragment extends BaseFragment {

    public static final String TAG = "TabFragment";

    public static final String FRAGMENT_TYPE = "fragment_type";
    public static final int FRAGMENT_PROVINCE = 0;
    public static final int FRAGMENT_LEVEL = 1;
    public static final String LIST_KEY = "list_key";
    public static final String ID_KEY = "id_key";

    private static final int[] schoolResIds = new int[]{
            R.array.anhui, R.array.aomen, R.array.beijing, R.array.chongqing, R.array.fujian, R.array.gansu,
            R.array.guizhou, R.array.guangdong, R.array.guangxi, R.array.hainan,R.array.heilongjiang,
            R.array.hebei, R.array.henan, R.array.hubei, R.array.hunam, R.array.jiangxi, R.array.jiangsu, R.array.jilin, R.array.liaoning,
            R.array.neimeng, R.array.ningxia, R.array.qinghai, R.array.shanghai, R.array.shandong,
            R.array.shanxi, R.array.shannxi, R.array.sichuan, R.array.taiwan, R.array.tianjin, R.array.xianggang,
            R.array.xizang, R.array.xinjiang, R.array.yunnan, R.array.zhejiang
    };

    private static final int[] majorResIds = new int[]{
            R.array.benke_major, R.array.zhuanke_major
    };

    @BindView(R.id.fragment_tab_container)
    FrameLayout frameLayout;

    @BindView(R.id.fragment_recycler_view)
    RecyclerView recyclerView;

    @BindArray(R.array.level)
    String[] levels;

    private MainRVAdapter adapter;

    private List<MultiItemEntity> list = new ArrayList<>();

    private String[] proNames;

    private int[] ids;

    private int type = FRAGMENT_PROVINCE;

    private View headerView;

    private EnsureToTopDialogFragment ensureToTopDialogFragment;

    public static TabFragment getInstance(int type) {
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(Bundle savedInstanceState, ViewGroup container) {
        ensureToTopDialogFragment = EnsureToTopDialogFragment.getInstance();
        ensureToTopDialogFragment.setToTopListener(toTopListener);
        headerView = fatherActivity.getLayoutInflater().inflate(R.layout.main_search_header, container, false);
        initRV();
    }

    @Override
    protected void handleArguments(Bundle arguments) {
        type = arguments.getInt(FRAGMENT_TYPE);
    }

    private void initRV(){
        list.clear();
        setData();
        adapter = new MainRVAdapter(list, fatherActivity, type, proNames);
        adapter.addHeaderView(headerView);
        adapter.setToTopListener(toTopListener);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                if(adapter.getItem(position).getItemType() == MainRVAdapter.TYPE_FIRST_LEVEL){
                    return type == DBHelper.TYPE_SCHOOL ? 4 : 3;
                } else {
                    return 1;
                }
            }
        });
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.open(fatherActivity, type);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(fatherActivity, type == DBHelper.TYPE_SCHOOL ? 4 : 3));
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("CheckResult")
    private void setData(){
        proNames = SharedPfUtils.getMainListFromSPF(fatherActivity, LIST_KEY, schoolResIds.length);
        if(proNames[0] == null){
            proNames = getResources().getStringArray(R.array.province);
        }
        ids = SharedPfUtils.getMainArrayFromSPF(fatherActivity, ID_KEY, schoolResIds.length);
        if(ids[0] == -1){
            ids = schoolResIds;
        }
        switch (type){
            case FRAGMENT_PROVINCE:
                setProvinceData();
                break;
            case FRAGMENT_LEVEL:
                setLevelData();
                break;
        }
    }

    private void setProvinceData(){
        for(int i=0; i<proNames.length; ++i){
            FirstLevel province = new FirstLevel(proNames[i], 0);
            String[] cities = getResources().getStringArray(ids[i]);
            for(String item : cities){
                SecondLevel city = new SecondLevel(item);
                province.addSubItem(city);
            }
            list.add(province);
        }
    }

    private void setLevelData(){
        for(int i=0; i<levels.length; i++){
            FirstLevel level = new FirstLevel(levels[i], 0);
            String[] majors = getResources().getStringArray(majorResIds[i]);
            for(String item : majors){
                SecondLevel major = new SecondLevel(item);
                level.addSubItem(major);
            }
            list.add(level);
        }
    }

    private OnToTopListener toTopListener = new OnToTopListener() {

        private int position = -1;

        @Override
        public void showDialog(boolean isShow, int position) {
            if(isShow){
                ensureToTopDialogFragment.show(fatherActivity.getSupportFragmentManager(), null);
            }
            this.position = position;
        }

        @Override
        public void ensureToTop(boolean ensure) {
            if(ensure && position != -1){
                position = position - 1;
                adapter.setHasTop(true);
                FirstLevel entity = (FirstLevel) list.get(position);
                list.set(position, list.get(0));
                list.set(0, entity);
                adapter.notifyItemRangeChanged(1, position+1);
                int id = ids[position];
                ids[position] = ids[0];
                ids[0] = id;
                String name = proNames[position];
                proNames[position] = proNames[0];
                proNames[0] = name;
                SharedPfUtils.saveMainData2SPF(fatherActivity, LIST_KEY, proNames, ID_KEY, ids).subscribe();
            }
        }
    };

    @Override
    public void showData(Object data) {

    }
}
