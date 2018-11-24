package com.example.snow.csci571hw9;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {

    private JSONArray UpcomingList;
    private Context context;
    public JSONObject event;



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public MyViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UpcomingAdapter(JSONArray UpcomingList, Context context) {
        this.UpcomingList = UpcomingList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.upcomings_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return  vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String name = new String();
        try {
            name = UpcomingList.getJSONObject(position).getString("displayName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.upcoming_eventname.setText(name);

        String uri = new String();
        try {
            uri = UpcomingList.getJSONObject(position).getString("uri");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String finalUri = uri;
        holder.upcoming_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(finalUri);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
                Toast.makeText(context, finalUri, Toast.LENGTH_SHORT).show();
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (UpcomingList.length() >5){
        return 5;}
        else{
            return UpcomingList.length();
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        public TextView upcoming_eventname;
        public TextView upcoming_artist;
        public TextView upcoming_time;
        public TextView upcoming_type;
        public CardView upcoming_card;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            upcoming_eventname = (TextView) itemView.findViewById(R.id.upcoming_eventname);
            upcoming_artist = (TextView) itemView.findViewById(R.id.upcoming_artist);
            upcoming_time = (TextView) itemView.findViewById(R.id.upcoming_time);
            upcoming_type = (TextView) itemView.findViewById(R.id.upcoming_type);
            upcoming_card = (CardView) itemView.findViewById(R.id.upcoming_card);

        }
    }

}