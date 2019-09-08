package com.example.pastpaperportal_group1b;

public class PaperUpload {

    private String PaperId;
    private String academicYear;
    private String Faculty;
    private String semester;
    private String module;
    private String specialization;
    private String note;

    public PaperUpload() {
    }

    public String getPaperId() {
        return PaperId;
    }

    public void setPaperId(String paperId) {
        PaperId = paperId;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
