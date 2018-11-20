package com.example.snow.csci571hw9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ResourceBundle;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private JSONArray EventList;
    private Context context;


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

    public EventAdapter(JSONArray Events, Context context) {
        EventList = Events;
        this.context = context;
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

        Picasso.get().load( imageURL() ).into(holder.category);

        String name = new String();
        try {
            name = EventList.getJSONObject(position).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.eventname.setText(name);

        String venue = new String();
        try {
            venue = EventList.getJSONObject(position).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.venuename.setText(venue);

        final String finalName = name;
        holder.eventitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, finalName, Toast.LENGTH_SHORT).show();

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return EventList.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView eventname;
        public TextView venuename;
        public ImageView category;
        public RelativeLayout eventitem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventname = (TextView) itemView.findViewById(R.id.eventname);
            venuename = (TextView) itemView.findViewById(R.id.venuename);
            category = (ImageView) itemView.findViewById(R.id.category);
            eventitem = (RelativeLayout) itemView.findViewById(R.id.eventitem);
        }
    }

    public String imageURL(){
        String music = "http://csci571.com/hw/hw9/images/android/music_icon.png";
        String sports = "http://csci571.com/hw/hw9/images/android/sport_icon.png";
        String artandtheater = "http://csci571.com/hw/hw9/images/android/art_icon.png";
        String miscellaneous = "http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png";
        String film = "http://csci571.com/hw/hw9/images/android/film_icon.png";
        return  sports;
    }

}