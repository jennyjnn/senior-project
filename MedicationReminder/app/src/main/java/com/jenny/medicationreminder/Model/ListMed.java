package com.jenny.medicationreminder.Model;

public class ListMed {
    private String nameMed;
    private String properties;
    private String descriptions;
    private Boolean getMed;
    private String medID;
    private String medRecordID;
    private String dateMedList;
    private String timeMedList;

    public ListMed() {
    }

    public ListMed(String nameMed, String properties, String descriptions, Boolean getMed, String medID, String medRecordID, String dateMedList, String timeMedList) {
        this.nameMed = nameMed;
        this.properties = properties;
        this.descriptions = descriptions;
        this.getMed = getMed;
        this.medID = medID;
        this.medRecordID = medRecordID;
        this.dateMedList = dateMedList;
        this.timeMedList = timeMedList;
    }

    public String getNameMed() {
        return nameMed;
    }

    public void setNameMed(String nameMed) {
        this.nameMed = nameMed;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Boolean getGetMed() {
        return getMed;
    }

    public void setGetMed(Boolean getMed) {
        this.getMed = getMed;
    }

    public String getMedID() {
        return medID;
    }

    public void setMedID(String medID) {
        this.medID = medID;
    }

    public String getMedRecordID() {
        return medRecordID;
    }

    public void setMedRecordID(String medRecordID) {
        this.medRecordID = medRecordID;
    }

    public String getDateMedList() {
        return dateMedList;
    }

    public void setDateMedList(String dateMedList) {
        this.dateMedList = dateMedList;
    }

    public String getTimeMedList() {
        return timeMedList;
    }

    public void setTimeMedList(String timeMedList) {
        this.timeMedList = timeMedList;
    }
}
