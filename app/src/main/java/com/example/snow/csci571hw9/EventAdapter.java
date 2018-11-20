package com.example.snow.csci571hw9;

import android.content.Context;
import android.content.Intent;
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

import static android.app.PendingIntent.getActivity;
import static android.support.v4.content.ContextCompat.startActivity;

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
        final JSONObject event;

        String cate = new String();
        try {
            cate = EventList.getJSONObject(position).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Picasso.get().load( imageURL(cate) ).into(holder.category);

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

        String eventid = new String();
        try {
            eventid = EventList.getJSONObject(position).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String artist1 = new String();
        try {
            artist1 = EventList.getJSONObject(position).getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(0).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String artist2 = new String();
        try {
            artist2 = EventList.getJSONObject(position).getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(1).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalName = name;
        final String finalVenue = venue;
        final String finalId = eventid;
        final String finalArtist2 = artist2;
        final String finalArtist1 = artist1;
        final String finalCate = cate;

        holder.eventitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, finalName, Toast.LENGTH_SHORT).show();

                try {
                    Intent detailpage;
                    detailpage = new Intent( context ,DetailPageActivity.class);
                    detailpage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    detailpage.putExtra("event_name", finalName);
                    detailpage.putExtra("event_id", finalId);
                    detailpage.putExtra("venue", finalVenue);
                    detailpage.putExtra("artist1", finalArtist1);
                    detailpage.putExtra("artist2", finalArtist2);
                    detailpage.putExtra("segment_id", finalCate);
                    context.startActivity (detailpage);
                } catch (Exception e) { }

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

    public String imageURL(String cate){
        String music = "http://csci571.com/hw/hw9/images/android/music_icon.png";
        String sports = "http://csci571.com/hw/hw9/images/android/sport_icon.png";
        String artandtheater = "http://csci571.com/hw/hw9/images/android/art_icon.png";
        String miscellaneous = "http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png";
        String film = "http://csci571.com/hw/hw9/images/android/film_icon.png";


        if ( new String(cate).equals("KZFzniwnSyZfZ7v7nJ") ){ return music ; }
        if ( new String(cate).equals("KZFzniwnSyZfZ7v7nE")){ return sports; }
        if ( new String(cate).equals("KZFzniwnSyZfZ7v7na")){ return artandtheater; }
        if ( new String(cate).equals("KZFzniwnSyZfZ7v7nn")){ return film; }
        if ( new String(cate).equals("KZFzniwnSyZfZ7v7n1")){ return miscellaneous; }
        return  sports;
    }

}