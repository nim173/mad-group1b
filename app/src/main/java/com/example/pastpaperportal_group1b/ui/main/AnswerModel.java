package com.example.pastpaperportal_group1b.ui.main;

public class AnswerModel {

    private String name;
    private String url;
    private String username;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
    private String pdfname;


    public AnswerModel() {
    }

    public AnswerModel(String pdfname, String url) {
        this.url = url;
        this.pdfname=pdfname;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPdfname() {
        return pdfname;
    }
}
