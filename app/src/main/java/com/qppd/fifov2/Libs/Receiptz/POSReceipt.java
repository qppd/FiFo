package com.qppd.fifov2.Libs.Receiptz;

import com.qppd.fifov2.Classes.Cart;

import java.util.ArrayList;

public class POSReceipt {

    StringBuilder sb = new StringBuilder();
    String symbol = "*";
    String spaceSymbol = " ";
    int width;
    String title = "Orient Fuel";
    String transaction_id;
    String date = "";
    ArrayList<Cart> cartsItem;

    public POSReceipt(int width, String transaction_id, ArrayList<Cart> cartsItem) {
        if (width % 2 == 1) {
            width++;
        }

        this.width = width;
        this.transaction_id = transaction_id;
        this.cartsItem = cartsItem;
    }

    public String generateReceipt() {
        //date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String date = "08/02/23 13:08:28 PM";
        String transaction_id = "000000021659";
        sb = new StringBuilder();

        //lineDivider();
        //newTextCenter(title, 26, 0);
        newTextCenter("  POWEROIL PETROLEUM TRADE  ", 28, 1);
        newTextCenter("   Operated by: QUEEN C   ", 28, 0);
        newTextCenter("  PETROLUEM TRADING AND  ", 28, 0);
        newTextCenter("GENERAL MDSE OPC", 28, 0);
        newTextCenter(" VAT REG TIN:77437143000001", 28, 0);
        newTextCenter(" KALILAYAN UNISAN QUEZON", 28, 0);
        newTextCenter(" MIN: 22071812251216901", 28, 0);
        newTextCenter("  SN: SSD01Q61C376C01", 28, 0);

        spaceDivider();
        spaceDivider();

        newTextLeft("Cashier: POWEROIL UNISAN", 24, 0);
        newTextLeft("SI No.:000000021659", 19, 0);
        newTextLeft("Date: 8/2/2023 10:34 AM", 23, 0);
        newTextLeft("POS No.: 1", 10, 0);
        spaceDivider();

        newTextCenter("***SALES INVOICE***", 28, 0);
        spaceDivider();

        newTextLeft("DESCRIPTION       TOTAL(Php)", 28, 0);
        lineDivider();
        newTextLeft("B UNLEADED         2000.00", 28, 0);
        newTextCenter("33.14L x 60.35PL", 28, 0);
        spaceDivider();

        newTextLeft("Sale Total         2000.00", 28, 0);
        newTextLeft("Total Amount Due   2000.00", 28, 0);
        newTextLeft("Amount Tendered    2000.00", 28, 0);
        newTextLeft("Change                0.00", 28, 0);
        newTextLeft("Amount Tendered    2000.00", 28, 0);
        spaceDivider();

        newTextLeft("VATable Sales      1785.71", 28, 0);
        newTextLeft("VAT Amount          214.29", 28, 0);
        newTextLeft("VAT-Exempt Sales      0.00", 28, 0);
        newTextLeft("VAT Zero-Rated Sales  0.00", 28, 0);

        spaceDivider();
        newTextLeft("Payment Type: Cash", 28, 0);
        lineDivider();

        newTextLeft("Customer: __________________", 28, 0);
        newTextLeft("Address: ___________________", 28, 0);
        newTextLeft("TIN No.: ___________________", 28, 0);
        newTextLeft("Business Type: _____________", 28, 0);
        lineDivider();

        newTextCenter("POS VENDOR: ACISS SOFTWARE  SPECIALIST", 28, 0);
        newTextCenter("BRGY MASAYA BAY LAGUNA", 28, 0);
        newTextCenter("VAT REG TIN: 291-141-580-000", 28, 0);
        newTextCenter("Accreditation No.: 0562911415802021051422", 28, 0);
        newTextCenter("PTU No.: FP072022-061-0336750-00001", 28, 0);
        newTextCenter("Date Issued: 07/18/2022", 28, 0);
        newTextCenter("Valid Until: 07/18/2027", 28, 0);
        spaceDivider();
        spaceDivider();

        newTextCenter("This serves as your Sales Invoice.", 28, 0);
        newTextCenter("THIS INVOICE SHALL BE VALID FOR 5 YEARS", 28, 0);
        newTextCenter("FROM THE DATE OF THE PERMIT TO USE.", 28, 0);

//        lineDivider();
//        newTextLeft("TOTAL", width, 1);
//        newTextRight(String.format("%.2f",total), width / 2, -1);
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
