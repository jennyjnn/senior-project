package com.jenny.medicationreminder.Model;

public class Med_Record {
    private String medRec_getTime;
    private String medRec_BefAft;
    private String medRec_dose;
    private String medRec_startDate;
    private String medRec_endDate;
    private String medRec_notiTime;
    private String medRec_notiDate;
    private String user_id;
    private String med_id;

    public Med_Record() {
    }

    public Med_Record(String medRec_getTime, String medRec_BefAft, String medRec_dose, String medRec_startDate, String medRec_endDate, String medRec_notiTime, String medRec_notiDate, String user_id, String med_id) {
        this.medRec_getTime = medRec_getTime;
        this.medRec_BefAft = medRec_BefAft;
        this.medRec_dose = medRec_dose;
        this.medRec_startDate = medRec_startDate;
        this.medRec_endDate = medRec_endDate;
        this.medRec_notiTime = medRec_notiTime;
        this.medRec_notiDate = medRec_notiDate;
        this.user_id = user_id;
        this.med_id = med_id;
    }

    public String getMedRec_getTime() {
        return medRec_getTime;
    }

    public void setMedRec_getTime(String medRec_getTime) {
        this.medRec_getTime = medRec_getTime;
    }

    public String getMedRec_BefAft() {
        return medRec_BefAft;
    }

    public void setMedRec_BefAft(String medRec_BefAft) {
        this.medRec_BefAft = medRec_BefAft;
    }

    public String getMedRec_dose() {
        return medRec_dose;
    }

    public void setMedRec_dose(String medRec_dose) {
        this.medRec_dose = medRec_dose;
    }

    public String getMedRec_startDate() {
        return medRec_startDate;
    }

    public void setMedRec_startDate(String medRec_startDate) {
        this.medRec_startDate = medRec_startDate;
    }

    public String getMedRec_endDate() {
        return medRec_endDate;
    }

    public void setMedRec_endDate(String medRec_endDate) {
        this.medRec_endDate = medRec_endDate;
    }

    public String getMedRec_notiTime() {
        return medRec_notiTime;
    }

    public void setMedRec_notiTime(String medRec_notiTime) {
        this.medRec_notiTime = medRec_notiTime;
    }

    public String getMedRec_notiDate() {
        return medRec_notiDate;
    }

    public void setMedRec_notiDate(String medRec_notiDate) {
        this.medRec_notiDate = medRec_notiDate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMed_id() {
        return med_id;
    }

    public void setMed_id(String med_id) {
        this.med_id = med_id;
    }
}
