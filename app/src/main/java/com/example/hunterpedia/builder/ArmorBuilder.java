package com.example.hunterpedia.builder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hunterpedia.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ArmorBuilder extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aromor_builder);

        // TabLayout and ViewPager2 setup
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        // Set adapter for ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("장비 검색");
                            break;
                        case 1:
                            tab.setText("내 장식주");
                            break;
                        case 2:
                            tab.setText("마이 세트");
                            break;
                    }
                }).attach();
    }

    // Adapter for ViewPager2
    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Return a fragment based on the tab position
            switch (position) {
                case 1:
                    return new DecoFragment();
                case 2:
                    return new MySetFragment();
                default:
                    return new BuilderFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3; // Number of tabs
        }
    }
}
