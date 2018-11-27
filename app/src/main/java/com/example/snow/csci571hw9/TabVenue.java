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
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TabVenue extends Fragment implements OnMapReadyCallback {
    private  String venuename;
    private TextView name, address, city, phone, hours, rule, childrule;
    private TableRow row_name, row_address, row_city, row_phone, row_hours, row_rule, row_childrule;
    private JSONObject VENUE = new JSONObject();
    private String lat, lon;


    public TabVenue() {
    }

    @SuppressLint("ValidFragment")
    public TabVenue(String venuename) {
        this.venuename = venuename;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = null;
        try {
            url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/detail-venue/"+URLEncoder.encode( venuename,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest addresslocation = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VENUE = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setName(VENUE);
                        setAddress(VENUE);
                        setCity(VENUE);
                        setPhone(VENUE);
                        setHours(VENUE);
                        setRule(VENUE);
                        setChildrule(VENUE);

                        setlocation(VENUE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","Volley Error");
                        // TODO: Handle error
                    }
                });
        queue.add(addresslocation);

        //MapFragment m = new MapFragment();

        //SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_venue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.name = getView().findViewById(R.id.venue_name);
        address = getView().findViewById(R.id.venue_address);
        city = getView().findViewById(R.id.venue_city);
        phone = getView().findViewById(R.id.venue_phone);
        hours = getView().findViewById(R.id.venue_hours);
        rule = getView().findViewById(R.id.venue_rule);
        childrule = getView().findViewById(R.id.venue_child);

        this.row_name = getView().findViewById(R.id.row_venue_name);
        row_address = getView().findViewById(R.id.row_venue_address);
        row_city = getView().findViewById(R.id.row_venue_city);
        row_phone = getView().findViewById(R.id.row_venue_phone);
        row_hours = getView().findViewById(R.id.row_venue_hours);
        row_rule = getView().findViewById(R.id.row_venue_rule);
        row_childrule = getView().findViewById(R.id.row_venue_child);

        setName(VENUE);
        setAddress(VENUE);
        setCity(VENUE);
        setPhone(VENUE);
        setHours(VENUE);
        setRule(VENUE);
        setChildrule(VENUE);

        setlocation(VENUE);

    }

    private void setName(JSONObject VENUE){
        try {
            String temp = VENUE.getString("name");
            name.setText(temp);
            row_name.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_name.setVisibility(View.GONE);
        }
    }
    private void setAddress(JSONObject VENUE){
        try {
            String temp = VENUE.getJSONObject("address").getString("line1");
            address.setText(temp);
            row_address.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_address.setVisibility(View.GONE);
        }
    }
    private void setCity(JSONObject VENUE){

        Boolean citydataexist = false;
        Boolean statedataexist = false;
        String temp_city = new String(), temp_state = new String();
        try {
            temp_city = VENUE.getJSONObject("city").getString("name");
            citydataexist = true;
        } catch (JSONException e){ }
        try {
            temp_state = VENUE.getJSONObject("state").getString("name");
            statedataexist = true;
        } catch (JSONException e){ }
        String temp = new String();
        if (citydataexist && statedataexist){
            temp = temp_city + ", "+ temp_state;
            row_city.setVisibility(View.VISIBLE);
        }
        if (citydataexist && !statedataexist){
            temp = temp_city;
            row_city.setVisibility(View.VISIBLE);
        }
        if (!citydataexist && statedataexist){
            temp = temp_state;
            row_city.setVisibility(View.VISIBLE);
        }
        if (!citydataexist && !statedataexist){
            row_city.setVisibility(View.GONE);
        }
        city.setText(temp);
    }
    private void setPhone(JSONObject VENUE){
        try {
            String temp = VENUE.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");
            phone.setText(temp);
            row_phone.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_phone.setVisibility(View.GONE);
        }
    }
    private void setHours(JSONObject VENUE){
        try {
            String temp = VENUE.getJSONObject("boxOfficeInfo").getString("openHoursDetail");
            hours.setText(temp);
            row_hours.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_hours.setVisibility(View.GONE);
        }
    }
    private void setRule(JSONObject VENUE){
        try {
            String temp = VENUE.getJSONObject("generalInfo").getString("generalRule");
            rule.setText(temp);
            row_rule.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_rule.setVisibility(View.GONE);
        }
    }
    private void setChildrule(JSONObject VENUE){
        try {
            String temp = VENUE.getJSONObject("generalInfo").getString("childRule");
            childrule.setText(temp);
            row_childrule.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            row_childrule.setVisibility(View.GONE);
        }
    }

    private void setlocation(JSONObject VENUE){
        try {
            String lat = VENUE.getJSONObject("location").getString("latitude");
            String lon = VENUE.getJSONObject("location").getString("longitude");
            String temp = "lat = " + lat + ", lon = " + lon;
            TextView location = getView().findViewById(R.id.venue_location);
            location.setText(temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}