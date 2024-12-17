package com.example.hunterpedia.armor;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunterpedia.R;
import com.example.hunterpedia.api.ApiClient;
import com.example.hunterpedia.api.ApiService;
import com.example.hunterpedia.datastructure.Armor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArmorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArmorAdapter adapter;
    private List<Armor> armorList = new ArrayList<>();
    private SearchView searchView;
    private Spinner typeSpinner, rankSpinner;

    private String selectedType = "All";
    private String selectedRank = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armor);

        // View 초기화
        recyclerView = findViewById(R.id.armorRecyclerView);
        searchView = findViewById(R.id.armorSearchView);
        typeSpinner = findViewById(R.id.armorTypeSpinner);
        rankSpinner = findViewById(R.id.armorRankSpinner);

        // RecyclerView 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArmorAdapter(armorList);
        recyclerView.setAdapter(adapter);

        setupSpinners();
        setupSearchView();
        fetchArmors();
    }

    // 스피너 설정
    private void setupSpinners() {
        String[] types = {"All", "head", "chest", "gloves", "waist", "legs"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        String[] ranks = {"All", "low", "high", "master"};
        ArrayAdapter<String> rankAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ranks);
        rankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rankSpinner.setAdapter(rankAdapter);

        // 이벤트 리스너 설정
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedType = types[position];
                filterArmors();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        rankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedRank = ranks[position];
                filterArmors();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // SearchView 설정
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterArmors();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterArmors();
                return true;
            }
        });
    }

    // API를 통해 방어구 가져오기
    private void fetchArmors() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Armor>> call = apiService.getAllArmors();

        call.enqueue(new Callback<List<Armor>>() {
            @Override
            public void onResponse(Call<List<Armor>> call, Response<List<Armor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    armorList = response.body();
                    adapter.updateList(armorList);
                }
            }

            @Override
            public void onFailure(Call<List<Armor>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // 필터링 메서드
    private void filterArmors() {
        String query = searchView.getQuery().toString().toLowerCase();

        List<Armor> filteredList = new ArrayList<>();
        for (Armor armor : armorList) {
            boolean matchesType = selectedType.equals("All") || armor.getType().equalsIgnoreCase(selectedType);
            boolean matchesRank = selectedRank.equals("All") || armor.getRank().equalsIgnoreCase(selectedRank);
            boolean matchesSearch = armor.getName().toLowerCase().contains(query);

            if (matchesType && matchesRank && matchesSearch) {
                filteredList.add(armor);
            }
        }

        adapter.updateList(filteredList);
    }
}
