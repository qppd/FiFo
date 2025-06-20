package com.qppd.fifov2.Globals;

public class ExpenseGlobal {

    private static int category_id;
    private static String title;

    public static int getCategory_id() {
        return category_id;
    }

    public static void setCategory_id(int category_id) {
        ExpenseGlobal.category_id = category_id;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        ExpenseGlobal.title = title;
    }
}
