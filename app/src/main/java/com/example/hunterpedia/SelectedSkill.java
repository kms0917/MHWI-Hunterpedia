package com.example.hunterpedia;

import java.util.List;

public class SelectedSkill {
    private String name;       // 표시할 이름
    private String nameEng;    // 내부적으로 사용할 이름
    private List<String> spinnerOptions;  // Spinner에 표시할 옵션 리스트
    private int maxLevel;
    private int selectedLevel;  // 선택된 레벨을 저장하는 필드 추가

    public SelectedSkill(String name, String nameEng, List<String> spinnerOptions, int maxLevel) {
        this.name = name;
        this.nameEng = nameEng;
        this.spinnerOptions = spinnerOptions;
        this.maxLevel = maxLevel;
        this.selectedLevel = 0;
    }

    public String getName() {
        return name;
    }

    public String getNameEng() {
        return nameEng;
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