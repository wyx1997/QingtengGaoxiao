package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.listener.OnToTopListener;

public class EnsureToTopDialogFragment extends DialogFragment {

    private OnToTopListener toTopListener;

    public static EnsureToTopDialogFragment getInstance(){
        return new EnsureToTopDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.main_rv_to_top)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toTopListener.ensureToTop(true);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toTopListener.ensureToTop(false);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        toTopListener.ensureToTop(false);
                    }
                })
                .create();
    }

    public void setToTopListener(OnToTopListener toTopListener) {
        this.toTopListener = toTopListener;
    }
}
