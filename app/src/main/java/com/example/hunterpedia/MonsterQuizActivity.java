package com.example.hunterpedia;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hunterpedia.api.ApiClient;
import com.example.hunterpedia.api.ApiService;
import com.example.hunterpedia.datastructure.Monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonsterQuizActivity extends AppCompatActivity {

    private TextView timerTextView, monsterDescription, scoreTextView, loadingTextView, feedbackTextView;
    private ImageView monsterIcon;
    private LinearLayout optionsLayout, endGameLayout, quizLayout;
    private ProgressBar loadingProgressBar;

    private List<Monster> monsterList = new ArrayList<>();
    private Monster correctMonster;
    private CountDownTimer countDownTimer;

    private static final int TIME_LIMIT = 15000; // 15초 제한 시간
    private int score = 0; // 맞춘 문제 수
    private int questionCount = 0; // 출제된 문제 수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_quiz);

        // View 초기화
        timerTextView = findViewById(R.id.timerTextView);
        monsterIcon = findViewById(R.id.monsterIcon);
        monsterDescription = findViewById(R.id.monsterDescription);
        optionsLayout = findViewById(R.id.optionsLayout);
        scoreTextView = findViewById(R.id.scoreTextView);
        endGameLayout = findViewById(R.id.endGameLayout);
        quizLayout = findViewById(R.id.quizLayout);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        loadingTextView = findViewById(R.id.loadingTextView);

        Button restartButton = findViewById(R.id.restartButton);
        Button gotoMainButton = findViewById(R.id.gotoMainButton);

        restartButton.setOnClickListener(v -> restartGame());
        gotoMainButton.setOnClickListener(v -> goToMain());

        fetchMonsters();
    }

    private void fetchMonsters() {
        showLoadingScreen(true);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Monster>> call = apiService.getAllMonsters();

        call.enqueue(new Callback<List<Monster>>() {
            @Override
            public void onResponse(Call<List<Monster>> call, Response<List<Monster>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    monsterList = response.body();
                    showLoadingScreen(false);
                    loadNextQuiz();
                }
            }

            @Override
            public void onFailure(Call<List<Monster>> call, Throwable t) {
                showLoadingScreen(false);
            }
        });
    }

    private void loadNextQuiz() {
        if (questionCount >= 10) {
            endGame();
            return;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Collections.shuffle(monsterList);
        correctMonster = monsterList.get(0);

        loadMonsterQuizUI();
        startTimer();
        questionCount++;
    }

    private void loadMonsterQuizUI() {
        boolean showIcon = Math.random() > 0.5;

        if (showIcon) {
            int imageResource = getResources().getIdentifier("_" + correctMonster.getId(), "drawable", getPackageName());
            if (imageResource != 0) {
                monsterIcon.setImageResource(imageResource);
            }
            monsterDescription.setText("");
        } else {
            monsterIcon.setImageResource(0);
            monsterDescription.setText("Species: " + correctMonster.getSpecies() +
                    "\nDescription: " + correctMonster.getDescription());
        }

        List<Monster> options = new ArrayList<>(monsterList.subList(0, 4));
        if (!options.contains(correctMonster)) {
            options.set(0, correctMonster);
        }
        Collections.shuffle(options);

        optionsLayout.removeAllViews();
        for (Monster option : options) {
            Button optionButton = new Button(this);
            optionButton.setText(option.getName());
            optionButton.setOnClickListener(v -> checkAnswer(option));
            optionsLayout.addView(optionButton);
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(TIME_LIMIT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                loadNextQuiz();
            }
        }.start();
    }

    private void checkAnswer(Monster selectedMonster) {
        countDownTimer.cancel();
        if (selectedMonster.getId() == correctMonster.getId()) {
            score++;
        }
        // 점수 텍스트뷰 업데이트
        scoreTextView.setText("Score: " + score + " / " + questionCount);
        loadNextQuiz();
    }


    private void endGame() {
        optionsLayout.setVisibility(View.GONE);
        scoreTextView.setText("You answered " + score + " out of " + questionCount + " correctly!");
        endGameLayout.setVisibility(View.VISIBLE);
    }

    private void restartGame() {
        score = 0;
        questionCount = 0;
        optionsLayout.setVisibility(View.VISIBLE);
        endGameLayout.setVisibility(View.GONE);
        scoreTextView.setText("Score: 0 / 0");
        loadNextQuiz();
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoadingScreen(boolean show) {
        if (show) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loadingTextView.setVisibility(View.VISIBLE);
            quizLayout.setVisibility(View.GONE);
            endGameLayout.setVisibility(View.GONE);
        } else {
            loadingProgressBar.setVisibility(View.GONE);
            loadingTextView.setVisibility(View.GONE);
            quizLayout.setVisibility(View.VISIBLE);
        }
    }
}
