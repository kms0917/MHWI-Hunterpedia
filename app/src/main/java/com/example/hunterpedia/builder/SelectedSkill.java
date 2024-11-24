package com.example.hunterpedia.builder;

import java.util.List;

public class SelectedSkill {
    private String name;       // 표시할 이름
    private List<String> spinnerOptions;  // Spinner에 표시할 옵션 리스트
    private int maxLevel;
    private int selectedLevel;  // 선택된 레벨을 저장하는 필드 추가

    public SelectedSkill(String name, List<String> spinnerOptions, int maxLevel) {
        this.name = name;
        this.spinnerOptions = spinnerOptions;
        this.maxLevel = maxLevel;
        this.selectedLevel = 0;
    }

    public String getName() {
        return name;
    }

    public List<String> getSpinnerOptions() {
        return spinnerOptions;
    }
    public int getMaxLevel() {
        return maxLevel;
    }
    public int getSelectedLevel() {
        return selectedLevel;
    }

    public void setSelectedLevel(int selectedLevel) {
        this.selectedLevel = selectedLevel;
    }
}
