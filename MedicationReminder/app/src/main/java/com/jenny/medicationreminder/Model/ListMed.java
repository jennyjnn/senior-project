package com.jenny.medicationreminder.Model;

public class ListMed {
    private String nameMed;

    public ListMed() {
    }

    public ListMed(String nameMed, String properties, String descriptions) {
        this.nameMed = nameMed;
        this.properties = properties;
        this.descriptions = descriptions;
    }

    private String properties;

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

    private String descriptions;
}
