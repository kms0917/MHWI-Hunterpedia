package com.example.hunterpedia.api;

import com.example.hunterpedia.datastructure.Armor;
import com.example.hunterpedia.datastructure.Charm;
import com.example.hunterpedia.datastructure.Decoration;
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

    @GET("armor?q={\"type\":\"head\", \"rank\":\"master\"}")
    Call<List<Armor>> getHeadArmors();

    @GET("armor?q={\"type\":\"chest\", \"rank\":\"master\"}")
    Call<List<Armor>> getChestArmors();

    @GET("armor?q={\"type\":\"gloves\", \"rank\":\"master\"}")
    Call<List<Armor>> getGlovesArmors();

    @GET("armor?q={\"type\":\"waist\", \"rank\":\"master\"}")
    Call<List<Armor>> getWaistArmors();

    @GET("armor?q={\"type\":\"legs\", \"rank\":\"master\"}")
    Call<List<Armor>> getLegsArmors();

    @GET("armor?q={\"rank\":\"high\"}")
    Call<List<Armor>> getHighArmors();

    @GET("decorations")
    Call<List<Decoration>> getDecorations();

    @GET("charms")
    Call<List<Charm>> getCharms();
}
