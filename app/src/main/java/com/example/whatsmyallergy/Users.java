package com.example.whatsmyallergy;

import java.util.ArrayList;

public class Users {

    String userUID;
    String userEmail;
    String userName;
    String userDOB;
    boolean pets;
    ArrayList<String> knownAllergens;
    ArrayList<String> familyHistory;

    Users (String uid, String email, String name, String DOB, boolean pets, ArrayList<String> ka, ArrayList<String> fh)
    {
        this.userUID = uid;
        this.userEmail = email;
        this.userName = name;
        this.userDOB = DOB;
        this.pets = pets;
        this.knownAllergens = ka;
        this.familyHistory = fh;
    }

}
