package com.victory.qingteng.qingtenggaoxiao.model.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.victory.qingteng.qingtenggaoxiao.ui.adapter.MainRVAdapter;

public class FirstLevel extends AbstractExpandableItem<SecondLevel> implements MultiItemEntity{

    private String name;
    private int totalSchoolNums;

    public FirstLevel(String name, int totalSchoolNums) {
        this.name = name;
        this.totalSchoolNums = totalSchoolNums;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return MainRVAdapter.TYPE_FIRST_LEVEL;
    }

    public String getName() {
        return name;
    }

    public int getTotalSchoolNums() {
        return totalSchoolNums;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalSchoolNums(int totalSchoolNums) {
        this.totalSchoolNums = totalSchoolNums;
    }
}
