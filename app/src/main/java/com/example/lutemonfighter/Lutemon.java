package com.example.lutemonfighter;

public class Lutemon extends Creature {
    protected String color;
    protected int experience;
    protected int id;
    protected int faintCount;
    private static int idCounter;
    private static final long serialVersionUID = 420;

    public Lutemon(String name, String color) {
        switch (color) {
            case "White":
                attack = 5;
                defense = 4;
                maxHealth = 20;
                break;

            case "Green":
                attack = 6;
                defense = 3;
                maxHealth = 19;
                break;

            case "Pink":
                attack = 7;
                defense = 2;
                maxHealth = 18;
                break;

            case "Orange":
                attack = 8;
                defense = 1;
                maxHealth = 17;
                break;

            case "Black":
                attack = 9;
                defense = 0;
                maxHealth = 16;
                break;
        }
        this.name = name;
        this.color = color;
        health = maxHealth;
        faintCount = 0;
        experience = 0;
        id = idCounter;
        idCounter++;
    }

    public String getColor() {
        return color;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int experience) {
        if (this.experience + experience < maxSkill) {
            this.experience += experience;
            this.attack += experience;
            this.defense += experience;
            this.maxHealth += experience;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFaintCount() {
        return faintCount;
    }

    public void setFaintCount(int faintCount) {
        this.faintCount = faintCount;
    }
}
