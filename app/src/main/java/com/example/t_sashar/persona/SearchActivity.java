package com.example.t_sashar.persona;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by t-briken on 7/27/2017.
 */

public class SearchActivity extends AppCompatActivity {

    private EditText mTags;
    private Button mSearchButton;

    private String myId;
    final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences profile = getSharedPreferences(PREFS_NAME, 0);

       //this.myId = profile.getString()


        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        mTags = (EditText) findViewById(R.id.input_tags);
        mSearchButton = (Button) findViewById(R.id.search_button);

        mSearchButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

            }
        });
    }
}
