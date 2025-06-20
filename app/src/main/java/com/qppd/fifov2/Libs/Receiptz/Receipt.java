package com.qppd.fifov2.Libs.Receiptz;

import com.qppd.fifov2.Classes.Cart;
import com.qppd.fifov2.Classes.Expense;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;

import java.util.ArrayList;

public class Receipt {

    StringBuilder sb = new StringBuilder();
    String symbol = "*";
    String spaceSymbol = " ";
    int width;

    String transaction_id;
    ArrayList<Expense> expenses;

    public Receipt(int width, ArrayList<Expense> expenses) {
        if (width % 2 == 1) {
            width++;
        }
        this.width = width;
        this.transaction_id = transaction_id;
        this.expenses = expenses;
    }

    public String generateReceipt() {
        sb = new StringBuilder();
        double totalExpenses = 0;
        String previousDate = "";

        for(Expense expense : expenses){
            if(!previousDate.equals(expense.getDate().trim())){

                newTextLeft(expense.getDate(), expense.getDate().length(), 0);
                previousDate = expense.getDate().trim();
                spaceDivider();
            }
            newTextLeft("    " +expense.getProduct() , expense.getProduct().length() + 4, 0);
            newTextRight(String.format("%.2f", expense.getPrice()), width / 2, -1);
            totalExpenses += expense.getPrice();
            spaceDivider();
        }




//
//        newTextCenter("POS VENDOR: ACISS SOFTWARE  SPECIALIST", 28, 0);
//        newTextCenter("BRGY MASAYA BAY LAGUNA", 28, 0);
//        newTextCenter("VAT REG TIN: 291-141-580-000", 28, 0);
//        newTextCenter("Accreditation No.: 0562911415802021051422", 28, 0);
//        newTextCenter("PTU No.: FP072022-061-0336750-00001", 28, 0);
//        newTextCenter("Date Issued: 07/18/2022", 28, 0);
//        newTextCenter("Valid Until: 07/18/2027", 28, 0);
//        spaceDivider();
//        spaceDivider();
//
//        newTextCenter("This serves as your Sales Invoice.", 28, 0);
//        newTextCenter("THIS INVOICE SHALL BE VALID FOR 5 YEARS", 28, 0);
//        newTextCenter("FROM THE DATE OF THE PERMIT TO USE.", 28, 0);

        lineDivider();
        newTextLeft("TOTAL", width, 1);
        newTextRight(String.format("%.2f", totalExpenses), width / 2, -1);
//        newTextLeft("CASH", width, 1);
//        newTextRight(String.format("%.2f",cash), width / 2, -1);
//        newTextLeft("CHANGE", width, 1);
//        newTextRight(String.format("%.2f",cash - total), width / 2, -1);
//        lineDivider();
//        newTextLeft("VATable", width, 1);
//        newTextRight(String.format("%.2f",total - (total * 0.12)), width / 2, -1);
//        newTextLeft("VAT(12%)", width, 1);
//        newTextRight(String.format("%.2f",total * 0.12), width / 2, -1);
//        newTextLeft("VAT Exempt Sale", width, 1);
//        newTextRight(String.format("%.2f", 0.00), width / 2, -1);
//        newTextLeft("VAT Zero-Rated Sale", width, 1);
//        newTextRight(String.format("%.2f", 0.00), width / 2, -1);
//        spaceDivider();
//        lineDivider();
//        lineDivider();
//        newTextCenter("YOUR HEALTH, OUR PRIORITY", 28, 0);
//        newTextCenter("THANK YOU FOR CHOOSING US", 28, 0);
//        lineDivider();
        return sb.toString();
    }

    void newTextLeft(String text, int limit, int fill) {
        StringBuilder builder = new StringBuilder();
        String[] strArr = splitString(text, spaceSymbol, limit, fill);

        for (String str : strArr) {
            builder.append(openLn());
            builder.append(fillRight(str, " ", width));
            builder.append(closeLn());
        }

        sb.append(builder.toString());
    }

    void newTextRight(String text, int limit, int fill) {
        StringBuilder builder = new StringBuilder();
        String[] strArr = splitString(text, spaceSymbol, limit, fill);

        for (String str : strArr) {
            builder.append(openLn());
            builder.append(fillLeft(str, " ", width));
            builder.append(closeLn());
        }

        sb.append(builder.toString());
    }

    void newTextCenter(String text, int limit, int fill) {
        StringBuilder builder = new StringBuilder();
        String[] strArr = splitString(text, spaceSymbol, limit, fill);

        for (String str : strArr) {

            builder.append(openLn());
            builder.append(fillBoth(str, " ", width));
            builder.append(closeLn());
        }

        sb.append(builder.toString());
    }

    String[] splitString(String str, String fs, int limit, int fill) {
        int arrlen = (int) Math.ceil((double) str.length() / limit);
        int strlen = str.length();
        String[] strArray = new String[arrlen];

        for (int i = 0; i < arrlen; i++) {
            int startIndex = i * limit;
            int endIndex = startIndex + limit;

            if (i == arrlen - 1) {
                if (fill == -1) {
                    strArray[i] = fillLeft(str.substring(startIndex, strlen), fs, limit);
                }
                else if (fill == 0) {
                    strArray[i] = fillBoth(str.substring(startIndex, strlen), fs, limit);
                }
                else {
                    strArray[i] = fillRight(str.substring(startIndex, strlen), fs, limit);
                }

            }
            else {
                strArray[i] = str.substring(startIndex, endIndex);
            }
        }

        return strArray;
    }

    String fillLeft(String text, String fs, int limit) {
        if (text.length() == limit) {
            return text;
        }

        int remaining = limit - text.length();
        return multiplyString(fs, remaining) + text;
    }

    String fillRight(String text, String fs, int limit) {
        if (text.length() == limit) {
            return text;
        }

        int remaining = limit - text.length();
        return text + multiplyString(fs, remaining);
    }

    String fillBoth(String text, String fs, int limit) {
        if (text.length() == limit) {
            return text;
        }

        int remaining = limit - text.length();
        int halfA = remaining / 2;
        int halfB = remaining - halfA;

        return multiplyString(fs, halfA) + text + multiplyString(fs, halfB);
    }

    void spaceDivider() {
        sb.append(openLn()).append(multiplyString(" ", width)).append(closeLn());
    }

    void lineDivider() {
        sb.append(multiplyString(symbol, width + 4)).append("\n");
    }

    String multiplyString(String str, int n) {
        return new String(new char[n]).replace("\0", str);
    }

    String openLn() {
        return " ";
    }

    String closeLn() {
        return " " + "\n";
    }
}
