package com.qppd.fifov2.Classes;

public class Category {

    private int id;
    private String user_id;
    private String name;

    public Category(int id, String user_id, String name) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
