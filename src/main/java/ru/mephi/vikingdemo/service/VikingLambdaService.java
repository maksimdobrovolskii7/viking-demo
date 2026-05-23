package ru.mephi.vikingdemo.service;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Quality;
import ru.mephi.vikingdemo.model.EquipmentItem;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public long countWarriorsWithAxeCount(int axeCount) {
        return vikingService.getAllVikings().stream()
                .filter(v -> {
                    long count = v.getEquipment().stream()
                            .filter(item -> {
                                String name = item.getName().toLowerCase();
                                return name.contains("топор") ||
                                        name.contains("секира") ||
                                        name.contains("бердыш") ||
                                        name.contains("чекан");
                            })
                            .count();
                    return count == axeCount;
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
                .filter(v -> v.getBeardStyle() != BeardStyle.CLEAN_SHAVEN)
                .sorted(Comparator.comparingInt(Viking::getAge))
                .collect(Collectors.toList());
    }

    public Optional<Integer> locateMaxId() {
        Integer[] ids = vikingService.getAllVikings().stream()
                .map(Viking::getId)
                .toArray(Integer[]::new);

        if (ids.length == 0) {
            return Optional.empty();
        }

        return Arrays.stream(ids)
                .max(Integer::compareTo);
    }

    public List<Integer> extractEvenPositionIds() {
        Integer[] ids = vikingService.getAllVikings().stream()
                .map(Viking::getId)
                .toArray(Integer[]::new);

        return IntStream.range(0, ids.length)
                .filter(i -> i % 2 == 0)
                .mapToObj(i -> ids[i])
                .collect(Collectors.toList());
    }}