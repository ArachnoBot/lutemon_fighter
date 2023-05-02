package com.example.lutemonfighter.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.lutemonfighter.Lutemon;
import com.example.lutemonfighter.Storage;
import com.example.lutemonfighter.MainActivity;
import com.example.lutemonfighter.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class StartScreenFragment extends Fragment {
    Storage storage = Storage.getInstance();
    public StartScreenFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_screen, container, false);

        Button playBtn = view.findViewById(R.id.playBtn);
        Button loadBtn = view.findViewById(R.id.loadBtn);
        Button saveBtn = view.findViewById(R.id.saveBtn);
        Button exitBtn = view.findViewById(R.id.exitBtn);
        Button howToPlayBtn = view.findViewById(R.id.howToPlayBtn);

        playBtn.setOnClickListener(listener);
        loadBtn.setOnClickListener(listener);
        saveBtn.setOnClickListener(listener);
        exitBtn.setOnClickListener(listener);
        howToPlayBtn.setOnClickListener(listener);

        return view;
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String gameFragTag = "gameFragTag";
            String startScreenFragTag = "startScreenFragTag";
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            StartScreenFragment startScreenFragment = (StartScreenFragment) getParentFragmentManager().findFragmentByTag(startScreenFragTag);
            GameFragment gameFragment = (GameFragment) getParentFragmentManager().findFragmentByTag(gameFragTag);

            assert startScreenFragment != null;
            switch (view.getId()) {
                case R.id.playBtn:
                    if (getParentFragmentManager().findFragmentByTag(gameFragTag) == null) {
                        ft.add(R.id.mainFrame, new GameFragment(), gameFragTag);
                        ft.hide(startScreenFragment);
                    } else {
                        assert gameFragment != null;
                        ft.show(gameFragment);
                        ft.hide(startScreenFragment);
                    }
                    ft.commit();
                    break;

                case R.id.loadBtn:
                    try {
                        FileInputStream file = requireContext().openFileInput("saved_lutemons.ser");
                        ObjectInputStream in = new ObjectInputStream(file);
                        HashMap<Integer, Lutemon> lutemonMap = (HashMap<Integer, Lutemon>) in.readObject();
                        storage.setLutemonMap(lutemonMap);
                        in.close();
                        file.close();
                    }
                    catch (FileNotFoundException e) {
                        Toast.makeText(getContext(), "No save file found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    catch(IOException i) {
                        System.out.println("IOException is caught");
                        i.printStackTrace();
                        return;
                    }
                    catch(ClassNotFoundException c) {
                        System.out.println("ClassNotFoundException is caught");
                        c.printStackTrace();
                        return;
                    }

                    if (getParentFragmentManager().findFragmentByTag(gameFragTag) != null) {
                        assert gameFragment != null;
                        ft.remove(gameFragment);
                    }
                    ft.add(R.id.mainFrame, new GameFragment(), gameFragTag);
                    ft.hide(startScreenFragment);
                    ft.commit();
                    break;

                case R.id.saveBtn:
                    if (storage.getLutemonMap().isEmpty()) {
                        Toast.makeText(getContext(), "No lutemons to save", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        FileOutputStream file = requireContext().openFileOutput("saved_lutemons.ser", Context.MODE_PRIVATE);
                        ObjectOutputStream out = new ObjectOutputStream(file);
                        out.writeObject(storage.getLutemonMap());
                        out.close();
                        file.close();
                    }
                    catch(IOException i) {
                        System.out.println("IOException is caught");
                        i.printStackTrace();
                        return;
                    }
                    Toast.makeText(getContext(), "Game saved", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.howToPlayBtn:
                    ft.add(R.id.mainFrame, new HowToPlayFragment());
                    ft.commit();
                    break;

                case R.id.exitBtn:
                    ((MainActivity)getActivity()).finish();
                    System.exit(0);
                    break;
            }
        }
    };
}