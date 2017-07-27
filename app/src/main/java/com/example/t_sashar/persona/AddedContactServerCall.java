package com.example.t_sashar.persona;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by brianobioha on 7/26/17.
 */

public class AddedContactServerCall {

    MobileServiceClient mClient;
    Context context;
    String myId;
    String addedContactId;
    ArrayList<String> tags;

    public AddedContactServerCall(Context context, String myId, String addedContactId, ArrayList<String> tags){
        this.context = context;
        this.myId = myId;
        this.addedContactId = addedContactId;
        this.tags = tags;

        try {
            this.mClient = new MobileServiceClient("https://hackathon-persona.azurewebsites.net", context);
        } catch (Exception e) {
            Log.d("URL", e.getMessage());
        }

        new inserter().execute(this.tags, this.tags, this.tags);
    }

    private class inserter extends AsyncTask< ArrayList<String>, ArrayList<String>, ArrayList<String>> {

        protected ArrayList<String> doInBackground(ArrayList<String>... tagsMap){

            MobileServiceTable<BusinessCard> mTable = mClient.getTable("Business", BusinessCard.class);

            try {

                //Returns a reference to the contacts map of the calling user
                MobileServiceList<BusinessCard> result = mTable.where().field("id").eq(myId).select("contacts").execute().get();

                Log.v("Retrieved Row-Col", result.get(0).user_name);

                // TODO: 7/26/2017 Insert addContactId and tags arraylist into the contacts map of the calling user

            } catch (InterruptedException e) {

                e.printStackTrace();

            } catch (ExecutionException e) {

                e.printStackTrace();
            }
            return null;
        }
    }
}
