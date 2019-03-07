package com.example.whatsmyallergy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountInfo extends AppCompatActivity {

    private Button finishButton;
    private EditText editName, editDOB;


    private boolean checkComplete()
    {
        if (editName!=null && editName!=null)
            return true;
        else return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        Intent i = getIntent();
        String uid = i.getStringExtra("uid");

        editName = (EditText) findViewById(R.id.editName);
        editDOB = (EditText) findViewById(R.id.editDOB);








        finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInfo.this, MainActivity.class));
            }
        });



    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }
}
