package com.qppd.fifov2.Classes;

public class CategoryExpense {

    private String category_name;
    private double expense_total;

    public CategoryExpense(String category_name, double expense_total) {
        this.category_name = category_name;
        this.expense_total = expense_total;
    }

    public CategoryExpense(){

    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public double getExpense_total() {
        return expense_total;
    }

    public void setExpense_total(double expense_total) {
        this.expense_total = expense_total;
    }
}
