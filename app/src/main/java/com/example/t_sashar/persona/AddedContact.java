package com.example.t_sashar.persona;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    String user_name;
    String user_email;
    String user_number;
    String user_organization;

    String id;

    ArrayList<String> tags;

    public AddedContact(JSONObject obj){
        getFieldsFromJson(obj);
        tags = new ArrayList<>();
    }

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

        name_display.setText(String.format("%s %s",name_display.getText().toString(), user_name));
        email_display.setText(String.format("%s %s", email_display.getText().toString(), user_email));
        number_display.setText(String.format("%s %s", number_display.getText().toString(), user_number));
        organization_display.setText(String.format("%s %s",
                organization_display.getText().toString(), user_organization));
        addUser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(addTags.getText().toString())){
                    getAllTags();
                }

                new AddedContactServerCall(getApplicationContext(), id, tags);

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getFieldsFromJson(JSONObject obj){
        try {
            this.user_name = obj.getString("user_name");
            this.user_email = obj.getString("user_email");
            this.user_number = obj.getString("user_number");
            this.user_organization = obj.getString("user_organization");
            this.id = obj.getString("added_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllTags(){
        String tagString = this.addTags.getText().toString();

        tagString = tagString.replace(';', ' ');

        for(String word : tagString.split(" ")) {
            this.tags.add(word);
        }
    }

}
