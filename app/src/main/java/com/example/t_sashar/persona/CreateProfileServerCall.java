package com.example.t_sashar.persona;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;

import java.net.MalformedURLException;

/**
 * Created by t-sashar on 7/25/2017.
 */

public class CreateProfileServerCall {
    BusinessCard mBusinessCard;
    MobileServiceClient mClient;
    Context context;
    SharedPreferences profile;


    CreateProfileServerCall(Context context, SharedPreferences profile ,String name, String email, String number, String org) {
        this.mBusinessCard = new BusinessCard(name, email, number, org);
        this.context = context;
        this.profile = profile;

        try {
            this.mClient = new MobileServiceClient("https://hackathon-persona.azurewebsites.net",
                    context);
        } catch (Exception e) {
            Log.d("URL", e.getMessage());
        }
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
                JsonObject insertedItem = mJsonToDoTable.insert(jsonItem).get();
                SharedPreferences.Editor editor = profile.edit();
                editor.putString("addedContactId", insertedItem.getAsJsonPrimitive("addedContactId").getAsString());
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
