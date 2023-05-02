package com.example.lutemonfighter;

import java.io.Serializable;
import java.util.Random;

public class Creature implements Serializable {
    protected String name;
    protected int attack;
    protected int defense;
    protected int health;
    protected int maxHealth;
    protected final int maxSkill = 99;
    private final double dmgVariance = 0.2;

    public Creature() {
        //Required for serializing?
    }

    public Creature(String name, int attack, int defense, int maxHealth) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health > this.maxHealth) {
            health = this.maxHealth;
        }
        if (health < 0) {
            health = 0;
        }
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttackDamage(Creature creature) {
        int damage = 0;
        Random random = new Random();

        if (random.nextInt((103 - creature.defense)) != 0) {
            damage = (int) (Math.round(attack * (1.0 + random.nextDouble() * (dmgVariance * 2) - dmgVariance)) - Math.round(creature.defense*0.2f));
        }
        return damage;
    }
}
