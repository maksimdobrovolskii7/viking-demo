package ru.mephi.vikingdemo.controller;

import ru.mephi.vikingdemo.model.*;
import ru.mephi.vikingdemo.service.VikingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Получить викинга по ID")
    public ResponseEntity<Viking> getVikingById(@PathVariable String id) {
        return vikingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/random", produces = "application/json")
    @Operation(summary = "Создать случайного викинга (автоматически)")
    public Viking createRandomViking() {
        Viking viking = vikingService.createRandomViking();
        vikingListener.refreshGui();
        return viking;
    }

    @PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Создать викинга вручную (задать параметры)")
    public Viking createVikingManually(@RequestBody CreateVikingRequest request) {
        Viking viking = vikingService.createVikingManually(request);
        vikingListener.refreshGui();
        return viking;
    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Обновить викинга по ID (через текущего)")
    public ResponseEntity<Viking> updateViking(@PathVariable String id, @RequestBody UpdateVikingRequest request) {
        return vikingService.updateViking(id, request)
                .map(viking -> {
                    vikingListener.refreshGui();
                    return ResponseEntity.ok(viking);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    @Operation(summary = "Удалить всех викингов")
    public void clearAllVikings() {
        vikingService.clear();
        vikingListener.refreshGui();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удалить викинга по ID")
    public ResponseEntity<Void> deleteVikingById(@PathVariable String id) {
        boolean deleted = vikingService.deleteById(id);
        if (deleted) {
            vikingListener.refreshGui();
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/count", produces = "application/json")
    @Operation(summary = "Количество викингов")
    public int getCount() {
        return vikingService.getAllVikings().size();
    }
}