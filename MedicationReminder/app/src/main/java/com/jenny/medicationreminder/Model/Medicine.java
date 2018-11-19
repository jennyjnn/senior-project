package com.jenny.medicationreminder.Model;

public class Medicine {
    private String Med_intro;
    private String Med_name;
    private String Med_preserve;
    private String Med_sideEffect;
    private String Med_warning;

    public Medicine() {
    }

    public Medicine(String med_intro, String med_name, String med_preserve, String med_sideEffect, String med_warning) {
        Med_intro = med_intro;
        Med_name = med_name;
        Med_preserve = med_preserve;
        Med_sideEffect = med_sideEffect;
        Med_warning = med_warning;
    }

    public String getMed_intro() {
        return Med_intro;
    }

    public void setMed_intro(String med_intro) {
        Med_intro = med_intro;
    }

    public String getMed_name() {
        return Med_name;
    }

    public void setMed_name(String med_name) {
        Med_name = med_name;
    }

    public String getMed_preserve() {
        return Med_preserve;
    }

    public void setMed_preserve(String med_preserve) {
        Med_preserve = med_preserve;
    }

    public String getMed_sideEffect() {
        return Med_sideEffect;
    }

    public void setMed_sideEffect(String med_sideEffect) {
        Med_sideEffect = med_sideEffect;
    }

    public String getMed_warning() {
        return Med_warning;
    }

    public void setMed_warning(String med_warning) {
        Med_warning = med_warning;
    }
}
