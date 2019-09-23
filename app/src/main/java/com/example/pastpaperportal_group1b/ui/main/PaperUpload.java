package com.example.pastpaperportal_group1b.ui.main;

import java.util.Map;

public class PaperUpload {

    private String paperId;
    private String moduleId;
    //private String note;
    private String url; // check this issue

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    private String pdfName;

    public PaperUpload() {
    }


    public String getModuleId() { return moduleId; }

    public void setModuleId(String moduleId) { this.moduleId = moduleId; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }
}

