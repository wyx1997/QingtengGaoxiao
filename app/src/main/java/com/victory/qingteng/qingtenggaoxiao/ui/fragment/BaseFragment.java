package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.message.EventBusMessage;
import com.victory.qingteng.qingtenggaoxiao.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements Contracts.IView{

    protected Unbinder unbinder;

    protected AppCompatActivity fatherActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fatherActivity = (AppCompatActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != savedInstanceState){
            handleSavedInstanceState(savedInstanceState);
        }
        if(null != getArguments()){
            handleArguments(getArguments());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(savedInstanceState, container);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != unbinder){
            unbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fatherActivity = null;
    }

    protected void registerEventBus(BaseFragment fragment){
        EventBus.getDefault().register(fragment);
    }

    protected void removeStickyEvent(EventBusMessage event){
        EventBus.getDefault().removeStickyEvent(event);
    }

    protected void unRegisterEventBus(BaseFragment fragment){
        if(EventBus.getDefault().isRegistered(fragment)){
            EventBus.getDefault().unregister(fragment);
        }
    }

    protected void removeAllEvent(){
        EventBus.getDefault().removeAllStickyEvents();
    }

    protected void handleArguments(Bundle arguments){}

    protected void handleSavedInstanceState(Bundle savedInstanceSate){}

    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState, ViewGroup container);

    @Override
    public void showError(String msg) {
        showToast(msg);
    }

    @Override
    public void showFailure(String msg) {
        showToast(msg);
    }

    private void showToast(String msg){
        ToastUtils.showToast(msg);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
