package ru.mephi.vikingdemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import ru.mephi.vikingdemo.model.CreateVikingRequest;
import ru.mephi.vikingdemo.model.UpdateVikingRequest;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Viking API", description = "Управление викингами")
public class VikingController {

    private final VikingService vikingService;

    public VikingController(VikingService vikingService) {
        this.vikingService = vikingService;
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Получить всех викингов")
    public List<Viking> getAllVikings() {
        return vikingService.getAllVikings();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Получить викинга по ID")
    public ResponseEntity<Viking> getVikingById(@PathVariable Integer id) {
        return vikingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/random", produces = "application/json")
    @Operation(summary = "Создать случайного викинга")
    public Viking createRandomViking() {
        Viking viking = vikingService.createRandomViking();
        return viking;
    }

    @PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Создать викинга вручную")
    public Viking createVikingManually(@RequestBody CreateVikingRequest request) {
        Viking viking = vikingService.createVikingManually(request);
        return viking;
    }

    @PostMapping(value = "/mass/{count}", produces = "application/json")
    @Operation(summary = "Массовая генерация викингов")
    public String massGenerateVikings(@PathVariable int count) {
        vikingService.generateRandomVikings(count);
        return "Created " + count + " vikings";
    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Обновить викинга по ID")
    public ResponseEntity<Viking> updateViking(@PathVariable Integer id, @RequestBody UpdateVikingRequest request) {
        return vikingService.updateViking(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    @Operation(summary = "Удалить всех викингов")
    public void clearAllVikings() {
        vikingService.clear();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удалить викинга по ID")
    public ResponseEntity<Void> deleteVikingById(@PathVariable Integer id) {
        boolean deleted = vikingService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}