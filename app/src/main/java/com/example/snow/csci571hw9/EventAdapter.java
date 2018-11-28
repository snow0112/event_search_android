package com.example.snow.csci571hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public  JSONObject event;
    int Source;

    public static SharedPreferences favolist;
    public static SharedPreferences.Editor faveditor;


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

    public EventAdapter(JSONArray Events, Context context, int Source) {
        EventList = Events;
        this.context = context;
        this.Source = Source;
        favolist = context.getSharedPreferences("favoritelist",context.MODE_PRIVATE);
        faveditor = favolist.edit();
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        try {
            event = EventList.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        String date = new String();
        try {
            date = EventList.getJSONObject(position).getJSONObject("dates").getJSONObject("start").getString("localDate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.eventdate.setText(date);

        String time = new String();
        try {
            time = EventList.getJSONObject(position).getJSONObject("dates").getJSONObject("start").getString("localTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.eventtime.setText(time);

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

        String eventurl = new String();
        try {
            eventurl = EventList.getJSONObject(position).getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalName = name;
        final String finalVenue = venue;
        final String finalId = eventid;
        final String finalArtist2 = artist2;
        final String finalArtist1 = artist1;
        final String finalCate = cate;
        final String finalevent = event.toString();
        final String finalEventurl = eventurl;

        holder.eventitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, finalName, Toast.LENGTH_SHORT).show();

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
                    detailpage.putExtra("eventstring", finalevent);
                    detailpage.putExtra("eventurl", finalEventurl);
                    context.startActivity (detailpage);
                } catch (Exception e) { }

            }
        });


        //String eventinfav = context.getSharedPreferences("favoritelist", context.MODE_PRIVATE).getString(finalId,"nono");


        if ( favolist.contains(finalId) ){
            holder.favoriteresult.setImageResource(R.drawable.heart_fill_red);
        }else{
            holder.favoriteresult.setImageResource(R.drawable.heart_outline_black); }

        holder.favoriteresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, finalName, Toast.LENGTH_SHORT).show();
                //String eventinfav = context.getSharedPreferences("favoritelist", context.MODE_PRIVATE).getString(finalId,"nono");
                if ( favolist.contains(finalId) ){
                    Toast.makeText(context, finalName + " is removed from favorite", Toast.LENGTH_SHORT).show();
                    faveditor.remove(finalId);
                    faveditor.commit();
                    if (Source == 0){
                        notifyItemRemoved(position);
                        EventList.remove(position);
                        notifyItemChanged(position, EventList.length());
                        if ( favolist.getAll().size() == 0){
                            holder.no_favorite_message_in_recycler.setVisibility(View.VISIBLE);
                        }else{
                            holder.no_favorite_message_in_recycler.setVisibility(View.GONE);
                        }
                    }
                    holder.favoriteresult.setImageResource(R.drawable.heart_outline_black);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context, finalName + " is added to favorite", Toast.LENGTH_SHORT).show();
                    faveditor.putString(finalId,finalevent);
                    faveditor.commit();
                    holder.favoriteresult.setImageResource(R.drawable.heart_fill_red);
                }


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
        public TextView eventdate;
        public TextView eventtime;
        public ImageView category;
        public ImageView favoriteresult;
        public RelativeLayout eventitem;
        public TextView no_favorite_message_in_recycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventname = itemView.findViewById(R.id.eventname);
            venuename = itemView.findViewById(R.id.venuename);
            eventdate = itemView.findViewById(R.id.date);
            eventtime = itemView.findViewById(R.id.time);
            category = itemView.findViewById(R.id.category);
            favoriteresult = itemView.findViewById(R.id.favoriteresult);
            eventitem = itemView.findViewById(R.id.eventitem);
            no_favorite_message_in_recycler = itemView.findViewById(R.id.no_favorite_message_in_recycler);
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