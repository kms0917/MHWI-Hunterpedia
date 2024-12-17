package com.example.hunterpedia.datastructure;

import java.util.List;

public class MonsterReward {
    private int id;
    private Object item; // Replace this with an Item class if detailed
    private List<Object> conditions;

    // Getters and Setters
    public int getId() { return id; }

    public Object getItem() { return item; }

    public List<Object> getConditions() { return conditions; }
}
