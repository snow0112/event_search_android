package com.example.snow.csci571hw9;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TabArtist extends Fragment {
    private String segment_id, artist1, artist2;
    private TextView atris1_name0,atris1_name,atris1_follower,atris1_popilarity,atris1_url;
    private String Satris1_name,Satris1_follower,Satris1_popilarity,Satris1_url;
    public JSONObject Artist1_Spotify;
    private RecyclerView recycler1;
    private JSONArray Artist1_Photos = new JSONArray();

    public TabArtist() {
    }

    @SuppressLint("ValidFragment")
    public TabArtist (String segment_id,String artist1, String artist2) {
        this.segment_id = segment_id;
        this.artist1 = artist1;
        this.artist2 = artist2;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(  new String(this.segment_id).equals("KZFzniwnSyZfZ7v7nJ" ) ) {
            callSpotifyAPI(artist1);
        }
        callCustomAPI(artist1);
    }

    public void callSpotifyAPI(String artist){

        String url = null;
        try {
            url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/spotify-music/"+URLEncoder.encode( artist,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest spotifyinfo = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Artist1_Spotify = response;
                        //hello.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");
                        // TODO: Handle error
                    }
                });
        queue.add(spotifyinfo);

    }

    public void callCustomAPI(String artist){

        String url = null;
        try {
            url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/custom-search/"+URLEncoder.encode( artist,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest photos = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Artist1_Photos = response;
                        recycler1 = (RecyclerView) getView().findViewById(R.id.artist1_photos);
                        PhotoAdapter photoAdapter1 = new PhotoAdapter(Artist1_Photos, getContext());
                        recycler1.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recycler1.setAdapter(photoAdapter1);
                        //hello.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");
                        // TODO: Handle error
                    }
                });
        queue.add(photos);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_artist, container, false);
        recycler1 = (RecyclerView) rootView.findViewById(R.id.artist1_photos);
        PhotoAdapter photoAdapter1 = new PhotoAdapter(Artist1_Photos, getContext());
        recycler1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler1.setAdapter(photoAdapter1);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //hello = (TextView) getView().findViewById(R.id.textView3);
        //hello.setText(segment_id+","+artist1+","+artist2);
        atris1_name0 = (TextView) getView().findViewById(R.id.atris1_name0);
        atris1_name0.setText(artist1);
        atris1_url = (TextView) getView().findViewById(R.id.atris1_url);;

        atris1_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://open.spotify.com/artist/66CXWjxzNUsdJxJ2JdwvnR";
                Uri webpage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }
}