package com.example.hospital20.Models;

import java.util.ArrayList;

public class User {
    public String firstname, lastname, email, disease;
    public ArrayList <History> historyUser;
    public String uid;
     public User(){

     }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String firstname, String lastname, String email, ArrayList<History> historyUser) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.historyUser = historyUser;
    }

    public User(String firstname, String lastname, String email, String disease, ArrayList<History> historyUser) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.disease = disease;
        this.historyUser = historyUser;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<History> getHistoryUser() {
        return historyUser;
    }

    public void setHistoryUser(ArrayList<History> historyUser) {
        this.historyUser = historyUser;
    }
}
