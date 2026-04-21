package ru.mephi.vikingdemo.model;

import java.util.List;

public record UpdateVikingRequest(
        String name,
        Integer age,
        Integer heightCm,
        HairColor hairColor,
        BeardStyle beardStyle,
        List<EquipmentItem> equipment
) {
}