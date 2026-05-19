package ru.mephi.vikingdemo.service;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Quality;
import ru.mephi.vikingdemo.model.EquipmentItem;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class VikingLambdaService {

    private final VikingService vikingService;

    public VikingLambdaService(VikingService vikingService) {
        this.vikingService = vikingService;
    }

    public long getWarriorsOlderThan(int age) {
        return vikingService.getAllVikings().stream()
                .filter(v -> v.getAge() > age)
                .count();
    }

    public long getWarriorsYoungerThan(int age) {
        return vikingService.getAllVikings().stream()
                .filter(v -> v.getAge() < age)
                .count();
    }

    public long getWarriorsAgeBetween(int minAge, int maxAge) {
        return vikingService.getAllVikings().stream()
                .filter(v -> v.getAge() >= minAge && v.getAge() <= maxAge)
                .count();
    }

    public long getWarriorsAgeOutsideRange(int low, int high) {
        return vikingService.getAllVikings().stream()
                .filter(v -> v.getAge() < low || v.getAge() > high)
                .count();
    }

    public long countWithSpecificBeardAndHair(BeardStyle beardTarget, HairColor hairTarget) {
        return vikingService.getAllVikings().stream()
                .filter(v -> v.getBeardStyle() == beardTarget && v.getHairColor() == hairTarget)
                .count();
    }

    public long countWarriorsWithSingleAxe() {
        return vikingService.getAllVikings().stream()
                .filter(v -> {
                    long axeCount = v.getEquipment().stream()
                            .filter(item -> item.getName().toLowerCase().contains("топор"))
                            .count();
                    return axeCount == 1;
                })
                .count();
    }

    public long countWarriorsWithDoubleAxe() {
        return vikingService.getAllVikings().stream()
                .filter(v -> {
                    long axeCount = v.getEquipment().stream()
                            .filter(item -> item.getName().toLowerCase().contains("топор"))
                            .count();
                    return axeCount == 2;
                })
                .count();
    }

    public Optional<Viking> pickRandomTallWarrior() {
        List<Viking> tallOnes = vikingService.getAllVikings().stream()
                .filter(v -> v.getHeightCm() > 180)
                .collect(Collectors.toList());
        if (tallOnes.isEmpty()) {
            return Optional.empty();
        }
        Random selector = new Random();
        return Optional.of(tallOnes.get(selector.nextInt(tallOnes.size())));
    }

    public List<Viking> fetchAllWithLegendaryGear() {
        return vikingService.getAllVikings().stream()
                .filter(v -> v.getEquipment().stream()
                        .anyMatch(item -> item.getQuality() == Quality.LEGENDARY))
                .collect(Collectors.toList());
    }

    public List<Viking> getRedHairedSortedByIncreasingAge() {
        return vikingService.getAllVikings().stream()
                .filter(v -> v.getHairColor() == HairColor.RED)
                .sorted(Comparator.comparingInt(Viking::getAge))
                .collect(Collectors.toList());
    }

    public Optional<Integer> locateMaxIndex() {
        List<Viking> warriors = vikingService.getAllVikings();
        if (warriors.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(warriors.size() - 1);
    }

    public List<Integer> extractEvenPositions() {
        List<Viking> warriors = vikingService.getAllVikings();
        List<Integer> evenIndices = new ArrayList<>();
        for (int i = 0; i < warriors.size(); i++) {
            if (i % 2 == 0) {
                evenIndices.add(i);
            }
        }
        return evenIndices;
    }
}