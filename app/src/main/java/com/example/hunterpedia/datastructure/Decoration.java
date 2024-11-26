package com.example.hunterpedia.datastructure;

import java.util.List;

public class Decoration {
    private int id;                // The ID of the decoration
    private String slug;           // A human-readable unique identifier
    private String name;           // The name of the decoration
    private int rarity;            // The rarity of the decoration
    private int slot;              // The slot that the decoration fits into
    private List<Skill.Rank> skills; // An array of skill ranks that the decoration provides

    public int getId() {
        return id;
    }
    public String getSlug() {
        return slug;
    }
    public String getName() {
        return name;
    }
    public int getRarity() {
        return rarity;
    }
    public int getSlot() {
        return slot;
    }
    public List<Skill.Rank> getSkills() {
        return skills;
    }
}
