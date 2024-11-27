package com.example.hunterpedia.datastructure;

import java.util.List;

public class Charm {
    private int id;
    private String slug;
    private String name;
    private List<CharmRank> ranks;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<CharmRank> getRanks() {
        return ranks;
    }

    public static class CharmRank {
        private int level;
        private int rarity;
        private List<Skill.Rank> skills;
        private CraftingInfo crafting;

        public int getLevel() {
            return level;
        }
        public int getRarity() {
            return rarity;
        }
        public List<Skill.Rank> getSkills() {
            return skills;
        }
        public CraftingInfo getCrafting() {
            return crafting;
        }

        public static class CraftingInfo {
            private boolean craftable;
            private List<CraftingMaterial> materials;

            // Getters and Setters
            public boolean isCraftable() {
                return craftable;
            }

            public List<CraftingMaterial> getMaterials() {
                return materials;
            }

            public static class CraftingMaterial {
                private int quantity;
                private Item item;

                // Getters and Setters
                public int getQuantity() {
                    return quantity;
                }

                public void setQuantity(int quantity) {
                    this.quantity = quantity;
                }

                public Item getItem() {
                    return item;
                }

                public void setItem(Item item) {
                    this.item = item;
                }

                public static class Item {
                    private int id;
                    private String name;
                    private String description;
                    private int rarity;
                    private int carryLimit;
                    private int sellPrice;
                    private int buyPrice;

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
}
