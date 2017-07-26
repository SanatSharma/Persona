package com.example.t_sashar.persona;

import android.content.Context;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.util.ArrayList;

/**
 * Created by brianobioha on 7/26/17.
 */

public class AddedContactServerCall {

    MobileServiceClient mClient;
    Context context;
    String id;

    ArrayList<String> tags;

    public AddedContactServerCall(Context context, String id, ArrayList<String> tags){
        this.context = context;
        this.id = id;
        this.tags = tags;

        try {
            this.mClient = new MobileServiceClient("https://hackathon-persona.azurewebsites.net", context);
        } catch (Exception e) {
            Log.d("URL", e.getMessage());
        }

    }



}
