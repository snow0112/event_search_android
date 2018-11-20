package com.example.snow.csci571hw9;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ResourceBundle;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private JSONArray EventList;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mTextView;
        public MyViewHolder(View v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public EventAdapter(JSONArray Events) {
        EventList = Events;
        Log.d("tag",EventList.toString());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.events_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        JSONObject event;
        String name = new String();
        try {
            name = EventList.getJSONObject(position).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.eventname.setText(name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return EventList.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView eventname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventname = (TextView) itemView.findViewById(R.id.eventname);
        }
    }

}