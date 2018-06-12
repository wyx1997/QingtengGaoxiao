package com.victory.qingteng.qingtenggaoxiao.model.eventbus.message;

import java.util.List;

public class StringListMessage extends EventBusMessage {

    private List<String> list;

    public StringListMessage(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
