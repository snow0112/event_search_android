package com.example.snow.csci571hw9;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import static java.lang.Float.parseFloat;

public class TabEvent extends Fragment {
    private String event_id;
    private TextView atrist;
    private TextView venue;
    private TextView time;
    private TextView category;
    private TextView price;
    private TextView Tickerstatus;
    private TextView ticketmaster , seatmap;
    private TableRow row_artist,row_venue, row_time, row_category, row_price, row_Tickerstatus, row_ticketmaster, row_seatmap;
    private JSONObject EVENT = new JSONObject();
    private Boolean pb, NR;
    private Boolean SEARCH;


    public TabEvent() {
        this.event_id = new String();
    }

    @SuppressLint("ValidFragment")
    public TabEvent(String event_id) {
        this.event_id = event_id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SEARCH = true;
        NR = false;
        pb = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.tab_event, container, false);
        return V;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.atrist = getView().findViewById(R.id.event_artist);
        this.venue = getView().findViewById(R.id.event_venue);
        this.time = getView().findViewById(R.id.event_time);
        this.category = getView().findViewById(R.id.event_category);
        this.price = getView().findViewById(R.id.event_price);
        this.Tickerstatus = getView().findViewById(R.id.event_stauts);
        this.ticketmaster = getView().findViewById(R.id.event_ticketmaster);
        this.seatmap = getView().findViewById(R.id.event_seatmap);

        this.row_artist = getView().findViewById(R.id.event_artist_row);
        this.row_venue = getView().findViewById(R.id.event_venue_row);
        this.row_time = getView().findViewById(R.id.event_time_row);
        this.row_category = getView().findViewById(R.id.event_category_row);
        this.row_price = getView().findViewById(R.id.event_price_row);
        this.row_Tickerstatus = getView().findViewById(R.id.event_stauts_row);
        this.row_ticketmaster = getView().findViewById(R.id.event_ticketmaster_row);
        this.row_seatmap = getView().findViewById(R.id.event_seatmap_row);

        setartist(EVENT);
        setvenue(EVENT);
        settime(EVENT);
        setcategory(EVENT);
        setprice(EVENT);
        setstatus(EVENT);
        setticketmaster(EVENT);
        setseatmap(EVENT);

        if (pb){
            RelativeLayout progressbar = getView().findViewById(R.id.searchingdetails);
            progressbar.setVisibility(View.GONE);
        }

        if(NR){
            NR = true;
            RelativeLayout no_result_message = getView().findViewById(R.id.no_result_message_event);
            no_result_message.setVisibility(View.VISIBLE);
        }

        if (SEARCH){
            SEARCH = false;
            String url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/detail-event/" +event_id ;
            RequestQueue queue = Volley.newRequestQueue(getContext());
            JsonObjectRequest addresslocation = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            pb = true;
                            RelativeLayout progressbar = getView().findViewById(R.id.searchingdetails);
                            progressbar.setVisibility(View.GONE);
                            EVENT = response;
                            setartist(EVENT);
                            setvenue(EVENT);
                            settime(EVENT);
                            setcategory(EVENT);
                            setprice(EVENT);
                            setstatus(EVENT);
                            setticketmaster(EVENT);
                            setseatmap(EVENT);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error","Volley Error");
                            // TODO: Handle error
                            pb = true;
                            RelativeLayout progressbar = getView().findViewById(R.id.searchingdetails);
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "error" ,Toast.LENGTH_LONG).show();
                            NR = true;
                            RelativeLayout no_result_message = getView().findViewById(R.id.no_result_message_event);
                            no_result_message.setVisibility(View.VISIBLE);
                        }
                    });
            queue.add(addresslocation);


        }

    }

    private void setartist(JSONObject EVENT){
        try {
            String temp = "";
            JSONArray Attractions = EVENT.getJSONObject("_embedded").getJSONArray("attractions");
            for (int i = 0; i < Attractions.length(); i++ ){
                try {
                    String name = Attractions.getJSONObject(i).getString("name");
                    temp += name;
                    if (i < Attractions.length() -1){
                        temp += " | ";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            atrist.setText(temp);
            row_artist.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_artist.setVisibility(View.GONE);
        }
    }
    private void setvenue(JSONObject EVENT){
        try {
            String temp = EVENT.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
            venue.setText(temp);
            row_venue.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_venue.setVisibility(View.GONE);
        }
    }
    private void settime(JSONObject EVENT){
        try {
            String temp_date = EVENT.getJSONObject("dates").getJSONObject("start").getString("localDate");
            try {
                temp_date = new SimpleDateFormat("MMM dd, yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(temp_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //temp_date = Date.format(temp_date);

            String temp_time = EVENT.getJSONObject("dates").getJSONObject("start").getString("localTime");
            String temp = temp_date + "  " + temp_time;
            time.setText(temp);
            row_time.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_time.setVisibility(View.GONE);
        }
    }
    private void setcategory(JSONObject EVENT){
        try {
            String temp = "";
            String segment =  EVENT.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
            temp += segment;
            try {
                String genre =  EVENT.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
                temp += " | " + genre;
            }catch (JSONException e){
                e.printStackTrace();
            }
            category.setText(temp);
            row_category.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_category.setVisibility(View.GONE);
        }
    }
    private void setprice(JSONObject EVENT){
        try {
            String temp = "";
            String min =  EVENT.getJSONArray("priceRanges").getJSONObject(0).getString("min");
            String max =  EVENT.getJSONArray("priceRanges").getJSONObject(0).getString("max");
            DecimalFormat DF = new DecimalFormat("###,##0.00");
            min = DF.format(parseFloat(min));
            max = DF.format(parseFloat(max));
            temp = "$" + min + " ~ $" +max;
            price.setText(temp);
            row_price.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_price.setVisibility(View.GONE);
        }
    }
    private void setstatus(JSONObject EVENT){
        try {
            String temp = EVENT.getJSONObject("dates").getJSONObject("status").getString("code");
            Tickerstatus.setText(temp);
            row_Tickerstatus.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_Tickerstatus.setVisibility(View.GONE);
        }
    }
    private void setticketmaster(JSONObject EVENT){
        try {
            final String url = this.EVENT.getString("url");
            ticketmaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
            row_ticketmaster.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_ticketmaster.setVisibility(View.GONE);
        }
    }
    private void setseatmap(JSONObject EVENT){
        try {
            final String url = this.EVENT.getJSONObject("seatmap").getString("staticUrl");
            seatmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
            row_seatmap.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_seatmap.setVisibility(View.GONE);
        }
    }

}