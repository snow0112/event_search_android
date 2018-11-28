package com.example.snow.csci571hw9;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.aware.PublishConfig;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ObjectInputValidation;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import ch.hsr.geohash.GeoHash;


public class TabSearch extends Fragment implements AdapterView.OnItemSelectedListener {

    public String key, cate, seg, rad, unitt, fromm, speclox;
    public double currentlat, currentlon;
    public String forminputs;
    public FusedLocationProviderClient mFusedLocationClient;

    public AutoCompleteTextView keyword;
    public List<String> autooptions;

    public Spinner category;
    public EditText radius;
    public Spinner unit;
    public RadioButton here;
    public RadioButton there;
    public RadioGroup from;
    public EditText specifylocation;
    public Button search;
    public Button clear;
    public String currentgeohasg,  specifgeohasg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentlat = location.getLatitude();
                            currentlon = location.getLongitude();
                            currentgeohasg = GeoHash.geoHashStringWithCharacterPrecision(currentlat,currentlon,9);
                            //Log.d("Geo",hash);
                        }
                    }
                });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_search , container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    Spinner catspinner = (Spinner) getView().findViewById(R.id.category);
    ArrayAdapter<CharSequence> catadapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories, android.R.layout.simple_spinner_item);
    catadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    catspinner.setAdapter(catadapter);

    Spinner unitspinner = (Spinner) getView().findViewById(R.id.unit);
    ArrayAdapter<CharSequence> unitadapter = ArrayAdapter.createFromResource(getActivity(), R.array.units, android.R.layout.simple_spinner_item);
    unitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    unitspinner.setAdapter(unitadapter);

    keyword = (AutoCompleteTextView) getView().findViewById(R.id.keyword);

    category = (Spinner) getView().findViewById(R.id.category);
    radius = (EditText) getView().findViewById(R.id.radius);
    unit = (Spinner) getView().findViewById(R.id.unit);

    here = (RadioButton) getView().findViewById(R.id.here);
    there = (RadioButton) getView().findViewById(R.id.there);
    from = (RadioGroup) getView().findViewById(R.id.from);
    specifylocation = (EditText) getView().findViewById(R.id.specifylocation);
    search = (Button) getView().findViewById(R.id.search);
    clear = (Button) getView().findViewById(R.id.clear);

    clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword.getText().clear();
                radius.getText().clear();
                specifylocation.getText().clear();
                specifylocation.setEnabled(false);
                here.setChecked(true);
                there.setChecked(false);
                category.setSelection(0);
                unit.setSelection(0);
                TextView keyword_validation = getView().findViewById(R.id.keyword_validation);
                keyword_validation.setVisibility(View.GONE);
                TextView specifylocation_validation = getView().findViewById(R.id.specifylocation_validation);
                specifylocation_validation.setVisibility(View.GONE);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView keyword_validation = getView().findViewById(R.id.keyword_validation);
                keyword_validation.setVisibility(View.GONE);
                TextView specifylocation_validation = getView().findViewById(R.id.specifylocation_validation);
                specifylocation_validation.setVisibility(View.GONE);

                key = keyword.getText().toString();
                try {
                    key = URLEncoder.encode(key,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                cate = category.getSelectedItem().toString();
                seg = segmentId();

                rad = radius.getText().toString();
                String unittemp = unit.getSelectedItem().toString();
                if ( new String(unittemp).equals("Miles") ){unitt = "miles";}else {unitt = "km";}
                int selectedid = from.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) getView().findViewById(selectedid);
                fromm = radioButton.getText().toString();
                speclox = specifylocation.getText().toString();
                //Toast.makeText(getActivity(), autooptions.get(0) ,Toast.LENGTH_SHORT).show();


                if ( new String(fromm).equals("Other, Specify Location") ){
                    //validation check for both key and location
                    if (new String(key).equals("") || key == null || new String(speclox).equals("") || speclox == null){
                        Toast.makeText(getActivity(), "Please fix all fields with errors" ,Toast.LENGTH_SHORT).show();
                        if (new String(key).equals("") || key == null){
                            keyword_validation.setVisibility(View.VISIBLE);}
                        if (new String(speclox).equals("") || speclox == null){
                            specifylocation_validation.setVisibility(View.VISIBLE);}
                        return;
                    }


                    try {
                        String url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/address-find/" +URLEncoder.encode(speclox,"UTF-8") ;
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        JsonObjectRequest addresslocation = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONObject loc = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                                            double specifylat = loc.getDouble("lat");
                                            double specifylon = loc.getDouble("lng");
                                            specifgeohasg = GeoHash.geoHashStringWithCharacterPrecision(specifylat,specifylon,9);
                                            forminputs = key +"&segmentId="+ seg + "&radius="+ rad +"&unit="+ unitt +"&geoPoint=" + specifgeohasg ;
                                            //Toast.makeText(getActivity(), forminputs ,Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("error","Volley Error");
                                        Toast.makeText(getActivity(), "error! can't get geohash from specified locaiotn" ,Toast.LENGTH_LONG).show();
                                    }
                                });
                        queue.add(addresslocation);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else{
                    // validation check for keyword
                    if (new String(key).equals("") || key == null){
                        Toast.makeText(getActivity(), "Please fix all fields with errors" ,Toast.LENGTH_SHORT).show();
                        keyword_validation.setVisibility(View.VISIBLE);
                        return;
                    }
                    forminputs = key +"&segmentId="+ seg + "&radius="+ rad +"&unit="+ unitt +"&geoPoint=" + currentgeohasg ;
                }

                // open nwe activity
                Intent searchresult;
                searchresult = new Intent(getActivity(), SearchResultActivity.class);
                searchresult.putExtra("forminputs",forminputs);
                startActivity (searchresult);
            }
        });

        specifylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView keyword_validation = getView().findViewById(R.id.specifylocation_validation);
                keyword_validation.setVisibility(View.GONE);
            }
        });

        keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView keyword_validation = getView().findViewById(R.id.keyword_validation);
                keyword_validation.setVisibility(View.GONE);
            }
        });

        here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specifylocation.setEnabled(false);
                specifylocation.getText().clear();
                TextView keyword_validation = getView().findViewById(R.id.specifylocation_validation);
                keyword_validation.setVisibility(View.GONE);
            }
        });

        there.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specifylocation.setEnabled(true);
            }
        });

        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                key = keyword.getText().toString();
                try {
                    String url = "http://csci571snowhw8.us-east-2.elasticbeanstalk.com/auto-complete/" + URLEncoder.encode(key,"UTF-8");
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    JsonObjectRequest addresslocation = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray attractions = response.getJSONObject("_embedded").getJSONArray("attractions");
                                        autooptions = new ArrayList<String>() ;
                                        for ( int i = 0; i < attractions.length() ; i++) {
                                            //attractions.getJSONObject(i).getString("name");
                                            autooptions.add( attractions.getJSONObject(i).getString("name") );
                                        }
                                        autooptions.toArray();
                                        ArrayAdapter<String> adapter_autocomplete = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line , autooptions);
                                        keyword.setAdapter(adapter_autocomplete);
                                    } catch (JSONException e) {
                                        e.printStackTrace(); }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("error","Volley Error");
                                    // TODO: Handle error

                                }
                            });
                    queue.add(addresslocation);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public String segmentId(){

        if ( new String(cate).equals("All") ){ return ""; }
        if ( new String(cate).equals("Music") ){ return "KZFzniwnSyZfZ7v7nJ"; }
        if ( new String(cate).equals("Sports")){ return "KZFzniwnSyZfZ7v7nE"; }
        if ( new String(cate).equals("Arts")){ return "KZFzniwnSyZfZ7v7na"; }
        if ( new String(cate).equals("Theater")){ return "KZFzniwnSyZfZ7v7na"; }
        if ( new String(cate).equals("Film")){ return "KZFzniwnSyZfZ7v7nn"; }
        if ( new String(cate).equals("Miscellaneous")){ return "KZFzniwnSyZfZ7v7n1"; }
        return "";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }


}
