package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.StringListMessage;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;
import com.victory.qingteng.qingtenggaoxiao.presenter.DetailsFragmentPresenter;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.DetailsRVAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class DetailsFragment extends BaseFragment {

    private static final String ARGS_DETAILS_LIST = "args_details_list";
    private static final String TYPE = "type";

    @BindView(R.id.details_img)
    ImageView imageView;

    @BindView(R.id.details_text)
    ListView listView;

    @BindView(R.id.details_recycler_view)
    RecyclerView recyclerView;

    private DetailsRVAdapter adapter;

    private int type = DBHelper.TYPE_SCHOOL;

    private DetailsFragmentPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus(this);
    }

    public static DetailsFragment getInstance(int type) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState, ViewGroup container) {
        presenter.setReady(true);
        presenter.getData();
    }

    @Override
    protected void handleArguments(Bundle arguments) {
        type = arguments.getInt(TYPE);
        if(null == presenter){
            presenter = new DetailsFragmentPresenter(this);
        } else {
            presenter.handleList(type);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterEventBus(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        unRegisterEventBus(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getList(StringListMessage message) {
        removeStickyEvent(message);
        if(null == presenter){
            presenter = new DetailsFragmentPresenter(this);
        }
        presenter.setDetailsData(message.getList());
        presenter.handleList(type);
        presenter.getData();
    }

    private void initRV(List<String> linkData, String name, List<Integer> originPos) {
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        adapter = new DetailsRVAdapter(fatherActivity, linkData, name, type, originPos);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(Object data) {
        List<List<String>> list = (List<List<String>>) data;
        List<String> rvList = list.get(0);
        List<Integer> originPos = handleEmptyData(rvList);
        if(list.size() == 1){
            imageView.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            initRV(rvList, null, originPos);
            return;
        }
        listView.setDivider(null);
        listView.setAdapter(new ArrayAdapter<>(fatherActivity, R.layout.item_details_text, list.get(1).subList(3, 8)));
        if (list.get(1).get(2) != null && list.get(1).get(2).length() != 0) {
            Glide.with(this)
                    .load("http://xyxtec.com/校徽/" + list.get(1).get(1) + "/" + list.get(1).get(2) + ".jpg")
                    .into(imageView);
        }
        initRV(rvList, list.get(1).get(0), originPos);
    }

    private List<Integer> handleEmptyData(List<String> data){
        List<Integer> posList = new ArrayList<>(data.size());
        int currPos = 0;
        Iterator<String> iterator = data.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if(element == null || element.equals("")){
                iterator.remove();
            } else {
                posList.add(currPos);
            }
            currPos++;
        }
        return posList;
    }
}









