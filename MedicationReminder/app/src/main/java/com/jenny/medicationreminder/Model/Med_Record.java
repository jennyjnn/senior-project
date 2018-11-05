package com.jenny.medicationreminder.Model;

public class Med_Record {
    private String MedRec_dateTime;
    private String MedRec_dose;
    private String MedRec_startDate;
    private String MedRec_endDate;
    private String MedRec_notiTime;
    private String User_id;
    private String Med_id;

    public Med_Record() {
    }

    public Med_Record(String medRec_dateTime, String medRec_dose, String medRec_startDate, String medRec_endDate, String medRec_notiTime, String user_id, String med_id) {
        MedRec_dateTime = medRec_dateTime;
        MedRec_dose = medRec_dose;
        MedRec_startDate = medRec_startDate;
        MedRec_endDate = medRec_endDate;
        MedRec_notiTime = medRec_notiTime;
        User_id = user_id;
        Med_id = med_id;
    }

    public String getMedRec_dateTime() {
        return MedRec_dateTime;
    }

    public void setMedRec_dateTime(String medRec_dateTime) {
        MedRec_dateTime = medRec_dateTime;
    }

    public String getMedRec_dose() {
        return MedRec_dose;
    }

    public void setMedRec_dose(String medRec_dose) {
        MedRec_dose = medRec_dose;
    }

    public String getMedRec_startDate() {
        return MedRec_startDate;
    }

    public void setMedRec_startDate(String medRec_startDate) {
        MedRec_startDate = medRec_startDate;
    }

    public String getMedRec_endDate() {
        return MedRec_endDate;
    }

    public void setMedRec_endDate(String medRec_endDate) {
        MedRec_endDate = medRec_endDate;
    }

    public String getMedRec_notiTime() {
        return MedRec_notiTime;
    }

    public void setMedRec_notiTime(String medRec_notiTime) {
        MedRec_notiTime = medRec_notiTime;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getMed_id() {
        return Med_id;
    }

    public void setMed_id(String med_id) {
        Med_id = med_id;
    }
}
