package ru.mephi.vikingdemo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Viking {
    private String id;
    private String name;
    private int age;
    private int heightCm;
    private HairColor hairColor;
    private BeardStyle beardStyle;
    private List<EquipmentItem> equipment;

    public Viking() {
        this.id = UUID.randomUUID().toString();
        this.equipment = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public int getHeightCm() { return heightCm; }
    public HairColor getHairColor() { return hairColor; }
    public BeardStyle getBeardStyle() { return beardStyle; }
    public List<EquipmentItem> getEquipment() { return equipment; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setHeightCm(int heightCm) { this.heightCm = heightCm; }
    public void setHairColor(HairColor hairColor) { this.hairColor = hairColor; }
    public void setBeardStyle(BeardStyle beardStyle) { this.beardStyle = beardStyle; }
    public void setEquipment(List<EquipmentItem> equipment) { this.equipment = equipment; }
}