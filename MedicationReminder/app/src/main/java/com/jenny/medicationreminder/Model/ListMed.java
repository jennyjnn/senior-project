package com.jenny.medicationreminder.Model;

public class ListMed {
    private String nameMed;
    private String properties;
    private String descriptions;
    private Boolean getMed;

    public ListMed() {
    }

    public ListMed(String nameMed, String properties, String descriptions, Boolean getMed) {
        this.nameMed = nameMed;
        this.properties = properties;
        this.descriptions = descriptions;
        this.getMed = getMed;
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

}
