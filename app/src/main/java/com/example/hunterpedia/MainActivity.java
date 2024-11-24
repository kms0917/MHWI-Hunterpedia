package com.example.hunterpedia;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;

import com.example.hunterpedia.builder.ArmorBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToArmorBuilderButton = findViewById(R.id.armorbuilderbtn);
        goToArmorBuilderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ArmorBuilder.class);
                startActivity(intent);
            }
        });
    }

}
