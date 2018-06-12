package com.victory.qingteng.qingtenggaoxiao.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.victory.qingteng.qingtenggaoxiao.R;
import com.victory.qingteng.qingtenggaoxiao.model.eventbus.listener.OnDeleteEnsureListener;

public class CollectDeleteDialogFragment extends DialogFragment {

    private OnDeleteEnsureListener deleteEnsureListener;

    public static CollectDeleteDialogFragment getInstance(){
        return new CollectDeleteDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.collect_all_delete_msg)
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEnsureListener.ensureDelete(true);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEnsureListener.ensureDelete(false);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        deleteEnsureListener.ensureDelete(false);
                    }
                })
                .create();
    }

    public void setDeleteEnsureListener(OnDeleteEnsureListener deleteEnsureListener) {
        this.deleteEnsureListener = deleteEnsureListener;
    }
}
