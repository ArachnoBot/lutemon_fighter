package com.example.lutemonfighter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.lutemonfighter.fragments.BattlefieldFragment;
import com.example.lutemonfighter.fragments.HomeFragment;
import com.example.lutemonfighter.fragments.StatisticsFragment;
import com.example.lutemonfighter.fragments.TrainingAreaFragment;

public class TabPagerAdapter extends FragmentStateAdapter {
    public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();

            case 1:
                return new TrainingAreaFragment();

            case 2:
                return new BattlefieldFragment();

            case 3:
                return new StatisticsFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() { return 4; }
}
