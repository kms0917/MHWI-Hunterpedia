package com.example.hunterpedia.datastructure;

import java.util.List;

public class Monster {
    private int id;
    private String name;
    private String type;
    private String species;
    private String description;
    private List<String> elements;
    private List<Ailment> ailments;
    private List<Location> locations;
    private List<MonsterResistance> resistances;
    private List<MonsterWeakness> weaknesses;
    private List<MonsterReward> rewards;

    // Getters and Setters
    public int getId() { return id; }

    public String getName() { return name; }

    public String getType() { return type; }

    public String getSpecies() { return species; }

    public String getDescription() { return description; }

    public List<String> getElements() { return elements; }

    public List<Ailment> getAilments() { return ailments; }

    public List<Location> getLocations() { return locations; }

    public List<MonsterResistance> getResistances() { return resistances; }

    public List<MonsterWeakness> getWeaknesses() { return weaknesses; }

    public List<MonsterReward> getRewards() { return rewards; }
}
