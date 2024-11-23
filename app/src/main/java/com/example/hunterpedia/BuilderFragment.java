package com.example.hunterpedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Pair;

public class BuilderFragment extends Fragment implements OnSkillSelectedListener {

    private Spinner weaponSpinner;
    private List<String> weaponSlots;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> groupList;
    private ArrayList<Pair<String, Integer>> targetSkills = new ArrayList<>();

    @Override
    public void onSkillSelected(SelectedSkill skill, int selectedLevel) {
        boolean skillExists = false;
        // 기존 스킬이 있는지 확인
        for (Pair<String, Integer> skillData : targetSkills) {
            if (skillData.first.equals(skill.getNameEng())) {
                if (selectedLevel == 0) {
                    // "없음"을 선택한 경우, 리스트에서 제거
                    targetSkills.remove(skillData);
                } else {
                    // 이미 존재하는 스킬이면 레벨을 업데이트
                    targetSkills.set(targetSkills.indexOf(skillData), new Pair<>(skill.getNameEng(), selectedLevel));
                }
                skillExists = true;
                break;
            }
        }

        if (!skillExists && selectedLevel > 0) {
            targetSkills.add(new Pair<>(skill.getNameEng(), selectedLevel));  // 스킬의 영문 이름과 요구 레벨을 저장
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.builder_fragment, container, false);

        weaponSpinner = view.findViewById(R.id.weaponspinner);
        expandableListView = view.findViewById(R.id.expandableListView);

        initializeWeaponSlots();
        initializeExpandableListView();

        ArrayAdapter<String> weaponAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, weaponSlots);
        weaponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weaponSpinner.setAdapter(weaponAdapter);

        weaponSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedWeapon = weaponSlots.get(position);
                int[] slotValues = parseSlotValues(selectedWeapon);
                Toast.makeText(requireContext(), "Weapon Slots: " + slotValues[0] + ", " + slotValues[1] + ", " + slotValues[2], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        weaponSpinner.setSelection(0);

        return view;
    }

    // Weapon Spinner 데이터 초기화
    private void initializeWeaponSlots() {
        weaponSlots = new ArrayList<>();
        weaponSlots.add("없음");
        weaponSlots.add("1");
        weaponSlots.add("1, 1");
        weaponSlots.add("1, 1, 1");
        weaponSlots.add("2");
        weaponSlots.add("2, 1");
        weaponSlots.add("2, 2");
        weaponSlots.add("2, 1, 1");
        weaponSlots.add("2, 2, 1");
        weaponSlots.add("2, 2, 2");
        weaponSlots.add("3");
        weaponSlots.add("3, 1");
        weaponSlots.add("3, 2");
        weaponSlots.add("3, 3");
        weaponSlots.add("3, 1, 1");
        weaponSlots.add("3, 2, 1");
        weaponSlots.add("3, 2, 2");
        weaponSlots.add("3, 3, 1");
        weaponSlots.add("3, 3, 2");
        weaponSlots.add("3, 3, 3");
        weaponSlots.add("4");
        weaponSlots.add("4, 1");
        weaponSlots.add("4, 2");
        weaponSlots.add("4, 3");
        weaponSlots.add("4, 4");
        weaponSlots.add("4, 1, 1");
        weaponSlots.add("4, 2, 1");
        weaponSlots.add("4, 2, 2");
        weaponSlots.add("4, 3, 1");
        weaponSlots.add("4, 3, 2");
        weaponSlots.add("4, 3, 3");
        weaponSlots.add("4, 4, 1");
        weaponSlots.add("4, 4, 2");
        weaponSlots.add("4, 4, 3");
        weaponSlots.add("4, 4, 4");
    }

    // ExpandableListView 데이터 초기화
    private void initializeExpandableListView() {
        groupList = new ArrayList<>();
        Map<String, List<SelectedSkill>> groupSkillsMap = new HashMap<>();

        // 그룹 항목 추가
        groupList.add("공격력");
        groupList.add("회심");
        groupList.add("속성/상태이상 강화");
        groupList.add("예리도");

        List<SelectedSkill> attackSkills = new ArrayList<>();
        attackSkills.add(new SelectedSkill("공격력 강화", "attack_boost", createSkillOptions(5), 5));
        attackSkills.add(new SelectedSkill("공격 스킬 2", "attack_skill_2", createSkillOptions(3), 3));

        List<SelectedSkill> critSkills = new ArrayList<>();
        critSkills.add(new SelectedSkill("회심", "critical", createSkillOptions(3), 3));
        critSkills.add(new SelectedSkill("회심 스킬 2", "critical_skill_2", createSkillOptions(2), 2));

        List<SelectedSkill> affinitySkills = new ArrayList<>();
        affinitySkills.add(new SelectedSkill("속성 강화", "element_boost", createSkillOptions(3), 3));

        List<SelectedSkill> sharpnessSkills = new ArrayList<>();
        sharpnessSkills.add(new SelectedSkill("예리도 스킬 1", "sharpness_boost", createSkillOptions(5), 5));


        // 각 그룹에 스킬 리스트 추가
        groupSkillsMap.put(groupList.get(0), attackSkills);
        groupSkillsMap.put(groupList.get(1), critSkills);
        groupSkillsMap.put(groupList.get(2), affinitySkills);
        groupSkillsMap.put(groupList.get(3), sharpnessSkills);

        expandableListAdapter = new SkillListAdapter(requireContext(), groupList, groupSkillsMap, this);
        expandableListView.setAdapter(expandableListAdapter);
    }

    // Weapon 슬롯 값을 정수 배열로 파싱
    private int[] parseSlotValues(String slotText) {
        if ("없음".equals(slotText)) {
            return new int[]{0, 0, 0};
        }
        String[] parts = slotText.split(", ");
        int[] slots = new int[3];
        for (int i = 0; i < parts.length; i++) {
            slots[i] = Integer.parseInt(parts[i]);
        }
        return slots;
    }

    private List<String> createSkillOptions(int maxLevel) {
        List<String> options = new ArrayList<>();
        options.add("없음");
        for (int i = 1; i <= maxLevel; i++) {
            options.add("Level " + i);
        }
        return options;
    }
}


