package com.example.hunterpedia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterpedia.api.ApiClient;
import com.example.hunterpedia.api.ApiService;
import com.example.hunterpedia.builder.BuilderActivity;
import com.example.hunterpedia.datastructure.Monster;
import com.example.hunterpedia.datastructure.MonsterWeakness;
import com.example.hunterpedia.datastructure.Skill;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToArmorBuilderButton = findViewById(R.id.armorbuilderbtn);
        goToArmorBuilderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BuilderActivity.class);
                startActivity(intent);
            }
        });

        Button goToMonsterButton = findViewById(R.id.monsterbtn);
        goToMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonsterGridActivity.class);
                startActivity(intent);
            }
        });

        Button goToSkillButton = findViewById(R.id.skillbtn);
        goToSkillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SkillActivity.class);
                startActivity(intent);
            }
        });

        Button goToArmorButton = findViewById(R.id.armorbtn);
        goToArmorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArmorActivity.class);
                startActivity(intent);
            }
        });
    }
}
