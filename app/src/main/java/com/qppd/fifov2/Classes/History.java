package com.qppd.fifov2.Classes;

public class History {

    String date;
    double income, expenses, balance, target, total;

    public History(String date, double income, double expenses, double balance, double target, double total) {
        this.date = date;
        this.income = income;
        this.expenses = expenses;
        this.balance = balance;
        this.target = target;
        this.total = total;
    }

    public History() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
