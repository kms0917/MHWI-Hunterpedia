package com.example.hunterpedia.armor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunterpedia.R;
import com.example.hunterpedia.datastructure.Armor;
import com.example.hunterpedia.datastructure.Skill;

import java.util.ArrayList;
import java.util.List;

public class ArmorAdapter extends RecyclerView.Adapter<ArmorAdapter.ArmorViewHolder> {

    private List<Armor> armorList;           // 필터링된 리스트
    private List<Armor> armorListFull;       // 원본 리스트

    public ArmorAdapter(List<Armor> armorList) {
        this.armorList = armorList;
        this.armorListFull = new ArrayList<>(armorList);
    }

    @NonNull
    @Override
    public ArmorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.armor_item, parent, false);
        return new ArmorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArmorViewHolder holder, int position) {
        Armor armor = armorList.get(position);
        holder.armorName.setText(armor.getName());
        holder.armorTypeRank.setText("Type: " + armor.getType() + ", Rank: " + armor.getRank());

        // 방어력 정보
        if (armor.getDefense() != null) {
            holder.armorDefense.setText("Defense: Base " + armor.getDefense().getBase() +
                    ", Max " + armor.getDefense().getMax() +
                    ", Augmented " + armor.getDefense().getAugmented());
        } else {
            holder.armorDefense.setText("Defense: N/A");
        }

        // 슬롯 정보
        StringBuilder slots = new StringBuilder("Slots: ");
        if (armor.getSlots() != null && !armor.getSlots().isEmpty()) {
            for (Armor.Slot slot : armor.getSlots()) {
                slots.append("[").append(slot.getRank()).append("] ");
            }
        } else {
            slots.append("N/A");
        }
        holder.armorSlots.setText(slots.toString());

        // 스킬 정보
        StringBuilder skills = new StringBuilder("Skills: ");
        if (armor.getSkills() != null && !armor.getSkills().isEmpty()) {
            for (int i = 0; i < armor.getSkills().size(); i++) {
                Skill.Rank skill = armor.getSkills().get(i);
                skills.append(skill.getSkillName()).append(" Lv.").append(skill.getLevel());
                if (i < armor.getSkills().size() - 1) skills.append(", ");
            }
        } else {
            skills.append("N/A");
        }
        holder.armorSkills.setText(skills.toString());
    }

    @Override
    public int getItemCount() {
        return armorList.size();
    }

    /**
     * 리스트 업데이트 메서드 - 외부에서 새로운 리스트로 업데이트
     */
    public void updateList(List<Armor> newList) {
        armorList.clear();
        armorList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class ArmorViewHolder extends RecyclerView.ViewHolder {
        TextView armorName, armorTypeRank, armorDefense, armorSlots, armorSkills;

        public ArmorViewHolder(@NonNull View itemView) {
            super(itemView);
            armorName = itemView.findViewById(R.id.armorName);
            armorTypeRank = itemView.findViewById(R.id.armorTypeRank);
            armorDefense = itemView.findViewById(R.id.armorDefense);
            armorSlots = itemView.findViewById(R.id.armorSlots);
            armorSkills = itemView.findViewById(R.id.armorSkills);
        }
    }
}

