package com.victory.qingteng.qingtenggaoxiao.model.entity;

import com.google.gson.annotations.SerializedName;

public class ExamWikiData {

    @SerializedName("序号")
    private int number;

    @SerializedName("文章名称")
    private String articleName;

    @SerializedName("文章编号")
    private String articleNum;

    @SerializedName("办学分类")
    private String category;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(String articleNum) {
        this.articleNum = articleNum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
