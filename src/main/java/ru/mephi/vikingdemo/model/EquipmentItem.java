package ru.mephi.vikingdemo.model;

public class EquipmentItem {
    private String name;
    private String type;
    private Quality quality;

    public EquipmentItem() {}

    public EquipmentItem(String name, String type) {
        this.name = name;
        this.type = type;
        this.quality = Quality.COMMON;
    }

    public EquipmentItem(String name, String type, Quality quality) {
        this.name = name;
        this.type = type;
        this.quality = quality;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Quality getQuality() { return quality; }
    public void setQuality(Quality quality) { this.quality = quality; }
}