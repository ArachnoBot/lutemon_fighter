package com.example.lutemonfighter.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

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

import com.example.lutemonfighter.Enemy;
import com.example.lutemonfighter.Lutemon;
import com.example.lutemonfighter.Storage;
import com.example.lutemonfighter.R;

import java.util.ArrayList;

public class BattlefieldFragment extends Fragment {
    private final ArrayList<Integer> localIdList = new ArrayList<>();
    private final ArrayList<Enemy> enemyList = new ArrayList<>();
    private final ArrayList<Button> attackBtnList = new ArrayList<>();
    private String enemyDescription;
    private boolean isInBattle = false;
    private int level = 1;
    private ConstraintLayout attackChoiceLayout;
    private ConstraintLayout startBattleLayout;
    private RadioGroup moveRadioGroup;
    private Button nextLevelBtn;
    private Button attackEnemy1Btn;
    private Button attackEnemy2Btn;
    private Button attackEnemy3Btn;
    private TextView historyText;
    private TextView levelText;
    private LinearLayout linearLayout;
    private static final Storage storage = Storage.getInstance();
    public BattlefieldFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battlefield, container, false);

        getParentFragmentManager().setFragmentResultListener("dataToBattlefield", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                receiveLutemons(result);
            }
        });

        attackChoiceLayout = view.findViewById(R.id.attackChoiceLayout);
        startBattleLayout = view.findViewById(R.id.startBattleLayout);
        moveRadioGroup = view.findViewById(R.id.moveRadioGroup);
        linearLayout = view.findViewById(R.id.checkBoxLinearLayout);
        historyText = view.findViewById(R.id.battleHistoryText);
        levelText = view.findViewById(R.id.battleLevelText);

        nextLevelBtn = view.findViewById(R.id.nextLevelBtn);
        nextLevelBtn.setOnClickListener(listener);
        nextLevelBtn.setVisibility(View.GONE);

        Button moveBtn = view.findViewById(R.id.moveBtn);
        moveBtn.setOnClickListener(listener);
        Button startBattleBtn = view.findViewById(R.id.startBattleBtn);
        startBattleBtn.setOnClickListener(listener);

        attackEnemy1Btn = view.findViewById(R.id.attackEnemy1Btn);
        attackEnemy1Btn.setOnClickListener(listener);
        attackEnemy2Btn = view.findViewById(R.id.attackEnemy2Btn);
        attackEnemy2Btn.setOnClickListener(listener);
        attackEnemy3Btn = view.findViewById(R.id.attackEnemy3Btn);
        attackEnemy3Btn.setOnClickListener(listener);

        attackBtnList.add(attackEnemy1Btn);
        attackBtnList.add(attackEnemy2Btn);
        attackBtnList.add(attackEnemy3Btn);

        resetBattlefield();

        return view;
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.moveBtn) {
                moveLutemons();
            }
            else if (view.getId() == R.id.startBattleBtn) {
                startBattle();
            }
            else if (view.getId() == R.id.nextLevelBtn && level < 5) {
                level++;
                resetBattlefield();
                nextLevelBtn.setVisibility(View.GONE);
            }
            else if (view.getId() == R.id.attackEnemy1Btn) {
                attackBtnPressed(attackEnemy1Btn);
            }
            else if (view.getId() == R.id.attackEnemy2Btn) {
                attackBtnPressed(attackEnemy2Btn);
            }
            else if (view.getId() == R.id.attackEnemy3Btn) {
                attackBtnPressed(attackEnemy3Btn);
            }
        }
    };

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

    private void resetBattlefield() {
        String text ="Battlefield level: " + level;
        levelText.setText(text);
        enemyList.clear();

        switch (level) {
            case 1:
                enemyList.add(new Enemy("goblin"));
                enemyDescription = "A lone Goblin attacks from the bushes.\n";
                break;

            case 2:
                enemyList.add(new Enemy("orc"));
                enemyList.add(new Enemy("goblin"));
                enemyList.add(new Enemy("goblin"));
                enemyDescription = "An Orc with two Goblins spot you during patrol.\n";
                break;

            case 3:
                enemyList.add(new Enemy("orc"));
                enemyList.add(new Enemy("orc"));
                enemyList.add(new Enemy("orc"));
                enemyDescription = "A group of three Orcs charges at you.\n";
                break;

            case 4:
                enemyList.add(new Enemy("ogre"));
                enemyList.add(new Enemy("ogre"));
                enemyDescription = "Two Ogres are stumbling towards you.\n";
                break;

            case 5:
                enemyList.add(new Enemy("giant"));
                enemyList.add(new Enemy("orc"));
                enemyList.add(new Enemy("orc"));
                enemyDescription = "A giant stomps it's way on the the battlefield with two Orcs.\n";
                break;

            default:
                enemyList.add(new Enemy("goblin"));
                enemyList.add(new Enemy("orc"));
                enemyList.add(new Enemy("ogre"));
                enemyDescription = "Developer level\n";
                break;
        }
    }

    public void startBattle() {
        Lutemon lutemon;

        for (int i = 0; i < localIdList.size(); i++) {
            lutemon = storage.getLutemonById(localIdList.get(i));
            if (lutemon.getHealth() > 0) {
                isInBattle = true;
            }
        }

        if (localIdList.isEmpty() || !isInBattle) {
            Toast.makeText(getContext(), "No lutemons on the battlefield", Toast.LENGTH_SHORT).show();
            return;
        }

        startBattleLayout.setVisibility(View.INVISIBLE);
        attackBtnList.clear();
        attackBtnList.add(attackEnemy1Btn);
        attackBtnList.add(attackEnemy2Btn);
        attackBtnList.add(attackEnemy3Btn);

        for (Button button : attackBtnList) {
            button.setVisibility(View.GONE);
        }

        for (int i = 0; i < enemyList.size(); i++) {
            attackBtnList.get(i).setVisibility(View.VISIBLE);
            attackBtnList.get(i).setText(enemyList.get(i).getName());
        }

        attackChoiceLayout.setVisibility(View.VISIBLE);
        addTextToHistory("You take ");
        for (int i = 0; i < localIdList.size(); i++) {
            lutemon = storage.getLutemonById(localIdList.get(i));
            addTextToHistory(lutemon.getName());
            if (i != localIdList.size() - 1) {
                addTextToHistory(" and ");
            }
        }
        addTextToHistory(" with you to the battlefield.\n");
        addTextToHistory(enemyDescription + "\n");
    }

    private void attackBtnPressed(Button attackButton) {
        ArrayList<Lutemon> lutemons = new ArrayList<>();
        Lutemon lutemon;
        Enemy enemy = enemyList.get(attackBtnList.indexOf(attackButton));
        int damage;

        if (localIdList.size() > 1) {
            for (int i = 0; i < localIdList.size(); i++) {
                lutemons.add(storage.getLutemonById(localIdList.get(i)));
            }
            lutemon = lutemons.get(0);
            for (Lutemon comparisonLutemon : lutemons) {
                if (lutemon.getHealth() < comparisonLutemon.getHealth()) {
                    lutemon = comparisonLutemon;
                }
            }
        } else {
            lutemon = storage.getLutemonById(localIdList.get(0));
        }

        damage = lutemon.getAttackDamage(enemy);
        if (damage > 0) {
            addTextToHistory(lutemon.getName() + " attacks the " + enemy.getName() + " and does " + damage + " damage.\n");
            enemy.setHealth(enemy.getHealth() - damage);
        } else {
            addTextToHistory(lutemon.getName() + " attacks the " + enemy.getName() + ", but completely misses!\n");
        }
        if (checkEnemyStatus(enemy)) { return; }

        damage = enemy.getAttackDamage(lutemon);
        if (damage > 0) {
            addTextToHistory("The " + enemy.getName() + " attacks back and does " + damage + " damage.\n");
            lutemon.setHealth(lutemon.getHealth() - damage);
        } else {
            addTextToHistory("The " + enemy.getName() + " attacks " + lutemon.getName() + ", but completely misses!\n");
        }

        if (checkLutemonStatus(lutemon, lutemons, enemy)) { return; }

        addTextToHistory(lutemon.getName() + " now has " + lutemon.getHealth() + "/" + lutemon.getMaxHealth() + " hp, and the " + enemy.getName()  + " has " + enemy.getHealth() + "/" + enemy.getMaxHealth() + " hp.\n\n");
    }

    private boolean checkLutemonStatus(Lutemon lutemon, ArrayList<Lutemon> lutemons, Enemy enemy) {
        if (lutemon.getHealth() <= 0) {
            String text = lutemon.getName() + " (fainted)";
            ((CheckBox) linearLayout.getChildAt(localIdList.indexOf(lutemon.getId()))).setText(text);
            addTextToHistory(lutemon.getName() + " has fainted! Their stats have been reset to default.\n");
            addTextToHistory("The " + enemy.getName() + " now still has " + enemy.getHealth() + " hp.\n\n");
            lutemon.setFaintCount(lutemon.getFaintCount() + 1);

            for (Lutemon testLutemon : lutemons) {
                if (testLutemon.getHealth() > 0) {
                    return true;
                }
            }
            isInBattle = false;
            addTextToHistory("You have been defeated! All your lutemons have fainted.\n\n");
            attackChoiceLayout.setVisibility(View.INVISIBLE);
            startBattleLayout.setVisibility(View.VISIBLE);
            resetBattlefield();
            return true;
        }
        return false;
    }

    private boolean checkEnemyStatus(Enemy enemy) {
        if (enemy.getHealth() <= 0) {
            addTextToHistory("The " + enemy.getName() + " has been slain!\n\n");
            attackBtnList.get(enemyList.indexOf(enemy)).setVisibility(View.GONE);
            attackBtnList.remove(attackBtnList.get(enemyList.indexOf(enemy)));
            enemyList.remove(enemy);
            if (!enemyList.isEmpty()) {
                return true;
            }
        }
        if (enemyList.isEmpty()) {
            isInBattle = false;
            addTextToHistory("You are victorious! Surviving lutemons gain " + level + " experience.\n");

            for (int i = 0; i < localIdList.size(); i++) {
                storage.getLutemonById(localIdList.get(i)).addExperience(level);
                addTextToHistory(storage.getLutemonStats(storage.getLutemonById(localIdList.get(i))));
            }
            addTextToHistory("\n");
            attackChoiceLayout.setVisibility(View.INVISIBLE);

            if (level < 5) {
                nextLevelBtn.setVisibility(View.VISIBLE);
            }
            startBattleLayout.setVisibility(View.VISIBLE);
            resetBattlefield();
            return true;
        }
        return false;
    }

    public void addCheckBox(String text) {
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

    private void moveLutemons() {
        Bundle bundle = new Bundle();
        ArrayList<Integer> idList = new ArrayList<>();
        String key;

        if (isInBattle) {
            Toast.makeText(getContext(), "Cant move lutemons during battle", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (moveRadioGroup.getCheckedRadioButtonId()) {
            case R.id.battleRadioBtn:
                key = "dataToHome";
                break;
            case R.id.homeRadioBtn:
                key = "dataToTraining";
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