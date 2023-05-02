package com.example.lutemonfighter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatsViewAdapter extends RecyclerView.Adapter<StatsViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<Lutemon> lutemons;

    public StatsViewAdapter(Context context, ArrayList<Lutemon> lutemons) {
        this.context = context;
        this.lutemons = lutemons;
    }

    @NonNull
    @Override
    public StatsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stats_item, parent, false);

        return new StatsViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewAdapter.MyViewHolder holder, int position) {
        String color = "(" + lutemons.get(position).getColor() + ")";
        String attack = "att: " + lutemons.get(position).getAttack();
        String defense = "def: " + lutemons.get(position).getAttack();
        String health = "hp: " + lutemons.get(position).getHealth();
        String maxHealth = "max hp: " + lutemons.get(position).getMaxHealth();
        String experience = "experience gained: " + lutemons.get(position).getExperience();
        String faintCount = "times fainted: " + lutemons.get(position).getFaintCount();

        holder.nameText.setText(lutemons.get(position).getName());
        holder.colorText.setText(color);
        holder.attackText.setText(attack);
        holder.defenseText.setText(defense);
        holder.healthText.setText(health);
        holder.maxHealthText.setText(maxHealth);
        holder.experienceText.setText(experience);
        holder.faintText.setText(faintCount);
    }

    @Override
    public int getItemCount() { return lutemons.size(); }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView nameText, colorText, attackText, defenseText, healthText, maxHealthText, experienceText, faintText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.nameText);
            colorText = itemView.findViewById(R.id.colorText);
            attackText = itemView.findViewById(R.id.attackText);
            defenseText = itemView.findViewById(R.id.defenseText);
            healthText = itemView.findViewById(R.id.healthText);
            maxHealthText = itemView.findViewById(R.id.maxHealthText);
            experienceText = itemView.findViewById(R.id.experienceText);
            faintText = itemView.findViewById(R.id.faintText);
        }
    }
}
