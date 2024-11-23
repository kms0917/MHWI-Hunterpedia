package com.example.hunterpedia;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String API_URL = "https://mhw-db.com/armor";
    private static final String FILE_NAME = "data.json"; // 저장할 파일 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void fetchDataFromApi() {
        OkHttpClient client = new OkHttpClient();

        // GET 요청을 보내기 위한 Request 객체 생성
        Request request = new Request.Builder()
                .url(API_URL)
                .get()
                .build();

        // 비동기 호출
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "API 호출 실패", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Log.d(TAG, "응답 받은 데이터: " + jsonResponse);

                    // 받은 JSON 데이터를 파일로 저장
                    saveJsonToFile(jsonResponse);
                } else {
                    Log.e(TAG, "API 호출 실패: " + response.message());
                }
            }
        });
    }

    private void saveJsonToFile(String jsonData) {
        try {
            // 파일을 앱의 내부 저장소에 저장
            File file = new File(getFilesDir(), FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonData.getBytes());  // JSON 데이터를 그대로 파일에 씁니다.
            fos.close();
            Log.d(TAG, "파일 저장 완료: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "파일 저장 실패", e);
        }
    }

}
