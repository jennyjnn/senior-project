package com.jenny.medicationreminder.Model;

public class ListMedHistory {
    private String medName;
    private String medProperty;
    private String medDose;
    private String medID;

    public ListMedHistory() {
    }

    public ListMedHistory(String medName, String medProperty, String medDose, String medID) {
        this.medName = medName;
        this.medProperty = medProperty;
        this.medDose = medDose;
        this.medID = medID;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedProperty() {
        return medProperty;
    }

    public void setMedProperty(String medProperty) {
        this.medProperty = medProperty;
    }

    public String getMedDose() {
        return medDose;
    }

    public void setMedDose(String medDose) {
        this.medDose = medDose;
    }

    public String getMedID() {
        return medID;
    }

    public void setMedID(String medID) {
        this.medID = medID;
    }
}
