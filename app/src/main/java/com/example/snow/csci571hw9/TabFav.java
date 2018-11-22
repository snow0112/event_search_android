package com.example.snow.csci571hw9;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFav extends Fragment {

    public SharedPreferences favolist;
    public static SharedPreferences.Editor faveditor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_favorite, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        favolist = getActivity().getSharedPreferences("favoritelist",getActivity().MODE_PRIVATE);
        faveditor = favolist.edit();

    }
}
