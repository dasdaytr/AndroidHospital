package com.example.hospital20.Models;

import java.util.ArrayList;
import java.util.List;

public class Doctor {
    public String name,lastname,email,special;
    public String uid;
    public User user;
    public List<User> userArrayList;
    public Doctor(){

    }

    public Doctor(String name, String lastname, String email, String special, ArrayList<User> userArrayList) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.special = special;
        this.userArrayList = userArrayList;
    }

    public Doctor(String name, String lastname, String email, String special, String uid, User user, ArrayList<User> userArrayList) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.special = special;
        this.uid = uid;
        this.user = user;
        this.userArrayList = userArrayList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUID() {
        return uid;
    }

    public void setUID(String UID) {
        this.uid = UID;
    }

    public void setUserArrayList(List<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public List<User> getUserArrayList() {
        return userArrayList;
    }


    public Doctor(String name, String lastname, String email, String special, String uid, List<User> userArrayList) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.special = special;
        this.uid = uid;
        this.userArrayList = userArrayList;
    }

    public Doctor(String name, String lastname, String email, String special) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.special = special;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
}
