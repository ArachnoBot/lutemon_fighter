package com.example.lutemonfighter.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lutemonfighter.Lutemon;
import com.example.lutemonfighter.Storage;
import com.example.lutemonfighter.R;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {
    private final ArrayList<Integer> localIdList = new ArrayList<>();
    private RadioGroup moveRadioGroup;
    private RadioGroup colorRadioGroup;
    private EditText nameInput;
    private LinearLayout linearLayout;
    private static final Storage storage = Storage.getInstance();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getParentFragmentManager().setFragmentResultListener("dataToHome", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                receiveLutemons(result);
            }
        });

        linearLayout = view.findViewById(R.id.checkBoxLinearLayout);
        moveRadioGroup = view.findViewById(R.id.moveRadioGroup);
        colorRadioGroup = view.findViewById(R.id.colorRadioGroup);
        nameInput = view.findViewById(R.id.nameInput);

        Button moveBtn = view.findViewById(R.id.moveBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveLutemons();
            }
        });

        Button createLutemonBtn = view.findViewById(R.id.createLutemonBtn);
        createLutemonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLutemon();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!storage.getLutemonMap().isEmpty()) {
            for (Map.Entry<Integer, Lutemon> entry : storage.getLutemonMap().entrySet()) {
                addLutemon(storage.getLutemonById(entry.getKey()));
            }
        }
    }

    private void receiveLutemons(Bundle result) {
        ArrayList<Integer> newId = result.getIntegerArrayList("lutemonId");
        for (Integer id : newId) {
            String checkBoxText = storage.getLutemonById(id).getName();
            if (storage.getLutemonById(id).getHealth() < storage.getLutemonById(id).getMaxHealth()) {
                checkBoxText += " (healed)";
                storage.getLutemonById(id).setHealth(storage.getLutemonById(id).getMaxHealth());
            }
            CheckBox checkbox = new CheckBox(new ContextThemeWrapper(getContext(), R.style.MyCheckboxStyle));
            checkbox.setText(checkBoxText);
            checkbox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(checkbox);
            localIdList.add(id);
        }
    }

    private void createLutemon() {
        Lutemon lutemon;
        String name = nameInput.getText().toString();

        if (storage.getLutemonMap().size() > 2) {
            Toast.makeText(getContext(), "Cant create more lutemons", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Give a name", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (colorRadioGroup.getCheckedRadioButtonId()) {
            case R.id.whiteRadioBtn:
                lutemon = new Lutemon(name, "White");
                break;

            case R.id.greenRadioBtn:
                lutemon = new Lutemon(name, "Green");
                break;

            case R.id.pinkRadioBtn:
                lutemon = new Lutemon(name, "Pink");
                break;

            case R.id.orangeRadioBtn:
                lutemon = new Lutemon(name, "Orange");
                break;

            case R.id.blackRadioBtn:
                lutemon = new Lutemon(name, "Black");
                break;

            default:
                Toast.makeText(getContext(), "Select a color", Toast.LENGTH_SHORT).show();
                return;
        }
        storage.addLutemon(lutemon);
        addLutemon(lutemon);
        nameInput.setText("");
        colorRadioGroup.clearCheck();
    }

    public void addLutemon(Lutemon lutemon) {
        CheckBox checkbox = new CheckBox(new ContextThemeWrapper(getContext(), R.style.MyCheckboxStyle));
        checkbox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        checkbox.setText(lutemon.getName());
        linearLayout.addView(checkbox);
        localIdList.add(lutemon.getId());
    }

    private void moveLutemons() {
        Bundle bundle = new Bundle();
        ArrayList<Integer> idList = new ArrayList<>();
        String key;

        switch (moveRadioGroup.getCheckedRadioButtonId()) {
            case R.id.battleRadioBtn:
                key = "dataToTraining";
                break;

            case R.id.homeRadioBtn:
                key = "dataToBattlefield";
                break;

            default:
                Toast.makeText(getContext(), "Select a destination", Toast.LENGTH_SHORT).show();
                return;
        }

        for (int i = linearLayout.getChildCount() - 1; i >= 0; i--) {
            if (((CheckBox) linearLayout.getChildAt(i)).isChecked()) {
                idList.add(localIdList.get(i));
                linearLayout.removeViewAt(i);
                localIdList.remove(i);
            }
        }
        bundle.putIntegerArrayList("lutemonId", idList);
        getParentFragmentManager().setFragmentResult(key, bundle);
    }
}