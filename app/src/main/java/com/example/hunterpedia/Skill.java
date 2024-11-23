package com.example.hunterpedia;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Skill {
    @SerializedName("id")
    private int id;

    @SerializedName("slug")
    private String slug;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("ranks")
    private List<Rank> ranks;

    // Getters
    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public static class Rank {
        @SerializedName("id")
        private int id;

        @SerializedName("slug")
        private String slug;

        @SerializedName("skill")
        private int skillId;

        @SerializedName("level")
        private int level;

        @SerializedName("description")
        private String description;

        @SerializedName("modifiers")
        private Modifiers modifiers;

        // Getters
        public int getId() {
            return id;
        }

        public String getSlug() {
            return slug;
        }

        public int getSkillId() {
            return skillId;
        }

        public int getLevel() {
            return level;
        }

        public String getDescription() {
            return description;
        }

        public Modifiers getModifiers() {
            return modifiers;
        }
    }

    public static class Modifiers {
        @SerializedName("Modifier")
        private int Modifier;

        // Getter
        public int getModifier() {
            return Modifier;
        }
    }
}

