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
    private TextView hello;
    private RecyclerView event;
    public SharedPreferences favolist;
    public static SharedPreferences.Editor faveditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);



        Intent inputsource = getIntent();
        forminputs = inputsource.getStringExtra("forminputs");
        hello = (TextView) findViewById(R.id.text);
        RelativeLayout pb = (RelativeLayout) findViewById(R.id.searchingevents);
        pb.setVisibility(View.VISIBLE);


        favolist = getSharedPreferences("favoritelist", MODE_PRIVATE);
        faveditor = favolist.edit();

        //progress


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
                            JSONArray Events = response.getJSONObject("_embedded").getJSONArray("events");
                            //hello.setText(Events.toString());
                            if (Events.length() == 0 ){
                                TextView noresult = (TextView) findViewById(R.id.eventnoresult);
                            }
                            else{
                            EventAdapter eventAdapter = new EventAdapter(Events, getApplicationContext());
                            event.setAdapter(eventAdapter);}

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ERRORtoast();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");
                        ERRORtoast();
                    }
                });
        queue.add(eventsearch);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


    }

    public void ERRORtoast(){
        Toast.makeText(this, "error" ,Toast.LENGTH_LONG).show();
    }


}
