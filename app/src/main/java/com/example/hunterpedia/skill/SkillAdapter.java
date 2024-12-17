package com.example.hunterpedia.skill;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunterpedia.R;
import com.example.hunterpedia.datastructure.Skill;

import java.util.ArrayList;
import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {

    private List<Skill> skillList;
    private List<Skill> skillListFull; // 전체 스킬 목록 (필터링 원본)

    public SkillAdapter(List<Skill> skillList) {
        this.skillList = skillList;
        this.skillListFull = new ArrayList<>(skillList); // 원본 목록 저장
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skill_item, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        Skill skill = skillList.get(position);
        holder.skillName.setText(skill.getName());
        int maxLevel = skill.getRanks() != null ? skill.getRanks().size() : 0;
        holder.skillMaxLevel.setText("Max Level: " + maxLevel);
        holder.skillDescription.setText(skill.getDescription());
    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView skillName, skillMaxLevel, skillDescription;

        public SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            skillName = itemView.findViewById(R.id.skillName);
            skillMaxLevel = itemView.findViewById(R.id.skillMaxLevel);
            skillDescription = itemView.findViewById(R.id.skillDescription);
        }
    }

    // 필터링 메서드
    public void filter(String text) {
        skillList.clear();
        if (text.isEmpty()) {
            skillList.addAll(skillListFull); // 검색어가 없으면 전체 목록 표시
        } else {
            text = text.toLowerCase();
            for (Skill skill : skillListFull) {
                if (skill.getName().toLowerCase().contains(text) ||
                        skill.getDescription().toLowerCase().contains(text)) {
                    skillList.add(skill);
                }
            }
        }
        notifyDataSetChanged(); // RecyclerView 갱신
    }
}

