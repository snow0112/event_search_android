package com.example.snow.csci571hw9;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.geohash.GeoHash;

public class SearchResultActivity extends AppCompatActivity {


    private String forminputs;
    private RecyclerView event;
    public static SharedPreferences eventlistshare;
    public static SharedPreferences.Editor eventshareeditor;
    private JSONArray Events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);

        Intent inputsource = getIntent();
        forminputs = inputsource.getStringExtra("forminputs");
        RelativeLayout pb = (RelativeLayout) findViewById(R.id.searchingevents);
        pb.setVisibility(View.VISIBLE);

        eventlistshare = getSharedPreferences("eventlist", MODE_PRIVATE);
        eventshareeditor = eventlistshare.edit();

        // Recycler view
        event = (RecyclerView) findViewById(R.id.eventslist);
        event.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        event.setLayoutManager(mLayoutManager);

        String url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/event-search/" + forminputs;
        Log.d("132", String.valueOf(url));
        //hello.setText(url);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest eventsearch = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hello.setText("response get");
                        RelativeLayout pb = (RelativeLayout) findViewById(R.id.searchingevents);
                        pb.setVisibility(View.INVISIBLE);
                        try {
                            Events = response.getJSONObject("_embedded").getJSONArray("events");
                            AddEventsToSharedPreference();
                            //hello.setText(Events.toString());
                            if (Events.length() == 0 ){
                                RelativeLayout no_result_message = findViewById(R.id.no_result_message_search);
                                no_result_message.setVisibility(View.VISIBLE);
                            }
                            else{
                                RelativeLayout no_result_message = findViewById(R.id.no_result_message_search);
                                no_result_message.setVisibility(View.GONE);
                            EventAdapter eventAdapter = new EventAdapter(Events, getApplicationContext(),1);
                            event.setAdapter(eventAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            RelativeLayout no_result_message = findViewById(R.id.no_result_message_search);
                            no_result_message.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");
                        RelativeLayout pb = (RelativeLayout) findViewById(R.id.searchingevents);
                        pb.setVisibility(View.INVISIBLE);
                        RelativeLayout no_result_message = findViewById(R.id.no_result_message_search);
                        no_result_message.setVisibility(View.VISIBLE);
                        ERRORtoast();
                    }
                });
        queue.add(eventsearch);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


    }

    public void AddEventsToSharedPreference(){
        String event_string = new String();
        event_string = Events.toString() ;
        eventshareeditor.putString("Events",event_string).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        String event_string = getSharedPreferences("eventlist", MODE_PRIVATE).getString("Events","nono");
        JSONArray Events_resume = new JSONArray();
        try {
            Events_resume = new JSONArray(event_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        event = (RecyclerView) findViewById(R.id.eventslist);
        event.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        event.setLayoutManager(mLayoutManager);
        EventAdapter eventAdapter = new EventAdapter(Events_resume, getApplicationContext(),1);
        event.setAdapter(eventAdapter);

    }

    public void ERRORtoast(){
        Toast.makeText(this, "error" ,Toast.LENGTH_LONG).show();
    }


}
