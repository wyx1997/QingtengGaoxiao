package com.victory.qingteng.qingtenggaoxiao.presenter;

import com.victory.qingteng.qingtenggaoxiao.Contracts;
import com.victory.qingteng.qingtenggaoxiao.model.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragmentPresenter extends BasePresenter {

    private List<String> detailsData;
    
    private List<String> linkData = new ArrayList<>();

    private List<String> textData = new ArrayList<>();

    private boolean isReady = false;
    
    public DetailsFragmentPresenter(Contracts.IView view) {
        super(view);
    }

    public void handleList(int type){
        switch (type) {
            case DBHelper.TYPE_SCHOOL:
                String project = detailsData.get(6).equals("2") ? "入选985、211工程" : detailsData.get(6).equals("1") ? "入选211工程" : "";
                String banxue = detailsData.get(8);
                banxue = banxue.equals("1") ? "公办" : banxue.equals("2") ? "民办" :
                        banxue.equals("3") ? "中外合资" : banxue.equals("4") ? "港澳台合办" : "";
                textData.add(detailsData.get(0));
                textData.add(detailsData.get(1));
                textData.add(detailsData.get(3));
                textData.add("主管部门: " + detailsData.get(4));
                textData.add("学校类型: " + detailsData.get(5));
                textData.add("学校属性: " + project);
                textData.add("学校分类: " + detailsData.get(7));
                textData.add("学校类别: " + banxue);
                linkData.add(detailsData.get(9));
                linkData.add(detailsData.get(10));
                linkData.add(detailsData.get(11));
                linkData.add("https://baike.baidu.com/item/" + detailsData.get(0));
                linkData.add("http://tieba.baidu.com/f?kw=" + detailsData.get(0));
                linkData.add(detailsData.get(12));
                linkData.add(detailsData.get(13));
                break;
            case DBHelper.TYPE_MAJOR:
                linkData.add("https://baike.baidu.com/item/" + detailsData.get(0) + "专业");
                linkData.add("http://tieba.baidu.com/f?kw=" + detailsData.get(0) + "专业");
                break;
        }
    }
    
    public void getData(){
        if(isReady && null != linkData){
            List<List<String>> lists = new ArrayList<>();
            lists.add(linkData);
            if(textData.size() != 0){
                lists.add(textData);
            }
            view.showLoading();
            view.showData(lists);
            view.hideLoading();
        }
    }
    
    public void setDetailsData(List<String> detailsData) {
        this.detailsData = detailsData;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
