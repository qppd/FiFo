package com.qppd.fifov2.Classes;

public class Cart {

    String id;
    String name;
    String price;
    String stock;
    String quantity;

    public Cart(String id, String name, String price, String stock, String quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.quantity = quantity;
    }

    public Cart(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
