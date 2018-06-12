package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.util.AsyncListUtil;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.presenter.UpdatePresenter;
import com.victory.qingteng.qingtenggaoxiao.utils.SharedPfUtils;
import com.victory.qingteng.qingtenggaoxiao.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import util.UpdateAppUtils;

public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener, Contracts.IView<List<String>>{

    public static final String apkName = "青藤高校";
    private static final String DOWNLOAD_ID = "downloadId";

    @BindView(R.id.update_details)
    TextView updateDetails;

    @BindView(R.id.update_agree)
    Button agreeBtn;

    @BindView(R.id.update_refuse)
    Button refuseBtn;

    private Unbinder unbinder;

    private UpdatePresenter presenter;

    private String url;

    private int versionCode;

    private FragmentActivity fatherActivity;

    public static UpdateDialogFragment getInstance(){
        return new UpdateDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fatherActivity = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && getActivity() != null && dialog.getWindow() != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new UpdatePresenter(getActivity(), this);
        initView();
        presenter.getData();
        return view;
    }

    private void initView(){
        agreeBtn.setOnClickListener(this);
        refuseBtn.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fatherActivity = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_agree:
                UpdateAppUtils.from(fatherActivity)
                        .serverVersionCode(versionCode)
                        .apkPath(url)
                        .update();
                break;
            case R.id.update_refuse:
                dismiss();
                break;
        }
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showData(List<String> data) {
        updateDetails.setText(data.get(1));
        url = data.get(2);
        versionCode = Integer.valueOf(data.get(0));
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showToast(msg);
    }

    @Override
    public void showFailure(String msg) {
        ToastUtils.showToast(msg);
    }
}
