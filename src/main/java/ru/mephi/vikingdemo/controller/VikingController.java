package ru.mephi.vikingdemo.controller;

import ru.mephi.vikingdemo.model.*;
import ru.mephi.vikingdemo.service.VikingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Viking API", description = "Управление викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Получить всех викингов")
    public List<Viking> getAllVikings() {
        return vikingService.getAllVikings();
    }

    @PostMapping(value = "/random", produces = "application/json")
    @Operation(summary = "Создать случайного викинга (автоматически)")
    public Viking createRandomViking() {
        Viking viking = vikingService.createRandomViking();
        vikingListener.refreshGui();
        return viking;
    }

    // ← НОВЫЙ МЕТОД: ручное создание викинга
    @PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Создать викинга вручную (задать параметры)")
    public Viking createVikingManually(@RequestBody CreateVikingRequest request) {
        Viking viking = new Viking();
        viking.setName(request.name());
        viking.setAge(request.age());
        viking.setHeightCm(request.heightCm());
        viking.setHairColor(request.hairColor());
        viking.setBeardStyle(request.beardStyle());
        viking.setEquipment(request.equipment());

        vikingService.addViking(viking);  // нужен этот метод в сервисе
        vikingListener.refreshGui();
        return viking;
    }

    @DeleteMapping
    @Operation(summary = "Удалить всех викингов")
    public void clearAllVikings() {
        vikingService.clear();
        vikingListener.refreshGui();
    }

    @GetMapping(value = "/count", produces = "application/json")
    @Operation(summary = "Количество викингов")
    public int getCount() {
        return vikingService.getAllVikings().size();
    }
}