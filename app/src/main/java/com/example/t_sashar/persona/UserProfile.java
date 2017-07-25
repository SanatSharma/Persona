package com.example.t_sashar.persona;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends AppCompatActivity {

    TextView name_display;
    TextView email_display;
    TextView number_display;
    TextView organization_display;
    String user_name = "", user_email = "", user_number = "", user_organization = "";
    final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        name_display = (TextView) findViewById(R.id.display_name);
        email_display = (TextView) findViewById(R.id.display_email);
        number_display = (TextView) findViewById(R.id.display_number);
        organization_display = (TextView) findViewById(R.id.display_organization);

        SharedPreferences profile = getSharedPreferences(PREFS_NAME, 0);

        user_name = profile.getString("name", "0");
        user_email = profile.getString("email", "0");
        user_number = profile.getString("number", "0");
        user_organization = profile.getString("organization", "0");

        if(user_name == "" || user_email == "" || user_organization == "" || user_number == "") {
            Toast.makeText(getApplicationContext(), "One or more of the fields are empty!!", Toast.LENGTH_SHORT).show();
        }
        else{
            name_display.setText(String.format("%s %s",name_display.getText().toString(), user_name));
            Log.v("HEREERTRETERTERT NAME", profile.getString("name", "0"));
            email_display.setText(String.format("%s %s", email_display.getText().toString(), user_email));
            number_display.setText(String.format("%s %s", number_display.getText().toString(), user_number));
            organization_display.setText(String.format("%s %s",
                    organization_display.getText().toString(), user_organization));
        }

    }

}
