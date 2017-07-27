package com.example.t_sashar.persona;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;

import org.json.JSONObject;

/**
 * Created by JSoyinka on 7/25/17.
 */

public class ContactsServerCall {
    String mId;
    MobileServiceClient mClient;
    Context context;
    BusinessCard mBusinessCard;
    SharedPreferences mProfile;


    ContactsServerCall(Context context, SharedPreferences profile) {
        this.context = context;

        try {
            this.mClient = new MobileServiceClient("https://hackathon-persona.azurewebsites.net",
                    context);
        } catch (Exception e) {
            Log.d("URL", e.getMessage());
        }
        mProfile = profile;
        String id = mProfile.getString("addedContactId", "0");
        new ContactsServerCall.inserter().execute(id, id, id);
    }


    private class inserter extends AsyncTask<String, String, String> {
        private MobileServiceJsonTable mJsonToDoTable;

        @Override
        protected String doInBackground(String... params) {
            try {
                mJsonToDoTable = mClient.getTable("BusinessCard");
                JsonObject result = mJsonToDoTable.lookUp(mId).get();
                SharedPreferences.Editor editor = mProfile.edit();
                editor.putString("contacts", result.get("contacts").getAsString());
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
