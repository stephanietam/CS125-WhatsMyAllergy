package com.example.whatsmyallergy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class AccountInfo extends AppCompatActivity {

    private Bundle userBundle;
    private Button finish1Button, finish2Button;
    private EditText editName, editDOB, editZip;
    private Switch petSwitch;
    private String petStr;
    private ScrollView mainScroll;
    boolean petBool;
    ArrayList<String> knownAllergenList;
    ArrayList<String> familyHistoryList;
    private CheckBox grassKA, treesKA, ragweedKA, moldKA, dustKA, petKA;
    private CheckBox grassFH, treesFH, ragweedFH, moldFH, dustFH, petFH;
    private ArrayList<CheckBox> allergenKA;
    private ArrayList<CheckBox> allergenFH;
    private InputMethodManager inputMethodManager;

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    public void hideKeybord(View view) {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private TextWatcher  textWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            checkComplete();
        }
    };


    private void checkComplete() {
        String userName = editName.getText().toString();
        String userDOB = editDOB.getText().toString();
        if (userName.equals("") || userDOB.equals("") || (!checkZip()) || (userName.replaceAll("[^A-Za-z0-9 ]", "").length() == 0)) {
            finish1Button.setEnabled(false);
            finish2Button.setEnabled(false);
        } else {
            finish1Button.setEnabled(true);
            finish2Button.setEnabled(true);
        }

    }

    private boolean checkZip() {
        String userZip = editZip.getText().toString();

        if (userZip.equals("") || userZip.length()!=5 || (userZip.replaceAll("[^0-9]", "").length() != userZip.length()))
        {
            return false;
        }
        return true;
    }


    private void getID ()
    {
        editName = findViewById(R.id.editName);
        editDOB = findViewById(R.id.editDOB);
        editZip = findViewById(R.id.editZip);
        petSwitch = findViewById(R.id.petSwitch);
        grassKA =findViewById(R.id.grass_check_ka);
        treesKA =findViewById(R.id.tree_check_ka);
        ragweedKA = findViewById(R.id.ragweed_check_ka);
        moldKA = findViewById(R.id.mold_check_ka);
        dustKA = findViewById(R.id.dust_check_ka);
        petKA = findViewById(R.id.pet_check_ka);
        grassFH =findViewById(R.id.grass_check_fh);
        treesFH =findViewById(R.id.tree_check_fh);
        ragweedFH = findViewById(R.id.ragweed_check_fh);
        moldFH = findViewById(R.id.mold_check_fh);
        dustFH = findViewById(R.id.dust_check_fh);
        petFH = findViewById(R.id.pet_check_fh);
        mainScroll = findViewById(R.id.mainScrollView);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }


    private void addOnClicks()
    {
        allergenKA.add(grassKA);
        allergenKA.add(treesKA);
        allergenKA.add(ragweedKA);
        allergenKA.add(moldKA);
        allergenKA.add(dustKA);
        allergenKA.add(petKA);
        allergenFH.add(grassFH);
        allergenFH.add(treesFH);
        allergenFH.add(ragweedFH);
        allergenFH.add(moldFH);
        allergenFH.add(dustFH);
        allergenFH.add(petFH);

        for (int i = 0; i< allergenKA.size(); i++)
        {
            final CheckBox temp = allergenKA.get(i);
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    if (temp.isChecked())
                    {
                        if (!knownAllergenList.contains(temp.getText().toString()))
                        {
                            knownAllergenList.add(temp.getText().toString());
                        }
                    }
                    else
                    {
                        if (knownAllergenList.contains(temp.getText().toString())){
                            knownAllergenList.remove(temp.getText().toString());
                        }
                    }
                }
            });
        }

        for (int i = 0; i< allergenFH.size(); i++)
        {
            final CheckBox temp = allergenFH.get(i);
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    if (temp.isChecked())
                    {
                        if (!familyHistoryList.contains(temp.getText().toString()))
                        {
                            familyHistoryList.add(temp.getText().toString());
                        }
                    }
                    else
                    {
                        if (familyHistoryList.contains(temp.getText().toString())){
                            familyHistoryList.remove(temp.getText().toString());
                        }
                    }

                }
            });
        }

    }


    private void finishSetup () {
        finish1Button = findViewById(R.id.finish1Button);
        finish1Button.setEnabled(false);
        finish1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users user = new Users();
                if (user.isAlpha(editName.getText().toString()) && !user.check_valid(editDOB.getText().toString()).equals("")) {
                    petBool = Boolean.valueOf(petStr);
                    String key = userBundle.getString("uid");
                    user = new Users(key, userBundle.getString("email"), editName.getText().toString(), editDOB.getText().toString(), editZip.getText().toString(), petBool, knownAllergenList, familyHistoryList);
                    myRef.child(key).setValue(user);
                    Intent intent = new Intent(AccountInfo.this, MainActivity.class);
                    intent.putExtra("uid", key);
                    startActivity(intent);
                }
                else if (!user.isAlpha(editName.getText().toString())){
                    Toast.makeText(AccountInfo.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                }
                else if (user.check_valid(editDOB.getText().toString()).equals("")) {
                    Toast.makeText(AccountInfo.this, "Enter a valid date.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AccountInfo.this, "Can't create account. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        finish2Button = findViewById(R.id.finish2Button);
        finish2Button.setEnabled(false);
        finish2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users user = new Users();
                if (user.isAlpha(editName.getText().toString()) && !user.check_valid(editDOB.getText().toString()).equals("")) {
                    petBool = Boolean.valueOf(petStr);
                    String key = userBundle.getString("uid");
                    user = new Users(key, userBundle.getString("email"), editName.getText().toString(), editDOB.getText().toString(), editZip.getText().toString(), petBool, knownAllergenList, familyHistoryList);
                    myRef.child(key).setValue(user);
                    Intent intent = new Intent(AccountInfo.this, MainActivity.class);
                    intent.putExtra("uid", key);
                    startActivity(intent);
                }
                else if (!user.isAlpha(editName.getText().toString())){
                    Toast.makeText(AccountInfo.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                }
                else if (user.check_valid(editDOB.getText().toString()).equals("")) {
                    Toast.makeText(AccountInfo.this, "Enter a valid date.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AccountInfo.this, "Can't create account. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");


        Intent intent = getIntent();
        userBundle = intent.getExtras();


        allergenKA =  new ArrayList<>();
        allergenFH = new ArrayList<>();
        knownAllergenList = new ArrayList<>();
        familyHistoryList = new ArrayList<>();
        getID();
        mainScroll.requestFocus();
        addOnClicks();
        petBool = false;
        editName.addTextChangedListener(textWatcher);
        editDOB.addTextChangedListener(textWatcher);
        editZip.addTextChangedListener(textWatcher);

        petSwitch.setChecked(false);

        petSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
               if (petSwitch.isChecked()) {
                   petStr = petSwitch.getTextOn().toString();
               }
               else {
                   petStr = petSwitch.getTextOff().toString();
               }
            }
        });

        finishSetup();

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }
}
