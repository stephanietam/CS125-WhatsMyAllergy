package com.example.whatsmyallergy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class AccountInfo extends AppCompatActivity {

    private Button finishButton;
    private EditText editName, editDOB;
    private Switch petSwitch;
    private String petStr;
    boolean petBool;
    ArrayList<String> knownAllergenList;
    ArrayList<String> familyHistoryList;

    private CheckBox grassKA, treesKA, ragweedKA, moldKA, dustKA, petKA;
    private CheckBox grassFH, treesFH, ragweedFH, moldFH, dustFH, petFH;
    private ArrayList<CheckBox> allergenKA;
    private ArrayList<CheckBox> allergenFH;

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
        String userDOB = editName.getText().toString();
        userName.replaceAll("[^A-Za-z0-9 ]", "");
        if (userName.equals("") || userDOB.equals("") || (userName.replaceAll("[^A-Za-z0-9 ]", "").length() == 0)) {
            finishButton.setEnabled(false);
        } else {
            finishButton.setEnabled(true);
        }

    }

    private void getID ()
    {
        editName = findViewById(R.id.editName);
        editDOB = findViewById(R.id.editDOB);
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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        Intent i = getIntent();
        String uid = i.getStringExtra("uid");
        allergenKA =  new ArrayList<>();
        allergenFH = new ArrayList<>();
        knownAllergenList = new ArrayList<>();
        familyHistoryList = new ArrayList<>();
        getID();
        addOnClicks();
        petBool = false;
        editName.addTextChangedListener(textWatcher);
        editDOB.addTextChangedListener(textWatcher);
        petSwitch.setChecked(false);

        petSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (petSwitch.isChecked()) {
                   petStr = petSwitch.getTextOn().toString();
               }
               else {
                   petStr = petSwitch.getTextOff().toString();
               }
            }
        });

        finishButton = findViewById(R.id.finishButton);
        finishButton.setEnabled(false);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petBool = Boolean.valueOf(petStr);
                System.out.println("KNOWN ALLERGEN LIST: " + knownAllergenList);
                System.out.println("FAMILY ALLERGEN LIST: " + familyHistoryList);
                startActivity(new Intent(AccountInfo.this, MainActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }
}
