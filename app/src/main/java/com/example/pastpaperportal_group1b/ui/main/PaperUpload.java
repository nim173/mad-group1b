package com.example.pastpaperportal_group1b.ui.main;

public class PaperUpload {

    private String PaperId;
    private String moduleId;
    private String note;
    private String url; // check this issue
    private String pdfName;

    public PaperUpload() {
    }

    public PaperUpload(String url, String pdfName) {
        this.url = url;
        this.pdfName = pdfName;
    }

    public String getPaperId() {
        return PaperId;
    }

    public void setPaperId(String paperId) {
        PaperId = paperId;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUrl() {
        return url;
    }

    public String getPdfName() {
        return pdfName;
    }

    public String getModuleId() { return moduleId; }

    public void setModuleId(String moduleId) { this.moduleId = moduleId; }

}

