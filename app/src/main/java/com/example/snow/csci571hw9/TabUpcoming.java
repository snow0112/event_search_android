package com.example.snow.csci571hw9;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TabUpcoming extends Fragment  {
    private  String venuename;
    private JSONArray UpcomingEvents = new JSONArray();
    private Spinner sortspinner, ascendspinner;
    private RecyclerView recycler;

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

        String url = null;
        url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/songkick-upcoming/"+venue_id;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest upcomingevents = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        UpcomingEvents = response;
                        recycler = (RecyclerView) getView().findViewById(R.id.upcominglist);
                        UpcomingAdapter upcomingAdapter = new UpcomingAdapter(UpcomingEvents, getContext());
                        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recycler.setAdapter(upcomingAdapter);
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
        View rootView = inflater.inflate(R.layout.tab_upcoming, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.upcominglist);
        UpcomingAdapter upcomingAdapter = new UpcomingAdapter(UpcomingEvents, getContext());
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(upcomingAdapter);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sortspinner = (Spinner) getView().findViewById(R.id.sortby);
        ArrayAdapter<CharSequence> sortadapter = ArrayAdapter.createFromResource(getActivity(), R.array.sortby, android.R.layout.simple_spinner_item);
        sortadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortspinner.setAdapter(sortadapter);

        ascendspinner = (Spinner) getView().findViewById(R.id.acend);
        ArrayAdapter<CharSequence> ascendadapter = ArrayAdapter.createFromResource(getActivity(), R.array.acend, android.R.layout.simple_spinner_item);
        ascendadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ascendspinner.setAdapter(ascendadapter);
        ascendspinner.setEnabled(false);

        sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        recycler = getView().findViewById(R.id.upcominglist);
                        UpcomingAdapter upcomingAdapter = new UpcomingAdapter(UpcomingEvents, getContext());
                        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recycler.setAdapter(upcomingAdapter);
                        ascendspinner.setEnabled(false);
                        break;
                    case 1:
                        try {
                            this.SortByName();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ascendspinner.setEnabled(true);
                        break;
                    case 2:
                        ascendspinner.setEnabled(true);
                        break;
                    case 3:
                        ascendspinner.setEnabled(true);
                        break;
                    case 4:
                        ascendspinner.setEnabled(true);
                        break;
                    case 5:
                        ascendspinner.setEnabled(true);
                        break;
                }

            }

            public void SortByName() throws JSONException {

                List<JSONObject> Upcomings = new ArrayList<JSONObject>();
                for(int i = 0; i <UpcomingEvents.length(); i++){
                    Upcomings.add(UpcomingEvents.getJSONObject(i));
                }

                final String order =ascendspinner.getSelectedItem().toString();

                Collections.sort(Upcomings, new Comparator<JSONObject>(
                ) {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        String val1 = new String();
                        String val2 = new String();
                        try {
                            val1 = (String) o1.getString("displayName");
                            val2 = (String) o2.getString("displayName");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (new String(order).equals("Ascending" )){
                            return val1.compareTo(val2);
                        }else{
                            return -val1.compareTo(val2);
                        }

                    }
                });

                JSONArray sortedUpcomingEvents = new JSONArray();
                for (int i = 0; i < UpcomingEvents.length(); i++ ){
                    sortedUpcomingEvents.put(Upcomings.get(i));
                }

                recycler = getView().findViewById(R.id.upcominglist);
                UpcomingAdapter upcomingAdapter = new UpcomingAdapter(sortedUpcomingEvents, getContext());
                recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                recycler.setAdapter(upcomingAdapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ascendspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



}