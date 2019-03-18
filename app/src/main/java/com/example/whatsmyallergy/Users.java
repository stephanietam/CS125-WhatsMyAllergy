package com.example.whatsmyallergy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

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


    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c) && c!=' ') {
                System.out.println("+++++++++" + c);
                return false;
            }
        }

        return true;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public String check_valid(String dateStr)
    {
        StringTokenizer parsed = new StringTokenizer(dateStr, "/");
        ArrayList<String> date = new ArrayList<>();
        while (parsed.hasMoreElements())
        {
            date.add((String) parsed.nextElement());
        }
        int currentYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()));
        int currentMonth = Integer.valueOf(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
        if (date.size()!=3)
            return "";
        for (int i = 0; i < 3; i++) {
            if (!isNumeric(date.get(i))) {
                return "";
            }
        }
        //check month
        if (Integer.valueOf(date.get(0))>12 || Integer.valueOf(date.get(0))<0 || (Integer.valueOf(date.get(0))>currentMonth && Integer.valueOf(date.get(2))>=currentYear)){
            return "";
        }
        //check year
        if (Integer.valueOf(date.get(2))<0 || Integer.valueOf(date.get(2))>currentYear)
            return "";
        //check leap year
        if (Integer.valueOf(date.get(0))==2 && Integer.valueOf(date.get(2))%4 ==0 )
        {
            if (Integer.valueOf(date.get(1))>29 || Integer.valueOf(date.get(1))<0)
                return "";
        }
        else
        {
            if (Integer.valueOf(date.get(1))>28 || Integer.valueOf(date.get(1))<0)
                return "";
        }

        StringBuffer valid_date = new StringBuffer();
        for (int i = 2; i >= 0; i--) {
            valid_date.append(date.get(i));
        }

        return valid_date.toString();
    }

    public int age()
    {

        int dob = Integer.valueOf(check_valid(this.userDOB));
        int today = Integer.valueOf(Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime())));
        return (today-dob)/10000;


    }

}
