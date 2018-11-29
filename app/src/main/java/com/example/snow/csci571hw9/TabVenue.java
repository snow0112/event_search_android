package com.example.snow.csci571hw9;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TabVenue extends Fragment implements OnMapReadyCallback {
    private String venuename;
    private TextView name, address, city, phone, hours, rule, childrule;
    private TableRow row_name, row_address, row_city, row_phone, row_hours, row_rule, row_childrule;
    private JSONObject VENUE = new JSONObject();
    private Boolean pb, NR;
    private MapView mMapView;
    private GoogleMap googleMap;
    private double lat;
    private double lon;
    private Boolean SEARCH;


    public TabVenue() {
    }

    @SuppressLint("ValidFragment")
    public TabVenue(String venuename) {
        this.venuename = venuename;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pb = false;
        NR = false;
        SEARCH = true;

        //MapFragment m = new MapFragment();

        //SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_venue, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setGoogleMap(42.64834, -73.753957);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void setGoogleMap(){
        mMapView.setVisibility(View.VISIBLE);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(lat, lon);
                googleMap.addMarker(new MarkerOptions().position(sydney).title(venuename).snippet(""));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
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

        if (pb){
            RelativeLayout progressbar = getView().findViewById(R.id.searchingvenue);
            progressbar.setVisibility(View.GONE);
        }
        if (NR){
            RelativeLayout no_result_message = getView().findViewById(R.id.no_result_message_venue);
            no_result_message.setVisibility(View.VISIBLE);
        }

        if (SEARCH){
            SEARCH = false;
            String url = null;
            try {
                url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/detail-venue/" + URLEncoder.encode(venuename, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(getContext());
            JsonObjectRequest addresslocation = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            pb = true;
                            RelativeLayout progressbar = getView().findViewById(R.id.searchingvenue);
                            progressbar.setVisibility(View.GONE);
                            try {
                                VENUE = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                NR = true;
                                RelativeLayout no_result_message = getView().findViewById(R.id.no_result_message_venue);
                                no_result_message.setVisibility(View.VISIBLE);
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
                            pb = true;
                            RelativeLayout progressbar = getView().findViewById(R.id.searchingvenue);
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "error! can't find venue detail", Toast.LENGTH_LONG).show();
                            NR = true;
                            RelativeLayout no_result_message = getView().findViewById(R.id.no_result_message_venue);
                            no_result_message.setVisibility(View.VISIBLE);
                        }
                    });
            queue.add(addresslocation);
        }


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
            String lat_temp = VENUE.getJSONObject("location").getString("latitude");
            String lon_temp = VENUE.getJSONObject("location").getString("longitude");
            lat = Double.parseDouble(lat_temp);
            lon = Double.parseDouble(lon_temp);
            setGoogleMap();
        } catch (JSONException e) {
            mMapView.setVisibility(View.GONE);
            //Toast.makeText(getActivity(), "no location information", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        //LatLng sydney = new LatLng(-33.852, 151.211);
        //googleMap.addMarker(new MarkerOptions().position(sydney)
         //       .title("Marker in Sydney"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}