package com.example.lutemonfighter;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private HashMap<Integer, Lutemon> lutemonMap;
    static private Storage instance;

    private Storage() {
        lutemonMap = new HashMap<>();
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public HashMap<Integer, Lutemon> getLutemonMap() {
        return this.lutemonMap;
    }

    public void setLutemonMap(HashMap<Integer, Lutemon> lutemonMap) {
        this.lutemonMap = lutemonMap;
    }

    public Lutemon getLutemonById(int id) {
        return lutemonMap.get(id);
    }

    public void addLutemon(Lutemon lutemon) {
        for (Map.Entry<Integer, Lutemon> entry : lutemonMap.entrySet()) {
            if (lutemon.getId() == entry.getKey()) {
                lutemon.setId(lutemon.getId() + 1);
                addLutemon(lutemon);
                return;
            }
        }
        lutemonMap.put(lutemon.getId(), lutemon);
    }

    public String getLutemonStats(Lutemon lutemon) {
        return lutemon.getName() + "'s stats: att: " + lutemon.getAttack() + ", def: " + lutemon.getDefense() + ", hp: " + lutemon.getHealth() + ", max hp: " + lutemon.getMaxHealth() + ", exp: " + lutemon.getExperience() + ".\n";
    }
}
