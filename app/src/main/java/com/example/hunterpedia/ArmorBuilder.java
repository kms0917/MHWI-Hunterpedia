package com.example.hunterpedia;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // Retrofit을 사용하여 스킬 데이터를 받아오기
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Skill>> call = apiService.getSkills();

        call.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(@NonNull Call<List<Skill>> call, @NonNull Response<List<Skill>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 스킬 데이터가 성공적으로 받아졌을 때
                    List<Skill> skills = response.body();
                    // 받은 스킬 데이터를 처리하는 코드 작성
                    Log.d("ArmorBuilder", "Skills loaded: " + skills.size());
                    // 예를 들어, Adapter에 데이터를 전달할 수 있음
                } else {
                    // API 호출이 실패한 경우
                    Log.e("ArmorBuilder", "Failed to load skills.");
                    Toast.makeText(ArmorBuilder.this, "스킬 데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Skill>> call, @NonNull Throwable t) {
                // 네트워크 오류 등으로 실패한 경우
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
                Toast.makeText(ArmorBuilder.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
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
