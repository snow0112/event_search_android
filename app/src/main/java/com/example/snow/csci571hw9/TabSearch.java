package com.example.snow.csci571hw9;

import android.net.wifi.aware.PublishConfig;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;


public class TabSearch extends Fragment implements AdapterView.OnItemSelectedListener{

    public String key, cate, seg, rad, unitt, fromm, speclox;

    public AutoCompleteTextView keyword;
    public Spinner category;
    public EditText radius;
    public Spinner unit;
    public RadioButton here;
    public RadioButton there;
    public RadioGroup from;
    public EditText specifylocation;
    public Button search;
    public Button clear;



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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = keyword.getText().toString();
                cate = category.getSelectedItem().toString();
                seg = segmentId();

                rad = radius.getText().toString();
                unitt = unit.getSelectedItem().toString();
                int selectedid = from.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) getView().findViewById(selectedid);
                fromm = radioButton.getText().toString();
                speclox = specifylocation.getText().toString();
                Toast.makeText(getActivity(),rad ,Toast.LENGTH_SHORT).show();
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
