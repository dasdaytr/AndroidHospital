package com.example.hospital20.Models;

public class History {
    public String nameDisease, desOfDisease,verdictDoctor;
    public History(){

    }

    public History(String nameDisease, String desOfDisease, String verdictDoctor) {
        this.nameDisease = nameDisease;
        this.desOfDisease = desOfDisease;
        this.verdictDoctor = verdictDoctor;
    }

    public String getNameDisease() {
        return nameDisease;
    }

    public void setNameDisease(String nameDisease) {
       this.nameDisease = nameDisease;
    }

    public String getDesOfDisease() {
        return desOfDisease;
    }

    public void setDesOfDisease(String desOfDisease) {
       this.desOfDisease = desOfDisease;
    }

    public String getVerdictDoctor() {
        return verdictDoctor;
    }

    public void setVerdictDoctor(String verdictDoctor) {
        this.verdictDoctor = verdictDoctor;
    }
}
