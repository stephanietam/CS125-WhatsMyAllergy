package com.example.whatsmyallergy;

import java.util.ArrayList;

public class Users {

    String userUID;
    String userEmail;
    String userName;
    String userDOB;
    boolean pets;
    ArrayList<String> knownAllergens;

    Users (String uid, String email)
    {
        this.userUID=uid;
        this.userEmail=email;
        this.pets = false;
        this.knownAllergens = new ArrayList<>();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
    }
}
