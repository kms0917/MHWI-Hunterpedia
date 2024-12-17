package com.example.hunterpedia.weapon;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.hunterpedia.R;
import com.example.hunterpedia.api.ApiClient;
import com.example.hunterpedia.api.ApiService;
import com.example.hunterpedia.datastructure.Weapon;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeaponActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WeaponAdapter adapter;
    private List<Weapon> weaponList = new ArrayList<>();
    private List<Weapon> filteredList = new ArrayList<>();
    private SearchView searchView;

    private Spinner typeSpinner, elementSpinner;
    private String selectedType = "All", selectedElement = "All";
    private LinearLayout loadingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapon);

        recyclerView = findViewById(R.id.weaponRecyclerView);
        searchView = findViewById(R.id.weaponSearchView);
        typeSpinner = findViewById(R.id.weaponTypeSpinner);
        elementSpinner = findViewById(R.id.weaponElementSpinner);
        loadingLayout = findViewById(R.id.loadingLayout);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeaponAdapter(weaponList);
        recyclerView.setAdapter(adapter);

        setupSpinners();
        fetchWeapons();
        setupSearchView();
    }

    private void setupSpinners() {
        // 무기 타입 필터
        String[] weaponTypes = {"All", "great-sword", "long-sword", "sword-and-shield", "dual-blades",
                "hammer", "hunting-horn", "lance", "gunlance", "switch-axe", "charge-blade",
                "insect-glaive", "light-bowgun", "heavy-bowgun", "bow"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weaponTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        // 무기 속성 필터
        String[] weaponElements = {"All", "fire", "water", "thunder", "ice", "dragon", "poison", "paralysis",
                "sleep", "blast"};
        ArrayAdapter<String> elementAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weaponElements);
        elementAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elementSpinner.setAdapter(elementAdapter);

        // Spinner 이벤트 리스너
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = weaponTypes[position];
                filterWeapons();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        elementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedElement = weaponElements[position];
                filterWeapons();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void fetchWeapons() {
        showLoadingScreen(true); // 로딩 화면 표시

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Weapon>> call = apiService.getAllWeapons();

        call.enqueue(new Callback<List<Weapon>>() {
            @Override
            public void onResponse(Call<List<Weapon>> call, Response<List<Weapon>> response) {
                showLoadingScreen(false); // 로딩 화면 숨김
                if (response.isSuccessful() && response.body() != null) {
                    weaponList = response.body();
                    filterWeapons();
                }
            }

            @Override
            public void onFailure(Call<List<Weapon>> call, Throwable t) {
                showLoadingScreen(false); // 로딩 화면 숨김
                t.printStackTrace();
            }
        });
    }


    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterWeapons();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterWeapons();
                return true;
            }
        });
    }

    private void filterWeapons() {
        String query = searchView.getQuery().toString().toLowerCase();
        filteredList.clear();

        for (Weapon weapon : weaponList) {
            boolean matchesType = selectedType.equals("All") || weapon.getType().equalsIgnoreCase(selectedType);
            boolean matchesElement = selectedElement.equals("All") ||
                    (weapon.getElements() != null && weapon.getElements().stream()
                            .anyMatch(element -> element.getType().equalsIgnoreCase(selectedElement)));
            boolean matchesSearch = weapon.getName().toLowerCase().contains(query);

            if (matchesType && matchesElement && matchesSearch) {
                filteredList.add(weapon);
            }
        }

        adapter.updateList(filteredList);
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
