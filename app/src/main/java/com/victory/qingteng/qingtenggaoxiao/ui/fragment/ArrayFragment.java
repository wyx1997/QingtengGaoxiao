package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.victory.qingteng.qingtenggaoxiao.Constants;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.ArrayFragmentMessage;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.StringListMessage;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.model.model.QqgpModel;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.ArticleActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.BaikeDetailsActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.DetailsActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.ExamYuanDetailsActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.activity.QqgpActivity;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.SimpleRVAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ArrayFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener{

    private static final String TAG = "ArrayFragment";

    private static final String ARGS_LIST = "args_list";
    private static final String TYPE = "type";

    @BindView(R.id.array_rv)
    RecyclerView recyclerView;

    private SimpleRVAdapter adapter;

    private List<String> data;

    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus(this);
    }

    public static ArrayFragment getInstance(){
        return new ArrayFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_array;
    }

    @Override
    protected void initView(Bundle savedInstanceState, ViewGroup container) {
        Log.d(TAG, "initView");
        adapter = new SimpleRVAdapter(data);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(Object data) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getList(ArrayFragmentMessage message){
        data = message.getData();
        type = message.getType();
        if(null != adapter){
            adapter.setNewData(data);
            adapter.setOnItemClickListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterEventBus(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (type){
            case Constants.CITY_SCHOOL:
            case Constants.MAJOR:
                int type1 = type == Constants.CITY_SCHOOL ? DBHelper.TYPE_SCHOOL : DBHelper.TYPE_MAJOR;
                DBHelper.getInstance()
                        .queryValueByArgs(type1,null, "name=?", new String[]{(String) adapter.getItem(position)})
                        .subscribe(new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> list) throws Exception {
                                if(list.size() != 0){
                                    EventBus.getDefault().postSticky(new StringListMessage(list));
                                }
                            }
                        });
                DetailsActivity.open(fatherActivity, type1);
                break;
            case Constants.TIANBAO_ENTRY:
                ExamYuanDetailsActivity.open(fatherActivity, position);
                break;
            case Constants.BAOKAO_GP:
                position = position+1;
                String id = position<10 ? "0"+position : String.valueOf(position);
                QqgpActivity.open(fatherActivity, QqgpModel.EXAM, id);
                break;
            case Constants.BAIKE:
                position = position+1;
                BaikeDetailsActivity.open(fatherActivity, "20"+position, (String) adapter.getItem(position-1));
        }
    }
}
