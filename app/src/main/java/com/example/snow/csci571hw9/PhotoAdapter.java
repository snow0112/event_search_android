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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotoAdapter  extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private JSONArray PhotoArray;
    private Context context;
    public JSONObject event;



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public MyViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PhotoAdapter(JSONArray PhotoArray, Context context) {
        this.PhotoArray = PhotoArray;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_layout, parent, false);
        PhotoAdapter.ViewHolder vh = new PhotoAdapter.ViewHolder(v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String link = new String();
        try {
            link = PhotoArray.getJSONObject(position).getString("link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Picasso.get().load( link ).into(holder.artistphoto);
        //Glide.with(context).load( link ).into(holder.artistphoto);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return PhotoArray.length();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView artistphoto;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            artistphoto = (ImageView) itemView.findViewById(R.id.artistphoto);
        }
    }
}
