package com.example.hunterpedia.datastructure;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class Armor {
    @SerializedName("id")
    private int id;

    @SerializedName("slug")
    private String slug;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;    //확인 필요

    @SerializedName("rank")
    private String rank;    //확인 필요

    @SerializedName("rarity")
    private int rarity;

    @SerializedName("defense")
    private Defense defense;

    @SerializedName("resistances")
    private Resistances resistances;

    @SerializedName("slots")
    private List<Slot> slots;

    @SerializedName("skills")
    private List<Skill.Rank> skills;

    @SerializedName("armorSet")
    private SetInfo armorSet;

    @SerializedName("assets")
    private ArmorAssets assets;

    @SerializedName("crafting")
    private ArmorCraftingInfo crafting;

    @SerializedName("attributes")
    private Map<String, String> attributes;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRank() {
        return rank;
    }

    public int getRarity() {
        return rarity;
    }

    public Defense getDefense() {
        return defense;
    }

    public Resistances getResistances() {
        return resistances;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public List<Skill.Rank> getSkills() {
        return skills;
    }

    public SetInfo getArmorSet() {
        return armorSet;
    }

    public ArmorAssets getAssets() {
        return assets;
    }

    public ArmorCraftingInfo getCrafting() {
        return crafting;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public static class Defense {
        @SerializedName("base")
        private int base;

        @SerializedName("max")
        private int max;

        @SerializedName("augmented")
        private int augmented;

        // Getters
        public int getBase() {
            return base;
        }

        public int getMax() {
            return max;
        }

        public int getAugmented() {
            return augmented;
        }
    }

    public static class Resistances{
        @SerializedName("fire")
        private int fire;

        @SerializedName("water")
        private int water;

        @SerializedName("ice")
        private int ice;

        @SerializedName("thunder")
        private int thunder;

        @SerializedName("dragon")
        private int dragon;

        // Getters
        public int getFire() {
            return fire;
        }

        public int getWater() {
            return water;
        }

        public int getIce() {
            return ice;
        }

        public int getThunder() {
            return thunder;
        }

        public int getDragon() {
            return dragon;
        }
    }

    public static class Slot {
        @SerializedName("rank")
        private int rank;

        // Getter
        public int getRank() {
            return rank;
        }
    }

    public static class SetInfo {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("rank")
        private String rank;

        @SerializedName("pieces")
        private List<Integer> pieces;

        // Getters
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getRank() {
            return rank;
        }

        public List<Integer> getPieces() {
            return pieces;
        }
    }

    public static class ArmorAssets {
        @SerializedName("imageMale")
        private String imageMale;

        @SerializedName("imageFemale")
        private String imageFemale;

        // Getters
        public String getImageMale() {
            return imageMale;
        }

        public String getImageFemale() {
            return imageFemale;
        }
    }

    public static class ArmorCraftingInfo {
        @SerializedName("materials")
        private List<CraftingCost> materials;

        // Getter
        public List<CraftingCost> getMaterials() {
            return materials;
        }

        public static class CraftingCost {
            @SerializedName("quantity")
            private int quantity;

            @SerializedName("item")
            private Item item;

            // Getters
            public int getQuantity() {
                return quantity;
            }

            public Item getItem() {
                return item;
            }

            public static class Item {
                @SerializedName("id")
                private int id;

                @SerializedName("name")
                private String name;

                @SerializedName("description")
                private String description;

                @SerializedName("rarity")
                private int rarity;

                @SerializedName("carryLimit")
                private int carryLimit;

                @SerializedName("sellPrice")
                private int sellPrice;

                @SerializedName("buyPrice")
                private int buyPrice;

                // Getters
                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }

                public String getDescription() {
                    return description;
                }

                public int getRarity() {
                    return rarity;
                }

                public int getCarryLimit() {
                    return carryLimit;
                }

                public int getSellPrice() {
                    return sellPrice;
                }

                public int getBuyPrice() {
                    return buyPrice;
                }
            }
        }
    }
}
