package com.example.lutemonfighter.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lutemonfighter.Lutemon;
import com.example.lutemonfighter.StatsViewAdapter;
import com.example.lutemonfighter.R;
import com.example.lutemonfighter.Storage;

import java.util.ArrayList;
import java.util.Map;

public class StatisticsFragment extends Fragment {

    Storage storage = Storage.getInstance();
    StatsViewAdapter statsViewAdapter;
    RecyclerView statsRecyclerView;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        RecyclerView statsRecyclerView = view.findViewById(R.id.statsRecyclerView);
        statsViewAdapter = new StatsViewAdapter(getContext(), getLutemonList());
        statsRecyclerView.setAdapter(statsViewAdapter);
        statsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (statsViewAdapter.getItemCount() != getLutemonList().size()) {
            RecyclerView statsRecyclerView = getView().findViewById(R.id.statsRecyclerView);
            statsViewAdapter = new StatsViewAdapter(getContext(), getLutemonList());
            statsRecyclerView.setAdapter(statsViewAdapter);
            statsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            statsViewAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<Lutemon> getLutemonList() {
        ArrayList<Lutemon> lutemons = new ArrayList<>();
        for (Map.Entry<Integer, Lutemon> entry : storage.getLutemonMap().entrySet()) {
            lutemons.add(entry.getValue());
        }
        return lutemons;
    }
}