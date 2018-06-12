package com.victory.qingteng.qingtenggaoxiao.model.eventbus.listener;

public interface OnToTopListener {

    void showDialog(boolean isShow, int position);

    void ensureToTop(boolean ensure);
}
