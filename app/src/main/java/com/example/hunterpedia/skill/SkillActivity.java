package com.example.hunterpedia.skill;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.SearchView;

import com.example.hunterpedia.R;
import com.example.hunterpedia.api.ApiClient;
import com.example.hunterpedia.api.ApiService;
import com.example.hunterpedia.datastructure.Skill;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkillActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SkillAdapter skillAdapter;
    private SearchView searchView;
    private LinearLayout loadingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        recyclerView = findViewById(R.id.skillRecyclerView);
        searchView = findViewById(R.id.skillSearchView);
        loadingLayout = findViewById(R.id.loadingLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchSkills();
        setupSearchView();
    }

    private void fetchSkills() {
        showLoadingScreen(true); // 로딩 화면 표시

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Skill>> call = apiService.getSkills();

        call.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                showLoadingScreen(false); // 로딩 화면 숨김
                if (response.isSuccessful() && response.body() != null) {
                    List<Skill> skillList = response.body();
                    skillAdapter = new SkillAdapter(skillList);
                    recyclerView.setAdapter(skillAdapter);
                } else {
                    Log.e("SkillActivity", "Failed to fetch skills.");
                }
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {
                showLoadingScreen(false); // 로딩 화면 숨김
                Log.e("SkillActivity", "Error: " + t.getMessage());
            }
        });
    }


    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Enter 키를 누른 경우 처리
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (skillAdapter != null) {
                    skillAdapter.filter(newText); // 입력값에 따라 필터링
                }
                return true;
            }
        });
    }

    private void showLoadingScreen(boolean show) {
        if (show) {
            loadingLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            loadingLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
