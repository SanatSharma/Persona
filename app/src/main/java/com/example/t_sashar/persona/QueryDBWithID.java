package com.example.t_sashar.persona;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;

import java.util.concurrent.ExecutionException;

/**
 * Created by t-sashar on 7/25/2017.
 */


public class QueryDBWithID {
    MobileServiceClient mClient;
    Context context;
    private MobileServiceJsonTable mJsonToDoTable;
    private String id;

    QueryDBWithID(Context context, String id) {

        Log.d("Check", "Getting here");
        Log.d("id", id);
        this.context = context;
        this.id = id;
        try {
            this.mClient = new MobileServiceClient("https://hackathon-persona.azurewebsites.net",
                    context);
        } catch (Exception e) {
            Log.d("URL", e.getMessage());
        }
    }

    public JsonObject getContact(){
        mJsonToDoTable = mClient.getTable("BusinessCard");
        Log.d("Table name", mJsonToDoTable.getTableName());
        JsonObject jsonItem = new JsonObject();
        try {
            jsonItem =  mJsonToDoTable.lookUp(id).get();
            Log.d("Got Contact", jsonItem.getAsJsonPrimitive("name").getAsString());
            return jsonItem;
        } catch (InterruptedException e) {
            Log.d("Error", "Interrupted exception");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d("Error", "General exception");
            e.printStackTrace();
        }
        return null;
    }
}
