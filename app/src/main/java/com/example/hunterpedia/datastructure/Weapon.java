package com.example.hunterpedia.datastructure;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weapon {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("rarity")
    private int rarity;

    @SerializedName("attack")
    private Attack attack;

    @SerializedName("elderseal")
    private String elderseal;

    @SerializedName("damageType")
    private String damageType;

    @SerializedName("attributes")
    private Attributes attributes;

    @SerializedName("durability")
    private List<WeaponSharpness> durability;

    @SerializedName("slots")
    private List<Slot> slots;

    @SerializedName("elements")
    private List<WeaponElement> elements;

    @SerializedName("crafting")
    private WeaponCraftingInfo crafting;

    @SerializedName("assets")
    private WeaponAssets assets;

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getRarity() { return rarity; }
    public Attack getAttack() { return attack; }
    public String getElderseal() { return elderseal; }
    public String getDamageType() { return damageType; }
    public Attributes getAttributes() { return attributes; }
    public List<WeaponSharpness> getDurability() { return durability; }
    public List<Slot> getSlots() { return slots; }
    public List<WeaponElement> getElements() { return elements; }
    public WeaponCraftingInfo getCrafting() { return crafting; }
    public WeaponAssets getAssets() { return assets; }

    // Inner Classes
    public static class Attack {
        @SerializedName("display")
        private int display;

        @SerializedName("raw")
        private int raw;

        public int getDisplay() { return display; }
        public int getRaw() { return raw; }
    }

    public static class Attributes {
        @SerializedName("affinity")
        private Integer affinity;

        @SerializedName("defense")
        private Integer defense;

        public Integer getAffinity() { return affinity; }
        public Integer getDefense() { return defense; }
    }

    public static class WeaponSharpness {
        @SerializedName("red")
        private int red;
        @SerializedName("orange")
        private int orange;
        @SerializedName("yellow")
        private int yellow;
        @SerializedName("green")
        private int green;
        @SerializedName("blue")
        private int blue;
        @SerializedName("white")
        private int white;
        @SerializedName("purple")
        private int purple;

        public int getRed() { return red; }
        public int getOrange() { return orange; }
        public int getYellow() { return yellow; }
        public int getGreen() { return green; }
        public int getBlue() { return blue; }
        public int getWhite() { return white; }
        public int getPurple() { return purple; }
    }

    public static class Slot {
        @SerializedName("rank")
        private int rank;

        public int getRank() { return rank; }
    }

    public static class WeaponElement {
        @SerializedName("type")
        private String type;

        @SerializedName("damage")
        private int damage;

        @SerializedName("hidden")
        private boolean hidden;

        public String getType() { return type; }
        public int getDamage() { return damage; }
        public boolean isHidden() { return hidden; }
    }

    public static class WeaponCraftingInfo {
        @SerializedName("craftable")
        private boolean craftable;

        @SerializedName("previous")
        private Integer previous;

        @SerializedName("branches")
        private List<Integer> branches;

        @SerializedName("craftingMaterials")
        private List<CraftingCost> craftingMaterials;

        @SerializedName("upgradeMaterials")
        private List<CraftingCost> upgradeMaterials;

        public boolean isCraftable() { return craftable; }
        public Integer getPrevious() { return previous; }
        public List<Integer> getBranches() { return branches; }
        public List<CraftingCost> getCraftingMaterials() { return craftingMaterials; }
        public List<CraftingCost> getUpgradeMaterials() { return upgradeMaterials; }

        public static class CraftingCost {
            @SerializedName("quantity")
            private int quantity;

            @SerializedName("item")
            private CraftingItem item;

            public int getQuantity() { return quantity; }
            public CraftingItem getItem() { return item; }

            public static class CraftingItem {
                @SerializedName("id")
                private int id;

                @SerializedName("name")
                private String name;

                @SerializedName("description")
                private String description;

                @SerializedName("rarity")
                private int rarity;

                public int getId() { return id; }
                public String getName() { return name; }
                public String getDescription() { return description; }
                public int getRarity() { return rarity; }
            }
        }
    }

    public static class WeaponAssets {
        @SerializedName("icon")
        private String icon;

        @SerializedName("image")
        private String image;

        public String getIcon() { return icon; }
        public String getImage() { return image; }
    }
}
