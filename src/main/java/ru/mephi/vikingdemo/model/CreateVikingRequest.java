package ru.mephi.vikingdemo.model;

import java.util.List;

public record CreateVikingRequest(
        String name,
        int age,
        int heightCm,
        HairColor hairColor,
        BeardStyle beardStyle,
        List<EquipmentItem> equipment
) {
}