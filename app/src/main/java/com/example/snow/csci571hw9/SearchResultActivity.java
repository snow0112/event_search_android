package com.example.snow.csci571hw9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);

        Intent inputsource = getIntent();
        forminputs = inputsource.getStringExtra("forminputs");
        hello = (TextView) findViewById(R.id.text);


        String url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/event-search/" + forminputs;
        Log.d("132", String.valueOf(url));
        hello.setText(url);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest eventsearch = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hello.setText("response get");
                        try {
                            JSONArray Events = response.getJSONObject("_embedded").getJSONArray("events");
                            hello.setText(Events.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");
                        // TODO: Handle error
                    }
                });
        queue.add(eventsearch);


    }


}
