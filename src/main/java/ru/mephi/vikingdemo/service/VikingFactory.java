package ru.mephi.vikingdemo.service;

import ru.mephi.vikingdemo.model.*;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class VikingFactory {
    private final Faker faker = new Faker();

    private final List<String> vikingFirstNames = Arrays.asList(
            "Рагнар", "Лагерта", "Бьорн", "Ивар", "Уббе", "Хвитсерк", "Сигурд",
            "Флоки", "Ролло", "Харальд", "Хальвдан", "Эрик", "Лейф", "Гуннар",
            "Астрид", "Фрейдис", "Хельга", "Сигрид", "Брюнхильд", "Тюра"
    );

    private final List<String> vikingLastNames = Arrays.asList(
            "Лотброк", "Рагнарссон", "Железная Рука", "Грозовая Туча",
            "Бескостный", "Волчья Погибель", "Свирепый", "Красный",
            "Молот", "Волк", "Медведь", "Дракон"
    );

    private final List<String> weaponNames = Arrays.asList(
            "Датский топор", "Длинный меч", "Сакс", "Копье", "Боевой топор",
            "Лук", "Щит", "Нож", "Боевой молот", "Великий топор"
    );

    private final List<String> armorNames = Arrays.asList(
            "Кольчуга", "Кожаная броня", "Железный шлем", "Кожаный шлем",
            "Металлические наручи", "Плащ из волка", "Костяная броня"
    );

    private final List<String> accessoryNames = Arrays.asList(
            "Молот Тора", "Кольцо рун", "Глаз Одина", "Зуб Фенрира",
            "Брошь Валькирии", "Чешуя дракона", "Перо ворона"
    );

    public Viking createRandomViking() {
        Viking viking = new Viking();
        String firstName = faker.options().nextElement(vikingFirstNames);
        String lastName = faker.options().nextElement(vikingLastNames);
        viking.setName(firstName + " " + lastName);
        viking.setAge(faker.number().numberBetween(18, 70));
        viking.setHeightCm(faker.number().numberBetween(160, 210));
        viking.setHairColor(faker.options().option(HairColor.class));
        viking.setBeardStyle(faker.options().option(BeardStyle.class));
        viking.setEquipment(generateRandomEquipment());
        return viking;
    }

    private List<EquipmentItem> generateRandomEquipment() {
        int equipmentCount = faker.number().numberBetween(1, 4);
        List<EquipmentItem> equipment = new ArrayList<>();
        for (int i = 0; i < equipmentCount; i++) {
            equipment.add(generateSingleEquipment());
        }
        return equipment;
    }

    private EquipmentItem generateSingleEquipment() {
        String type = faker.options().option("weapon", "armor", "accessory");
        String typeRu = switch (type) {
            case "weapon" -> "оружие";
            case "armor" -> "броня";
            default -> "аксессуар";
        };
        String name = switch (type) {
            case "weapon" -> faker.options().nextElement(weaponNames);
            case "armor" -> faker.options().nextElement(armorNames);
            default -> faker.options().nextElement(accessoryNames);
        };
        Quality quality = generateQuality();
        return new EquipmentItem(name, typeRu, quality);
    }

    private Quality generateQuality() {
        int roll = faker.number().numberBetween(0, 100);
        if (roll < 60) return Quality.COMMON;
        if (roll < 85) return Quality.UNCOMMON;
        if (roll < 97) return Quality.RARE;
        return Quality.LEGENDARY;
    }

    public Viking createManualViking(CreateVikingRequest request) {
        Viking viking = new Viking();
        viking.setName(request.name());
        viking.setAge(request.age());
        viking.setHeightCm(request.heightCm());
        viking.setHairColor(request.hairColor());
        viking.setBeardStyle(request.beardStyle());
        viking.setEquipment(request.equipment());
        return viking;
    }
}