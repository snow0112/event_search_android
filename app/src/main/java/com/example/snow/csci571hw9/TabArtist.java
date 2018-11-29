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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import static java.lang.Float.parseFloat;

public class TabArtist extends Fragment {
    private String segment_id, artist1, artist2;
    private TextView atris1_name0;
    private TextView atris2_name0;
    private TableLayout artist1_spotify;
    public JSONObject Artist1_Spotify = new JSONObject(), Artist2_Spotify  = new JSONObject();
    private RecyclerView recycler1, recycler2;
    private JSONArray Artist1_Photos = new JSONArray(), Artist2_Photos = new JSONArray();
    private Boolean pba1_spotify, pba2_spotify, pba1_photo, pba2_photo, NR1, NR2;
    private Boolean SEARCH;

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

        SEARCH = true;
        NR1 = false;
        NR2 = false;

        pba1_spotify = true;
        pba2_spotify = true;
        if(  new String(this.segment_id).equals("KZFzniwnSyZfZ7v7nJ" ) ) {
            //Toast.makeText(getActivity(), "search artist" ,Toast.LENGTH_LONG).show();
            pba1_spotify = false;
            //callSpotifyAPI(artist1,1);
            if ( ! new String(artist2).equals("") ){
                pba2_spotify = false;
                //callSpotifyAPI(artist2,2);
            }
        }

        pba1_photo = false;
        pba2_photo = true;
        //callCustomAPI(artist1,1);
        if ( ! new String(artist2).equals("") ){
            pba2_photo = false;
            //callCustomAPI(artist2,2);
        }
    }

    public void callSpotifyAPI(String artist, final int i){

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
                        if (i == 1){
                            pba1_spotify = true;
                            RelativeLayout progressbar = getView().findViewById(R.id.searchingspotify_artist1);
                            progressbar.setVisibility(View.GONE);
                            try {
                                Artist1_Spotify = response.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                                settable(Artist1_Spotify,1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (i == 2){
                            pba2_spotify = true;
                            RelativeLayout progressbar = getView().findViewById(R.id.searchingspotify_artist2);
                            progressbar.setVisibility(View.GONE);
                            try {
                                Artist2_Spotify = response.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                                settable(Artist2_Spotify,2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");
                        // TODO: Handle error
                        if (i == 1){
                            Toast.makeText(getActivity(), "error! can't find artist's spotify" ,Toast.LENGTH_LONG).show();
                            pba1_spotify = true;
                            RelativeLayout progressbar = getView().findViewById(R.id.searchingspotify_artist1);
                            progressbar.setVisibility(View.GONE);
                        }
                        if (i == 2){
                            pba2_spotify = true;
                            RelativeLayout progressbar = getView().findViewById(R.id.searchingspotify_artist2);
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "error! can't find artist's spotify" ,Toast.LENGTH_LONG).show();
                        }
                    }
                });
        queue.add(spotifyinfo);

    }

    public void callCustomAPI(String artist, final int i){

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
                        if (i == 1){
                            pba1_photo = true;
                            hide_photo_progressbar_1();
                            Artist1_Photos = response;
                            recycler1 = (RecyclerView) getView().findViewById(R.id.artist1_photos);
                            PhotoAdapter photoAdapter1 = new PhotoAdapter(Artist1_Photos, getContext());
                            recycler1.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recycler1.setAdapter(photoAdapter1);
                            if (Artist1_Photos.length() == 0){
                                NR1 = true;
                                TextView no_result_message = getView().findViewById(R.id.no_result_message_photo1);
                                no_result_message.setVisibility(View.VISIBLE);
                            }
                        }
                        if(i == 2){
                            pba2_photo = true;
                            hide_photo_progressbar_2();
                            Artist2_Photos = response;
                            recycler2 = getView().findViewById(R.id.artist2_photos);
                            PhotoAdapter photoAdapter2 = new PhotoAdapter(Artist2_Photos, getContext());
                            recycler2.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recycler2.setAdapter(photoAdapter2);
                            if (Artist2_Photos.length() == 0){
                                NR2 = true;
                                TextView no_result_message = getView().findViewById(R.id.no_result_message_photo2);
                                no_result_message.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");
                        // TODO: Handle error
                        //Toast.makeText(getActivity(), "error" ,Toast.LENGTH_LONG).show();
                        if (i == 1){
                            pba1_photo = true;
                            hide_photo_progressbar_1();
                            NR1 = true;
                            TextView no_result_message = getView().findViewById(R.id.no_result_message_photo1);
                            no_result_message.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "error! can't find artist's photo" ,Toast.LENGTH_LONG).show();
                        }
                        if(i == 2){
                            pba2_photo = true;
                            hide_photo_progressbar_2();
                            NR2 = true;
                            TextView no_result_message = getView().findViewById(R.id.no_result_message_photo2);
                            no_result_message.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "error! can't find artist's photo" ,Toast.LENGTH_LONG).show();
                        }
                    }
                });
        queue.add(photos);

    }

    private void hide_photo_progressbar_1() {
        try {
            RelativeLayout progressbar = getView().findViewById(R.id.searchingphoto_artist1);
            progressbar.setVisibility(View.GONE);
        }catch (NullPointerException e){
            //hide_photo_progressbar_1();
        }
    }
    private void hide_photo_progressbar_2() {
        try {
            RelativeLayout progressbar = getView().findViewById(R.id.searchingphoto_artist2);
            progressbar.setVisibility(View.GONE);
        }catch (NullPointerException e){
           // hide_photo_progressbar_1();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_artist, container, false);

        recycler1 = rootView.findViewById(R.id.artist1_photos);
        PhotoAdapter photoAdapter1 = new PhotoAdapter(Artist1_Photos, getContext());
        recycler1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler1.setAdapter(photoAdapter1);

        recycler2 = rootView.findViewById(R.id.artist2_photos);
        PhotoAdapter photoAdapter2 = new PhotoAdapter(Artist2_Photos, getContext());
        recycler2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler2.setAdapter(photoAdapter2);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //hello = (TextView) getView().findViewById(R.id.textView3);
        //hello.setText(segment_id+","+artist1+","+artist2);

        atris1_name0 = getView().findViewById(R.id.atris1_name0);
        atris1_name0.setText(artist1);
        settable(Artist1_Spotify,1);

        atris2_name0 = getView().findViewById(R.id.atris2_name0);
        atris2_name0.setText(artist2);
        settable(Artist2_Spotify,2);

        if (pba1_spotify){
            RelativeLayout progressbar = getView().findViewById(R.id.searchingspotify_artist1);
            progressbar.setVisibility(View.GONE); }
        if (pba2_spotify){
            RelativeLayout progressbar = getView().findViewById(R.id.searchingspotify_artist2);
            progressbar.setVisibility(View.GONE); }
        if (pba1_photo){
            RelativeLayout progressbar = getView().findViewById(R.id.searchingphoto_artist1);
            progressbar.setVisibility(View.GONE); }
        if (pba2_photo){
            RelativeLayout progressbar = getView().findViewById(R.id.searchingphoto_artist2);
            progressbar.setVisibility(View.GONE); }

        if(NR1){
            TextView no_result_message = getView().findViewById(R.id.no_result_message_photo1);
            no_result_message.setVisibility(View.VISIBLE);
        }
        if(NR2){
            TextView no_result_message = getView().findViewById(R.id.no_result_message_photo2);
            no_result_message.setVisibility(View.VISIBLE);
        }

        if (SEARCH){
            SEARCH = false;

            pba1_spotify = true;
            pba2_spotify = true;
            if(  new String(this.segment_id).equals("KZFzniwnSyZfZ7v7nJ" ) ) {
                //Toast.makeText(getActivity(), "search artist" ,Toast.LENGTH_LONG).show();
                pba1_spotify = false;
                callSpotifyAPI(artist1,1);
                if ( ! new String(artist2).equals("") ){
                    pba2_spotify = false;
                    callSpotifyAPI(artist2,2);}
            }

            pba1_photo = false;
            pba2_photo = true;
            callCustomAPI(artist1,1);
            if ( ! new String(artist2).equals("") ){
                pba2_photo = false;
                callCustomAPI(artist2,2);}

        }






    }

    private void settable(JSONObject Artist_Spotify, int i){

        TextView atris_name = getView().findViewById(R.id.atris1_name);
        TextView atris_follower = getView().findViewById(R.id.atris1_follower);
        TextView atris_popilarity = getView().findViewById(R.id.atris1_popilarity);
        TextView atris_url = getView().findViewById(R.id.atris1_url);

        TableRow atris_name_row = getView().findViewById(R.id.atris1_name_row);
        TableRow atris_follower_row = getView().findViewById(R.id.atris1_follower_row);
        TableRow atris_popilarity_row = getView().findViewById(R.id.atris1_popilarity_row);
        TableRow atris_url_row = getView().findViewById(R.id.atris1_url_row);

        if (i == 2){
            atris_name = getView().findViewById(R.id.atris2_name);
            atris_follower = getView().findViewById(R.id.atris2_follower);
            atris_popilarity = getView().findViewById(R.id.atris2_popilarity);
            atris_url = getView().findViewById(R.id.atris2_url);

            atris_name_row = getView().findViewById(R.id.atris2_name_row);
            atris_follower_row = getView().findViewById(R.id.atris2_follower_row);
            atris_popilarity_row = getView().findViewById(R.id.atris2_popilarity_row);
            atris_url_row = getView().findViewById(R.id.atris2_url_row);
        }


        try {
            String temp = Artist_Spotify.getString("name");
            atris_name.setText(temp);
            atris_name_row.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            atris_name_row.setVisibility(View.GONE);
        }

        try {
            String temp = Artist_Spotify.getJSONObject("followers").getString("total");
            DecimalFormat DF = new DecimalFormat("###,###");
            temp = DF.format(parseFloat(temp));
            atris_follower.setText(temp);
            atris_follower_row.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            atris_follower_row.setVisibility(View.GONE);
        }

        try {
            String temp = Artist_Spotify.getString("popularity");
            atris_popilarity.setText(temp);
            atris_popilarity_row.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            atris_popilarity_row.setVisibility(View.GONE);
        }

        try {
            final String url = Artist_Spotify.getJSONObject("external_urls").getString("spotify");
            atris_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }}});
            atris_url_row.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            atris_url_row.setVisibility(View.GONE);
        }

    }

}