package com.qppd.fifov2.Libs.Sqlz;



public class Constant {

    // user table name
    public  String USER_TABLE = "user_management";
    // user table columns
    public String user_id  = "user_id_no";
    public String user_password = "user_password";
    public String user_first_name = "user_firstname";
    public String user_mid_name = "user_midname";
    public String user_last_name = "user_lastname";
    public String user_sex = "user_sex";
    public String user_birthday = "user_birthday";
    public String user_age = "user_age";
    public String user_address = "user_address";
    public String user_phonenumber = "user_phonenumber";
    public String user_email = "user_email";
    public String user_sss_no = "user_sss_no";
    public String user_pagibig_no = "user_pagibig_no";
    public String user_philhealth_no = "user_philhealth_no";
    public String user_tin_no = "user_tin_no";


    // create user table
    public String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "
            + USER_TABLE + "("
            + user_id + " VARCHAR PRIMARY KEY,"
            + user_password + " VARCHAR,"
            + user_first_name + " TEXT,"
            + user_mid_name + " TEXT,"
            + user_last_name + " TEXT,"
            + user_sex + " TEXT,"
            + user_birthday + " DATE,"
            + user_age + " INTEGER,"
            + user_sss_no + " TEXT,"
            + user_pagibig_no + " TEXT,"
            + user_philhealth_no + " TEXT,"
            + user_tin_no + " TEXT,"
            + user_address + " VARCHAR,"
            + user_phonenumber + " TEXT,"
            + user_email + " VARCHAR "+")";

}
