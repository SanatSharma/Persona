package com.example.t_sashar.persona;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

/**
 * Created by JSoyinka on 7/25/17.
 */

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView mContactRecyclerView;
    public RecyclerView.Adapter<MyAdapter.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MobileServiceClient mClient;
    private BusinessCard mBusinessCard;
    private MobileServiceJsonTable mTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContactRecyclerView = (RecyclerView) findViewById(R.id.contacts_recycler_view);
        mContactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mContactRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mContactRecyclerView.setLayoutManager(mLayoutManager);

        try {
            mClient = new MobileServiceClient("https://hackathon-persona.azurewebsites.net",
                    getApplicationContext());
        } catch (MalformedURLException e) {
            new Exception("There was an error creating the Mobile Service. Verify the URL");
        }

        mTable = mClient.getTable("BusinessCard");
        try {
            mTable.where().;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // specify an adapter (see also next example)

        mAdapter = new MyAdapter(myDataset);
        mContactRecyclerView.setAdapter(mAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            public ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_contact, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mDataset[position]);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
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
