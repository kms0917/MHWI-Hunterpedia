package com.example.hunterpedia.weapon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunterpedia.R;
import com.example.hunterpedia.datastructure.Weapon;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeaponAdapter extends RecyclerView.Adapter<WeaponAdapter.WeaponViewHolder> {

    private List<Weapon> weaponList;

    public WeaponAdapter(List<Weapon> weaponList) {
        this.weaponList = weaponList;
    }

    @NonNull
    @Override
    public WeaponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weapon_item, parent, false);
        return new WeaponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeaponViewHolder holder, int position) {
        Weapon weapon = weaponList.get(position);

        holder.weaponName.setText(weapon.getName());
        holder.weaponType.setText("Type: " + weapon.getType());
        holder.weaponRarity.setText("Rarity: " + weapon.getRarity());

        // 공격력 설정
        if (weapon.getAttack() != null) {
            holder.weaponAttack.setText("Attack: " + weapon.getAttack().getDisplay());
        } else {
            holder.weaponAttack.setText("Attack: N/A");
        }

        // 슬롯 설정
        if (weapon.getSlots() != null && !weapon.getSlots().isEmpty()) {
            StringBuilder slots = new StringBuilder("Slots: ");
            for (Weapon.Slot slot : weapon.getSlots()) {
                slots.append("[").append(slot.getRank()).append("] ");
            }
            holder.weaponSlots.setText(slots.toString());
        } else {
            holder.weaponSlots.setText("Slots: None");
        }

        // 속성 설정
        if (weapon.getElements() != null && !weapon.getElements().isEmpty()) {
            StringBuilder elements = new StringBuilder("Element: ");
            for (Weapon.WeaponElement element : weapon.getElements()) {
                elements.append(element.getType())
                        .append(" (")
                        .append(element.getDamage())
                        .append(")");
            }
            holder.weaponElements.setText(elements.toString());
        } else {
            holder.weaponElements.setText("Element: None");
        }

        // Picasso를 이용해 이미지 로드
        if (weapon.getAssets() != null && weapon.getAssets().getIcon() != null) {
            Picasso.get().load(weapon.getAssets().getIcon()).into(holder.weaponImage);
        }
    }


    @Override
    public int getItemCount() {
        return weaponList.size();
    }

    public void updateList(List<Weapon> newList) {
        weaponList = newList;
        notifyDataSetChanged();
    }

    static class WeaponViewHolder extends RecyclerView.ViewHolder {
        TextView weaponName, weaponType, weaponRarity, weaponAttack, weaponSlots, weaponElements;
        ImageView weaponImage;

        WeaponViewHolder(View itemView) {
            super(itemView);
            weaponName = itemView.findViewById(R.id.weaponName);
            weaponType = itemView.findViewById(R.id.weaponType);
            weaponRarity = itemView.findViewById(R.id.weaponRarity);
            weaponImage = itemView.findViewById(R.id.weaponImage);
            weaponAttack = itemView.findViewById(R.id.weaponAttack);
            weaponSlots = itemView.findViewById(R.id.weaponSlots);
            weaponElements = itemView.findViewById(R.id.weaponElements);
        }
    }
}
