package com.example.lutemonfighter.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.lutemonfighter.R;
import com.example.lutemonfighter.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class GameFragment extends Fragment {
    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ImageButton returnBtn = view.findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(listener);

        TabLayout tabLayout = view.findViewById(R.id.tabBar);
        ViewPager2 fragmentArea = view.findViewById(R.id.fragmentArea);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(requireActivity());
        fragmentArea.setAdapter(tabPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentArea.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragmentArea.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });

        fragmentArea.setOffscreenPageLimit(3);
        return view;
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            if (view.getId() == R.id.returnBtn) {
                ft.hide(getParentFragmentManager().findFragmentByTag("gameFragTag"));
                ft.show(getParentFragmentManager().findFragmentByTag("startScreenFragTag"));
                ft.commit();
            }
        }
    };
}