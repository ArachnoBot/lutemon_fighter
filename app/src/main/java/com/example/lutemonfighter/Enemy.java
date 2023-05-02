package com.example.lutemonfighter;

import java.util.Objects;

public class Enemy extends Creature {

    public Enemy(String enemyType) {
        super("", 0, 0, 0);
        if (Objects.equals(enemyType, "goblin")) {
            this.name = "Goblin";
            this.attack = 5;
            this.defense = 3;
            this.maxHealth = 10;
            this.health = this.maxHealth;
        }
        else if (Objects.equals(enemyType, "orc")) {
            this.name = "Orc";
            this.attack = 20;
            this.defense = 15;
            this.maxHealth = 30;
            this.health = this.maxHealth;
        }
        else if (Objects.equals(enemyType, "ogre")) {
            this.name = "Ogre";
            this.attack = 40;
            this.defense = 20;
            this.maxHealth = 60;
            this.health = this.maxHealth;
        }
        else if (Objects.equals(enemyType, "giant")) {
            this.name = "Giant";
            this.attack = 70;
            this.defense = 50;
            this.maxHealth = 99;
            this.health = this.maxHealth;
        }


    }
}
