package com.example.snow.csci571hw9;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TabUpcoming extends Fragment {
    private  String venuename;
    private TextView hello;

    public TabUpcoming() {
        this.venuename = new String();
    }

    @SuppressLint("ValidFragment")
    public TabUpcoming (String venuename) {
        this.venuename = venuename;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String url = null;
        try {
            url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/songkick-venueID/"+URLEncoder.encode( venuename,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest venueinformation = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hello.setText(response.toString());
                        call2dnAPI( response );
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");

                    }
                });
        queue.add(venueinformation);


    }

    public void call2dnAPI( JSONArray venueinfo ){
        String venue_id = new String();
        try {
            venue_id = venueinfo.getJSONObject(0).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hello.setText(venue_id);

        String url = null;
        url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/songkick-upcoming/"+venue_id;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest upcomingevents = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hello.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");
                        // TODO: Handle error
                    }
                });
        queue.add(upcomingevents);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_upcoming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hello = (TextView) getView().findViewById(R.id.textView2);
        hello.setText(venuename);
    }
}