package com.example.whatsmyallergy;

import java.util.ArrayList;

public class Users {

    String userUID;
    String userEmail;
    String userName;
    String userDOB;
    String userZip;
    boolean pets;
    ArrayList<String> knownAllergens;
    ArrayList<String> familyHistory;


    Users ()
    {
        this.userUID = "";
        this.userName = "";
        this.userEmail ="";
        this.userZip ="";
        this.userDOB ="";
        this.pets = false;
        this.knownAllergens = new ArrayList<>();
        this.familyHistory = new ArrayList<>();
    }

    Users (String uid, String email, String name, String DOB, String zip, boolean pets, ArrayList<String> ka, ArrayList<String> fh)
    {
        this.userUID = uid;
        this.userEmail = email;
        this.userName = name;
        this.userDOB = DOB;
        this.userZip = zip;
        this.pets = pets;
        this.knownAllergens = ka;
        this.familyHistory = fh;
    }

}
