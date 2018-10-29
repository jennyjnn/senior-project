package com.jenny.medicationreminder.Model;

public class User {
    private String username;
    private String password;
    private String User_fname;
    private String User_lname;
    private String User_disease;
    private String User_phone;
    private String User_allergy;
    private String User_weight;
    private String User_age;

    public User() {
    }

    public User(String username, String password, String user_fname, String user_lname, String user_disease, String user_phone, String user_allergy, String user_weight, String user_age) {
        this.username = username;
        this.password = password;
        User_fname = user_fname;
        User_lname = user_lname;
        User_disease = user_disease;
        User_phone = user_phone;
        User_allergy = user_allergy;
        User_weight = user_weight;
        User_age = user_age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_fname() {
        return User_fname;
    }

    public void setUser_fname(String user_fname) {
        User_fname = user_fname;
    }

    public String getUser_lname() {
        return User_lname;
    }

    public void setUser_lname(String user_lname) {
        User_lname = user_lname;
    }

    public String getUser_disease() {
        return User_disease;
    }

    public void setUser_disease(String user_disease) {
        User_disease = user_disease;
    }

    public String getUser_phone() {
        return User_phone;
    }

    public void setUser_phone(String user_phone) {
        User_phone = user_phone;
    }

    public String getUser_allergy() {
        return User_allergy;
    }

    public void setUser_allergy(String user_allergy) {
        User_allergy = user_allergy;
    }

    public String getUser_weight() {
        return User_weight;
    }

    public void setUser_weight(String user_weight) {
        User_weight = user_weight;
    }

    public String getUser_age() {
        return User_age;
    }

    public void setUser_age(String user_age) {
        User_age = user_age;
    }
}
