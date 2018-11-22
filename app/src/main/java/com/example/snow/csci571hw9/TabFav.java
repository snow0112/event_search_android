package com.example.snow.csci571hw9;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabFav extends Fragment {

    public static SharedPreferences favolist;
    public static SharedPreferences.Editor faveditor;
    private Map<String, ?> FavStringList;
    private JSONObject Event;
    private List<JSONObject> favlist = new ArrayList<>();
    private JSONArray FavoList;
    private Context context;
    private RecyclerView recycler;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_favorite, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.favoriterecycle);
        EventAdapter eventAdapter = new EventAdapter(FavoList, getContext());
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(eventAdapter);

        return rootView;}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        favolist = getActivity().getSharedPreferences("favoritelist",getActivity().MODE_PRIVATE);
        faveditor = favolist.edit();
        FavStringList = favolist.getAll();

        int i = 0;
        for (String key : FavStringList.keySet()){
            String event = getActivity().getSharedPreferences("favoritelist", getActivity().MODE_PRIVATE).getString(key,"nono");
            try {
                Event = new JSONObject(event);
                favlist.add(Event);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*
            String name = new String();
            try {
                name = Event.getString("name");
                Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
        FavoList = new JSONArray(favlist);

        /*
        String name = new String();
        try {
            name = FavoList.getJSONObject(0).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();*/

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       //TextView hello = (TextView) getActivity().findViewById(R.id.favlist);
       // hello.setText(FavStringList.toString());

        // Recycler view
        //event = (RecyclerView) getActivity().findViewById(R.id.favoriterecycle);
        //event.setLayoutManager(new LinearLayoutManager(getActivity()));
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //event.setLayoutManager(mLayoutManager);


        //EventAdapter eventAdapter = new EventAdapter(FavoList, getActivity().getApplicationContext());
        //event.setAdapter(eventAdapter);}
    }
}
