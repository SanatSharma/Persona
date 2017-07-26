package com.example.t_sashar.persona;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    final String PREFS_NAME = "MyPrefsFile";
    BusinessCard mBusinessCard;
    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBusinessCard = new BusinessCard("Name", "Email", "User");
        try {
            mClient = new MobileServiceClient("https://hackathon-persona.azurewebsites.net",
                    this);
        } catch (MalformedURLException e) {
            new Exception("There was an error creating the Mobile Service. Verify the URL");
        }
        mClient.getTable(BusinessCard.class).insert(mBusinessCard);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        if (settings.getString("name", "0") == "0") {
            // profile has not been made
            Log.d("Comments", "First time");

            // first time task
            final Intent i = new Intent(this, CreateProfile.class);

            Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                            finish();
                        }
                    }, 1000
            );


            // record the fact that the app has been started at least once
            settings.edit().putBoolean("first_launch", false).apply();
        } else {

            Log.v("NAME ", settings.getString("name", "0"));
            final Intent i = new Intent(this, HomeActivity.class);

            Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                            finish();
                        }
                    }, 1000
            );
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
