package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.*;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    public List<Viking> getAllVikings() {
        return vikingService.findAll();
    }

    @GetMapping("/test")
    public List<String> test() {
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    public void addViking() {
        vikingListener.testAdd();
    }

    @PostMapping
    public Viking createViking(@RequestBody CreateVikingRequest request) {
        Viking newViking = vikingService.createViking(request);
        vikingListener.refreshGui();
        return newViking;
    }

    @DeleteMapping("/{index}")
    public ResponseEntity<String> deleteVikingByIndex(@PathVariable int index) {
        boolean deleted = vikingService.deleteVikingByIndex(index);
        if (deleted) {
            vikingListener.refreshGui();
            return ResponseEntity.ok("Viking at index " + index + " deleted");
        }
        return ResponseEntity.status(404).body("Viking not found at index: " + index);
    }

    @DeleteMapping("/by-name/{name}")
    public ResponseEntity<String> deleteVikingByName(@PathVariable String name) {
        boolean deleted = vikingService.deleteVikingByName(name);
        if (deleted) {
            vikingListener.refreshGui();
            return ResponseEntity.ok("Viking with name " + name + " deleted");
        }
        return ResponseEntity.status(404).body("Viking not found with name: " + name);
    }

    @PutMapping("/{index}")
    public ResponseEntity<?> updateViking(@PathVariable int index, @RequestBody UpdateVikingRequest request) {
        try {
            Viking updated = vikingService.updateViking(index, request);
            vikingListener.refreshGui();
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}