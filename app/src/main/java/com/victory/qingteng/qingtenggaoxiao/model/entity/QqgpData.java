package com.victory.qingteng.qingtenggaoxiao.model.entity;

import com.google.gson.annotations.SerializedName;

public class QqgpData {

    @SerializedName("序号")
    private int number;

    @SerializedName("群名")
    private String gpName;

    @SerializedName("群号")
    private long gpNum;

    @SerializedName("简介（不超过15字）")
    private String desc;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getGpName() {
        return gpName;
    }

    public void setGpName(String gpName) {
        this.gpName = gpName;
    }

    public long getGpNum() {
        return gpNum;
    }

    public void setGpNum(long gpNum) {
        this.gpNum = gpNum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
