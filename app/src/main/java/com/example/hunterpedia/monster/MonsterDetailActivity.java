package com.example.hunterpedia.monster;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterpedia.R;

import java.util.ArrayList;

public class MonsterDetailActivity extends AppCompatActivity {

    private ImageView monsterImageView;
    private TextView monsterName, monsterType, monsterSpecies, monsterDescription,
            monsterElements, monsterAilments, monsterLocations, monsterResistances, monsterWeaknesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_detail);

        // View 초기화
        monsterImageView = findViewById(R.id.monsterImageView);
        monsterName = findViewById(R.id.monsterName);
        monsterType = findViewById(R.id.monsterType);
        monsterSpecies = findViewById(R.id.monsterSpecies);
        monsterDescription = findViewById(R.id.monsterDescription);
        monsterElements = findViewById(R.id.monsterElements);
        monsterAilments = findViewById(R.id.monsterAilments);
        monsterLocations = findViewById(R.id.monsterLocations);
        monsterResistances = findViewById(R.id.monsterResistances);
        monsterWeaknesses = findViewById(R.id.monsterWeaknesses);

        // 인텐트에서 몬스터 정보 가져오기
        int id = getIntent().getIntExtra("MONSTER_ID", -1);
        String name = getIntent().getStringExtra("MONSTER_NAME");
        String type = getIntent().getStringExtra("MONSTER_TYPE");
        String species = getIntent().getStringExtra("MONSTER_SPECIES");
        String description = getIntent().getStringExtra("MONSTER_DESCRIPTION");
        ArrayList<String> elements = getIntent().getStringArrayListExtra("MONSTER_ELEMENTS");
        ArrayList<String> ailments = getIntent().getStringArrayListExtra("MONSTER_AILMENTS");
        ArrayList<String> locations = getIntent().getStringArrayListExtra("MONSTER_LOCATIONS");
        ArrayList<String> resistances = getIntent().getStringArrayListExtra("MONSTER_RESISTANCES");
        ArrayList<String> weaknesses = getIntent().getStringArrayListExtra("MONSTER_WEAKNESSES");

        // UI 업데이트
        updateUI(id, name, type, species, description, elements, ailments, locations, resistances, weaknesses);
    }

    private void updateUI(int id, String name, String type, String species, String description,
                          ArrayList<String> elements, ArrayList<String> ailments,
                          ArrayList<String> locations, ArrayList<String> resistances, ArrayList<String> weaknesses) {

        // 이미지 설정
        int imageResource = getResources().getIdentifier("_" + id, "drawable", getPackageName());
        if (imageResource != 0) {
            monsterImageView.setImageResource(imageResource);
        }

        // 기본 정보 설정
        monsterName.setText(name);
        monsterType.setText("Type: " + type);
        monsterSpecies.setText("Species: " + species);
        monsterDescription.setText("Description: " + description);

        // Elements 설정
        if (elements != null && !elements.isEmpty()) {
            monsterElements.setText("Elements: " + String.join(", ", elements));
        } else {
            monsterElements.setText("Elements: None");
        }

        // Ailments 설정
        if (ailments != null && !ailments.isEmpty()) {
            monsterAilments.setText("Ailments: " + String.join(", ", ailments));
        } else {
            monsterAilments.setText("Ailments: None");
        }

        // Locations 설정
        if (locations != null && !locations.isEmpty()) {
            monsterLocations.setText("Locations: " + String.join(", ", locations));
        } else {
            monsterLocations.setText("Locations: None");
        }

        // Resistances 설정
        if (resistances != null && !resistances.isEmpty()) {
            monsterResistances.setText("Resistances: " + String.join(", ", resistances));
        } else {
            monsterResistances.setText("Resistances: None");
        }

        // Weaknesses 설정
        if (weaknesses != null && !weaknesses.isEmpty()) {
            monsterWeaknesses.setText("Weaknesses: " + String.join(", ", weaknesses));
        } else {
            monsterWeaknesses.setText("Weaknesses: None");
        }
    }
}
