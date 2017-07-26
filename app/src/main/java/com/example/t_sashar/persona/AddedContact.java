package com.example.t_sashar.persona;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by brianobioha on 7/25/17.
 */

public class AddedContact extends AppCompatActivity {

    TextView name_display;
    TextView email_display;
    TextView number_display;
    TextView organization_display;
    EditText addTags;
    Button addUser_button;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name_display = (TextView) findViewById(R.id.display_name);
        email_display = (TextView) findViewById(R.id.display_email);
        number_display = (TextView) findViewById(R.id.display_number);
        organization_display = (TextView) findViewById(R.id.display_organization);
        addTags = (EditText) findViewById(R.id.add_tags);
        addUser_button = (Button) findViewById(R.id.addUser_button);
    }

}
