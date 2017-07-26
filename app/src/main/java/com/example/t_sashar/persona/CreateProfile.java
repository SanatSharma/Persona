package com.example.t_sashar.persona;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;

import java.net.MalformedURLException;

public class CreateProfile extends AppCompatActivity {

    SharedPreferences profile;
    final String PREFS_NAME = "MyPrefsFile";
    private Button submit;
    private EditText name;
    private EditText email;
    private EditText number;
    private EditText organization;
    String name_pref = "", email_pref = "", number_pref = "", org_pref = "";
    private MobileServiceClient mClient;
    private BusinessCard mBusinessCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        submit = (Button) findViewById(R.id.btn_signup);
        name = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);

        number = (EditText) findViewById(R.id.input_number);
        organization = (EditText) findViewById(R.id.input_organization);

        profile = getSharedPreferences(PREFS_NAME,0);
        String user_name = profile.getString("name", "0");
        String user_email = profile.getString("email", "0");
        String user_number = profile.getString("number", "0");
        String user_organization = profile.getString("organization", "0");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_pref = name.getText().toString();
                email_pref = email.getText().toString();
                number_pref = number.getText().toString();
                org_pref = organization.getText().toString();

                SharedPreferences.Editor editor = profile.edit();

                Log.v("HEERERRERERER", name_pref);

                editor.putString("name", name_pref);
                editor.putString("email", email_pref);
                editor.putString("number", number_pref);
                editor.putString("organization", org_pref);
                editor.apply();

                Log.v("NAMEEEEEEEE", getSharedPreferences(PREFS_NAME, 0).getString("name", "0"));

                try {
                    mClient = new MobileServiceClient("https://hackathon-persona.azurewebsites.net",
                            getApplicationContext());
                } catch (MalformedURLException e) {
                    new Exception("There was an error creating the Mobile Service. Verify the URL");
                }

                if (name_pref.equals("") || email_pref.equals("") || number_pref.equals("") ||
                        org_pref.equals("") ) {
                    Toast.makeText(getApplicationContext(), "Blank Field, please enter all values and submit", Toast.LENGTH_SHORT).show();
                } else {
                    sendServerMessage(name_pref, email_pref, number_pref, org_pref);

                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                }
            }
        });

        // send the data to the server
       // <TODO>

    }

    private void sendServerMessage(String name, String email, String number, String org) {
        mBusinessCard = new BusinessCard(name, email, number, org);
        new inserter().execute(mBusinessCard, mBusinessCard, mBusinessCard);
    }

    private class inserter extends AsyncTask<BusinessCard, BusinessCard, BusinessCard> {
        private MobileServiceJsonTable mJsonToDoTable;

        protected BusinessCard doInBackground(BusinessCard... card) {
            try {
                mJsonToDoTable = mClient.getTable("BusinessCard");
                JsonObject jsonItem = new JsonObject();
                jsonItem.addProperty("name", mBusinessCard.user_name);
                jsonItem.addProperty("email", mBusinessCard.user_email);
                jsonItem.addProperty("number", mBusinessCard.user_number);
                jsonItem.addProperty("organization", mBusinessCard.user_organization);
                jsonItem.addProperty("contacts", mBusinessCard.tagMap.toString());
                JsonObject insertedItem = mJsonToDoTable
                        .insert(jsonItem)
                        .get();
                SharedPreferences.Editor editor = profile.edit();
                editor.putString("id", insertedItem.getAsJsonPrimitive("id").getAsString());
                editor.apply();
            }
            catch(Exception e) {
                Log.v("NOTE", e.getMessage());
            }
            return null;
        }
        protected BusinessCard onPostExecute() {
            Log.v("NOTE", "DONE");
            return null;
        }
    }
}
