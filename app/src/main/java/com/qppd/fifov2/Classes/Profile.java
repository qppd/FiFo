package com.qppd.fifov2.Classes;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile {

    private int id;
    private int uid;
    private String firstname;
    private String lastname;
    private String birthdate;
    private String phone;
    private String income;
    private int type;

    public Profile(int id, int uid, String firstname, String lastname, String birthdate, String phone,
                   String income, int type) {
        this.id = id;
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.phone = phone;
        this.income = income;
        this.type = type;
    }

    public Profile(int uid, String firstname, String lastname, String birthdate, String phone,
                   String income, int type) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.phone = phone;
        this.income = income;
        this.type = type;
    }

    public Profile(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
