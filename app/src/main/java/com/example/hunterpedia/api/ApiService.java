package com.example.hunterpedia.api;

import com.example.hunterpedia.datastructure.Armor;
import com.example.hunterpedia.datastructure.Skill;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("skills") // https://mhw-db.com/skills 엔드포인트를 호출
    Call<List<Skill>> getSkills();

    @GET("armor?q={\"rank\":\"master\"}")
    Call<List<Armor>> getMasterArmors();

    @GET("armor?q={\"rank\":\"high\"}")
    Call<List<Armor>> getHighArmors();
}
