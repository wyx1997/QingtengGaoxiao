package com.victory.qingteng.qingtenggaoxiao.model.entity;

import com.google.gson.annotations.SerializedName;

public class ArticleDetails {

    @SerializedName("序号")
    private int number;

    @SerializedName("作者")
    private String author;

    @SerializedName("年级")
    private int grade;

    @SerializedName("大学")
    private String school;

    @SerializedName("专业")
    private String major;

    @SerializedName("题目")
    private String title;

    @SerializedName("文章编号")
    private String articleNum;

    public double getNumber() {
        return number;
    }

    public double getGrade() {
        return grade;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(String articleNum) {
        this.articleNum = articleNum;
    }
}
