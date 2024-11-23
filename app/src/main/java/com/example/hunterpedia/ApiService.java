package com.example.hunterpedia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("skills") // https://mhw-db.com/skills 엔드포인트를 호출
    Call<List<Skill>> getSkills();
}
