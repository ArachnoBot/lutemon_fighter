package com.example.lutemonfighter.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lutemonfighter.Lutemon;
import com.example.lutemonfighter.Storage;
import com.example.lutemonfighter.R;

import java.util.ArrayList;

public class TrainingAreaFragment extends Fragment {
    private final ArrayList<Integer> localIdList = new ArrayList<>();
    private final int trainingInterval = 5000;
    private RadioGroup moveRadioGroup;
    private TextView historyText;
    private Handler handler;
    private LinearLayout linearLayout;
    private static final Storage storage = Storage.getInstance();
    public TrainingAreaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_area, container, false);

        getParentFragmentManager().setFragmentResultListener("dataToTraining", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                receiveLutemons(result);
            }
        });

        handler = new Handler();
        moveRadioGroup = view.findViewById(R.id.moveRadioGroup);
        linearLayout = view.findViewById(R.id.checkBoxLinearLayout);
        historyText = view.findViewById(R.id.trainingText);

        Button startTrainingBtn = view.findViewById(R.id.startTrainingBtn);
        startTrainingBtn.setOnClickListener(listener);

        Button stopTrainingBtn = view.findViewById(R.id.stopTrainingBtn);
        stopTrainingBtn.setOnClickListener(listener);

        Button moveBtn = view.findViewById(R.id.moveBtn);
        moveBtn.setOnClickListener(listener);

        return view;
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.moveBtn) {
                moveLutemons();
            }
            else if (view.getId() == R.id.startTrainingBtn && !handler.hasCallbacks(statusChecker) && !localIdList.isEmpty()) {
                for (int i = 0; i < localIdList.size(); i++) {
                    String lutemonName = storage.getLutemonById(localIdList.get(i)).getName();
                    addTextToHistory(lutemonName + " started training.\n");
                }
                statusChecker.run();
            }
            else if (view.getId() == R.id.stopTrainingBtn && handler.hasCallbacks(statusChecker)) {
                addTextToHistory("Training stopped.\n\n");
                handler.removeCallbacks(statusChecker);
            }
        }
    };

    Runnable statusChecker = new Runnable() {
        @Override
        public void run() {
            if (updateLutemonStats()) {
                handler.removeCallbacks(statusChecker);
                return;
            }
            handler.postDelayed(statusChecker, trainingInterval);
        }
    };

    private boolean updateLutemonStats() {
        Lutemon lutemon;

        for (int i = 0; i < localIdList.size(); i++) {
            lutemon = storage.getLutemonById(localIdList.get(i));
            if (lutemon.getHealth() <= 0) {
                String text = lutemon.getName() + " (fainted)";
                ((CheckBox) linearLayout.getChildAt(localIdList.indexOf(lutemon.getId()))).setText(text);
                addTextToHistory(lutemon.getName() + " has fainted! Their stats have been reset to default.\n");
                lutemon.setFaintCount(lutemon.getFaintCount() + 1);
                addTextToHistory("Training stopped.\n\n");
                return true;
            }
        }

        for (int i = 0; i < localIdList.size(); i++) {
            lutemon = storage.getLutemonById(localIdList.get(i));
            lutemon.addExperience(1);
            lutemon.setHealth(lutemon.getHealth() - 1);
            addTextToHistory(lutemon.getName() + " has leveled up!\n");
            addTextToHistory(storage.getLutemonStats(lutemon) + "\n");
        }
        return false;
    }

    private void receiveLutemons(Bundle result) {
        ArrayList<Integer> newId = result.getIntegerArrayList("lutemonId");
        for (Integer id : newId) {
            localIdList.add(id);
            String checkBoxText = storage.getLutemonById(id).getName();
            if (storage.getLutemonById(id).getHealth() <= 0) {
                checkBoxText += " (fainted)";
            }
            addCheckBox(checkBoxText);
        }
    }

    private void addCheckBox(String text) {
        CheckBox checkbox = new CheckBox(new ContextThemeWrapper(getContext(), R.style.MyCheckboxStyle));
        checkbox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        checkbox.setText(text);
        if (linearLayout != null) {
            linearLayout.addView(checkbox);
        }
    }

    private void addTextToHistory(String text) {
        historyText.append(text);
        if (historyText.getLineCount() > 40) {
            historyText.getEditableText().delete(historyText.getLayout().getLineStart(0), historyText.getLayout().getLineEnd(0));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void moveLutemons() {
        Bundle bundle = new Bundle();
        ArrayList<Integer> idList = new ArrayList<>();
        String key;

        if (handler.hasCallbacks(statusChecker)) {
            Toast.makeText(getContext(), "Stop training before moving lutemons", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (moveRadioGroup.getCheckedRadioButtonId()) {
            case R.id.homeRadioBtn:
                key = "dataToHome";
                break;

            case R.id.battleRadioBtn:
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