package com.qppd.fifov2.Classes;

import java.util.Date;

public class Expense {

    private int id;
    private int category_id;
    private String product;
    private double price;
    private int priority;

    public Expense(int id, int category_id, String product, double price, int priority, String date) {
        this.id = id;
        this.category_id = category_id;
        this.product = product;
        this.price = price;
        this.priority = priority;
        this.date = date;
    }

    public Expense(int category_id, String product, double price, int priority, String date) {
        this.category_id = category_id;
        this.product = product;
        this.price = price;
        this.priority = priority;
        this.date = date;
    }


    public Expense() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;


}
