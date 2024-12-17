package com.example.hunterpedia.monster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterpedia.R;
import com.example.hunterpedia.api.ApiClient;
import com.example.hunterpedia.api.ApiService;
import com.example.hunterpedia.datastructure.Ailment;
import com.example.hunterpedia.datastructure.Location;
import com.example.hunterpedia.datastructure.Monster;
import com.example.hunterpedia.datastructure.MonsterResistance;
import com.example.hunterpedia.datastructure.MonsterWeakness;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonsterGridActivity extends AppCompatActivity {

    private GridLayout monsterGridLayout;
    private LinearLayout loadingLayout;
    private List<Monster> monsterList = new ArrayList<>(); // 초기 빈 리스트

    private static final String TAG = "MonsterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_grid);
        loadingLayout = findViewById(R.id.loadingLayout);

        monsterGridLayout = findViewById(R.id.monsterGridLayout);

        // 몬스터 목록 가져오기
        fetchMonsters();
    }

    private void fetchMonsters() {
        showLoadingScreen(true); // 로딩 화면 표시

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Monster>> call = apiService.getAllMonsters();

        call.enqueue(new Callback<List<Monster>>() {
            @Override
            public void onResponse(Call<List<Monster>> call, Response<List<Monster>> response) {
                showLoadingScreen(false); // 로딩 화면 숨김
                if (response.isSuccessful() && response.body() != null) {
                    monsterList = response.body(); // API로 가져온 몬스터 목록 저장
                    Log.d(TAG, "Total Monsters: " + monsterList.size());

                    // UI에 그리드 업데이트
                    populateMonsterGrid();
                } else {
                    Log.e(TAG, "Failed to fetch monsters.");
                }
            }

            @Override
            public void onFailure(Call<List<Monster>> call, Throwable t) {
                showLoadingScreen(false); // 로딩 화면 숨김
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });
    }


    private void populateMonsterGrid() {
        // 그리드 초기화 (기존 항목 제거)
        monsterGridLayout.removeAllViews();

        // 화면의 너비를 가져와서 4등분
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int buttonSize = screenWidth / 4; // 각 버튼의 너비와 높이를 1/4로 설정

        for (Monster monster : monsterList) {
            // 아이템 레이아웃 생성
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setGravity(Gravity.CENTER);
            GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
            gridParams.width = buttonSize;
            gridParams.height = ViewGroup.LayoutParams.WRAP_CONTENT; // 아이템의 높이는 내용에 맞춤
            gridParams.setMargins(0, 10, 0, 10); // 위아래 마진 추가
            itemLayout.setLayoutParams(gridParams);

            // 이미지 버튼 생성
            ImageButton imageButton = new ImageButton(this);
            int imageResource = getResources().getIdentifier(
                    "_" + monster.getId(), "drawable", getPackageName());

            if (imageResource != 0) { // 리소스가 존재하는 경우
                imageButton.setImageResource(imageResource);
            }

            imageButton.setScaleType(ImageButton.ScaleType.FIT_CENTER); // 이미지 비율 유지
            imageButton.setAdjustViewBounds(true); // 이미지가 버튼 크기에 맞게 조정
            imageButton.setBackground(null);
            imageButton.setPadding(0, 20, 0, 20); // 이미지 버튼에 패딩 추가 (위아래 20px)
            imageButton.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, buttonSize
            ));

            // 클릭 이벤트 설정
            imageButton.setOnClickListener(v -> openMonsterDetail(monster.getId()));

            // 텍스트 뷰 생성 (몬스터 이름)
            TextView textView = new TextView(this);
            textView.setText(monster.getName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setPadding(0, 5, 0, 0); // 텍스트 위쪽 패딩 추가

            // 뷰 추가
            itemLayout.addView(imageButton);
            itemLayout.addView(textView);
            monsterGridLayout.addView(itemLayout);
        }
    }

    private void openMonsterDetail(int monsterId) {
        Monster selectedMonster = null;
        for (Monster monster : monsterList) {
            if (monster.getId() == monsterId) {
                selectedMonster = monster;
                break;
            }
        }

        if (selectedMonster != null) {
            Intent intent = new Intent(this, MonsterDetailActivity.class);
            intent.putExtra("MONSTER_ID", selectedMonster.getId());
            intent.putExtra("MONSTER_NAME", selectedMonster.getName());
            intent.putExtra("MONSTER_TYPE", selectedMonster.getType());
            intent.putExtra("MONSTER_SPECIES", selectedMonster.getSpecies());
            intent.putExtra("MONSTER_DESCRIPTION", selectedMonster.getDescription());

            // Elements
            if (selectedMonster.getElements() != null) {
                intent.putStringArrayListExtra("MONSTER_ELEMENTS", new ArrayList<>(selectedMonster.getElements()));
            }

            // Ailments
            ArrayList<String> ailmentList = new ArrayList<>();
            if (selectedMonster.getAilments() != null) {
                for (Ailment ailment : selectedMonster.getAilments()) {
                    String ailmentInfo = ailment.getName();
                    ailmentList.add(ailmentInfo);
                }
            }
            intent.putStringArrayListExtra("MONSTER_AILMENTS", ailmentList);

            // Locations
            ArrayList<String> locationList = new ArrayList<>();
            if (selectedMonster.getLocations() != null) {
                for (Location location : selectedMonster.getLocations()) {
                    String locationInfo = location.getName() + " (Zones: " + location.getZoneCount() + ")";
                    locationList.add(locationInfo);
                }
            }
            intent.putStringArrayListExtra("MONSTER_LOCATIONS", locationList);

            // Resistances
            ArrayList<String> resistanceList = new ArrayList<>();
            if (selectedMonster.getResistances() != null) {
                for (MonsterResistance resistance : selectedMonster.getResistances()) {
                    String resistanceInfo = resistance.getElement();
                    resistanceList.add(resistanceInfo);
                }
            }
            intent.putStringArrayListExtra("MONSTER_RESISTANCES", resistanceList);

            // Weaknesses
            ArrayList<String> weaknessList = new ArrayList<>();
            if (selectedMonster.getWeaknesses() != null) {
                for (MonsterWeakness weakness : selectedMonster.getWeaknesses()) {
                    String weaknessInfo = weakness.getElement() + " (Stars: " + weakness.getStars() + ")";
                    weaknessList.add(weaknessInfo);
                }
            }
            intent.putStringArrayListExtra("MONSTER_WEAKNESSES", weaknessList);

            // Start Activity
            startActivity(intent);
        }
    }

    private void showLoadingScreen(boolean show) {
        if (show) {
            loadingLayout.setVisibility(View.VISIBLE);
            monsterGridLayout.setVisibility(View.GONE);
        } else {
            loadingLayout.setVisibility(View.GONE);
            monsterGridLayout.setVisibility(View.VISIBLE);
        }
    }
}
