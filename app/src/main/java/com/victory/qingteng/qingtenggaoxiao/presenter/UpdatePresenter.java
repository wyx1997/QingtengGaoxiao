package com.victory.qingteng.qingtenggaoxiao.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.model.VersionModel;
import com.victory.qingteng.qingtenggaoxiao.utils.ToastUtils;

import java.util.List;

public class UpdatePresenter extends BasePresenter {

    private VersionModel model;

    private Context context;

    public UpdatePresenter(Context context, Contracts.IView view) {
        super(view);
        model = new VersionModel();
        this.context = context;
    }

    public void getData(){
        if(!isAttachView()){
            return;
        }
        view.showLoading();
        model.getFromNetwork(null, new Contracts.ICallback<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                try {
                    view.hideLoading();
                    PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    if(info.versionCode >= Integer.valueOf(data.get(0))){
                        ToastUtils.showToast(context.getResources().getString(R.string.no_update_version));
                    } else {
                        view.showData(data);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    ToastUtils.showToast(context.getResources().getString(R.string.update_failure));
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {

            }

            @Override
            public void onFailure(String failMsg) {
                view.hideLoading();
                view.showError(failMsg);
            }
        });
    }
}
