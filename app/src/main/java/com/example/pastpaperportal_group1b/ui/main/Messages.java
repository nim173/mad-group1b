package com.example.pastpaperportal_group1b.ui.main;

public class Messages {

    private String Body;

    private String Sent_To;

    private String Subject;

    private String author;

    private String userId;

    private String date;

    public Messages() {
    }

    public Messages(String body, String sent_To, String subject, String author, String userId,String date) {
        Body = body;
        Sent_To = sent_To;
        Subject = subject;
        this.author = author;
        this.userId = userId;
        this.date = date;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getSent_To() {
        return Sent_To;
    }

    public void setSent_To(String sent_To) {
        Sent_To = sent_To;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
