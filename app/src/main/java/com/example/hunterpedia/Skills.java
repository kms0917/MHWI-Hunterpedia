package com.example.hunterpedia;

import java.util.List;

public class Skills {
    private String name;       // 표시할 이름
    private String nameEng;    // 내부적으로 사용할 이름
    private List<String> spinnerOptions;  // Spinner에 표시할 옵션 리스트
    private int maxLevel;

    public Skills(String name, String nameEng, List<String> spinnerOptions, int maxLevel) {
        this.name = name;
        this.nameEng = nameEng;
        this.spinnerOptions = spinnerOptions;
        this.maxLevel = maxLevel;
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
}
