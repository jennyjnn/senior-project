package com.jenny.medicationreminder.Model;

public class User {
    private String username;
    private String password;
    private String user_fname;
    private String user_lname;
    private String user_disease;
    private String user_phone;
    private String user_allergy;
    private String user_weight;
    private String user_age;
    private String morning_bf;
    private String morning_af;
    private String noon_bf;
    private String noon_af;
    private String evening_bf;
    private String evening_af;
    private String bed_bf;

    public User() {
    }

    public User(String username, String password, String user_fname, String user_lname, String user_disease, String user_phone, String user_allergy, String user_weight, String user_age, String morning_bf, String morning_af, String noon_bf, String noon_af, String evening_bf, String evening_af, String bed_bf) {
        this.username = username;
        this.password = password;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_disease = user_disease;
        this.user_phone = user_phone;
        this.user_allergy = user_allergy;
        this.user_weight = user_weight;
        this.user_age = user_age;
        this.morning_bf = morning_bf;
        this.morning_af = morning_af;
        this.noon_bf = noon_bf;
        this.noon_af = noon_af;
        this.evening_bf = evening_bf;
        this.evening_af = evening_af;
        this.bed_bf = bed_bf;
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
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getUser_disease() {
        return user_disease;
    }

    public void setUser_disease(String user_disease) {
        this.user_disease = user_disease;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_allergy() {
        return user_allergy;
    }

    public void setUser_allergy(String user_allergy) {
        this.user_allergy = user_allergy;
    }

    public String getUser_weight() {
        return user_weight;
    }

    public void setUser_weight(String user_weight) {
        this.user_weight = user_weight;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public String getMorning_bf() {
        return morning_bf;
    }

    public void setMorning_bf(String morning_bf) {
        this.morning_bf = morning_bf;
    }

    public String getMorning_af() {
        return morning_af;
    }

    public void setMorning_af(String morning_af) {
        this.morning_af = morning_af;
    }

    public String getNoon_bf() {
        return noon_bf;
    }

    public void setNoon_bf(String noon_bf) {
        this.noon_bf = noon_bf;
    }

    public String getNoon_af() {
        return noon_af;
    }

    public void setNoon_af(String noon_af) {
        this.noon_af = noon_af;
    }

    public String getEvening_bf() {
        return evening_bf;
    }

    public void setEvening_bf(String evening_bf) {
        this.evening_bf = evening_bf;
    }

    public String getEvening_af() {
        return evening_af;
    }

    public void setEvening_af(String evening_af) {
        this.evening_af = evening_af;
    }

    public String getBed_bf() {
        return bed_bf;
    }

    public void setBed_bf(String bed_bf) {
        this.bed_bf = bed_bf;
    }
}
