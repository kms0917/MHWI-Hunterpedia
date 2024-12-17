package com.example.hunterpedia.datastructure;

public class Ailment {
    private int id;
    private String name;
    private String description;
    private Object recovery;  // Customize this further based on your API response
    private Object protection;

    // Getters and Setters
    public int getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Object getRecovery() { return recovery; }

    public Object getProtection() { return protection; }
}
