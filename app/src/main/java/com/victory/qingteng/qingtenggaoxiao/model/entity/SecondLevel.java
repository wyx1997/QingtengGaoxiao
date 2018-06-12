package com.victory.qingteng.qingtenggaoxiao.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.MainRVAdapter;

public class SecondLevel implements MultiItemEntity{

    private String name;

    public SecondLevel(String name) {
        this.name = name;
    }

    @Override
    public int getItemType() {
        return MainRVAdapter.TYPE_SECOND_LEVEL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
