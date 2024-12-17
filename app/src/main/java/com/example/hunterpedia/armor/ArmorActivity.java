package com.example.hunterpedia.armor;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunterpedia.R;
import com.example.hunterpedia.api.ApiClient;
import com.example.hunterpedia.api.ApiService;
import com.example.hunterpedia.datastructure.Armor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArmorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArmorAdapter armorAdapter;
    private SearchView searchView;

    private static final String TAG = "ArmorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armor);

        recyclerView = findViewById(R.id.armorRecyclerView);
        searchView = findViewById(R.id.armorSearchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchArmors();
        setupSearchView();
    }

    private void fetchArmors() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Armor>> call = apiService.getMasterArmors(); // 마스터 랭크 방어구 가져오기

        call.enqueue(new Callback<List<Armor>>() {
            @Override
            public void onResponse(Call<List<Armor>> call, Response<List<Armor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Armor> armorList = response.body();
                    armorAdapter = new ArmorAdapter(armorList);
                    recyclerView.setAdapter(armorAdapter);
                } else {
                    Log.e(TAG, "Failed to fetch armors.");
                }
            }

            @Override
            public void onFailure(Call<List<Armor>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Enter 키 동작은 필요 없음
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (armorAdapter != null) {
                    armorAdapter.filter(newText); // 입력값에 따라 필터링
                }
                return true;
            }
        });
    }
}
