package com.victory.qingteng.qingtenggaoxiao.model.eventbus.message;

import java.util.List;

public class ArrayFragmentMessage extends EventBusMessage {

    private int type;

    private String subTitle;

    private String title;

    private List<String> data;

    public ArrayFragmentMessage(int type, String subTitle, String title, List<String> data) {
        this.type = type;
        this.subTitle = subTitle;
        this.title = title;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
