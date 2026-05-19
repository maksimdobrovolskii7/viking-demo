package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/analysis")
@Tag(name = "Lambda Analysis", description = "Аналитические операции через Stream API")
public class VikingLambdaController {

    private final VikingLambdaService lambdaService;

    public VikingLambdaController(VikingLambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    @GetMapping("/age/older/{value}")
    @Operation(summary = "Подсчитать викингов старше указанного возраста")
    public long countOlderThan(@PathVariable int value) {
        return lambdaService.getWarriorsOlderThan(value);
    }

    @GetMapping("/age/younger/{value}")
    @Operation(summary = "Подсчитать викингов младше указанного возраста")
    public long countYoungerThan(@PathVariable int value) {
        return lambdaService.getWarriorsYoungerThan(value);
    }

    @GetMapping("/age/range")
    @Operation(summary = "Подсчитать викингов в возрастном промежутке")
    public long countInAgeRange(@RequestParam int from, @RequestParam int to) {
        return lambdaService.getWarriorsAgeBetween(from, to);
    }

    @GetMapping("/age/outside")
    @Operation(summary = "Подсчитать викингов вне возрастного промежутка")
    public long countOutsideAgeRange(@RequestParam int low, @RequestParam int high) {
        return lambdaService.getWarriorsAgeOutsideRange(low, high);
    }

    @GetMapping("/beard-hair/count")
    @Operation(summary = "Подсчитать по стилю бороды и цвету волос")
    public long countByBeardAndHairStyle(@RequestParam BeardStyle beard, @RequestParam HairColor hair) {
        return lambdaService.countWithSpecificBeardAndHair(beard, hair);
    }

    @GetMapping("/weapons/axe/count/{number}")
    @Operation(summary = "Количество викингов с указанным количеством топоров (1 или 2)")
    public long countWithAxeCount(@PathVariable int number) {
        return lambdaService.countWarriorsWithAxeCount(number);
    }

    @GetMapping("/random/tall")
    @Operation(summary = "Случайный высокий викинг (выше 180 см)")
    public ResponseEntity<Viking> getRandomTallOne() {
        Optional<Viking> result = lambdaService.pickRandomTallWarrior();
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/legendary/all")
    @Operation(summary = "Все викинги с легендарным снаряжением")
    public List<Viking> getAllLegendaryEquipped() {
        return lambdaService.fetchAllWithLegendaryGear();
    }

    @GetMapping("/red-haired/sorted")
    @Operation(summary = "Рыжеволосые викинги, отсортированные по возрасту")
    public List<Viking> getRedHairedSortedByAge() {
        return lambdaService.getRedHairedSortedByIncreasingAge();
    }

    // Исправленные методы - работаем с ID, а не с индексами
    @GetMapping("/ids/max")
    @Operation(summary = "Максимальный ID в списке (лексикографически)")
    public ResponseEntity<String> getMaxId() {
        Optional<String> maxId = lambdaService.locateMaxId();
        return maxId.map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/ids/even-positions")
    @Operation(summary = "ID викингов на чётных позициях (0, 2, 4...)")
    public List<String> getEvenPositionIds() {
        return lambdaService.extractEvenPositionIds();
    }
}