package com.qppd.fifov2.Classes;

public class Note {

    private int id;
    private String title;
    private String content;
    private String datetime;

    public Note(int id, String title, String content, String datetime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.datetime = datetime;
    }

    public Note(String title, String content, String datetime) {
        this.title = title;
        this.content = content;
        this.datetime = datetime;
    }

    public Note(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}


