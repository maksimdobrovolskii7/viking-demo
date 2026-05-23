package ru.mephi.vikingdemo.service;

import ru.mephi.vikingdemo.model.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Service
public class VikingService {
    private final Map<Integer, Viking> vikings = new ConcurrentHashMap<>();
    private final VikingFactory vikingFactory;

    public VikingService(VikingFactory vikingFactory) {
        this.vikingFactory = vikingFactory;
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        vikings.put(viking.getId(), viking);
        return viking;
    }

    public void generateRandomVikings(int count) {
        IntStream.range(0, count).forEach(i -> createRandomViking());
    }

    public Viking createVikingManually(CreateVikingRequest request) {
        Viking viking = vikingFactory.createManualViking(request);
        vikings.put(viking.getId(), viking);
        return viking;
    }

    

    public Optional<Viking> updateViking(Integer id, UpdateVikingRequest request) {
        Viking existing = vikings.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        if (request.name() != null) existing.setName(request.name());
        if (request.age() != null) existing.setAge(request.age());
        if (request.heightCm() != null) existing.setHeightCm(request.heightCm());
        if (request.hairColor() != null) existing.setHairColor(request.hairColor());
        if (request.beardStyle() != null) existing.setBeardStyle(request.beardStyle());
        if (request.equipment() != null) existing.setEquipment(request.equipment());
        return Optional.of(existing);
    }

    public List<Viking> getAllVikings() {
        return new ArrayList<>(vikings.values());
    }

    public List<Viking> findAll() {
        return new ArrayList<>(vikings.values());
    }

    public Optional<Viking> findById(Integer id) {
        return Optional.ofNullable(vikings.get(id));
    }

    public boolean deleteById(Integer id) {
        return vikings.remove(id) != null;
    }

    public void clear() {
        vikings.clear();
    }

    public void addViking(Viking viking) {
        vikings.put(viking.getId(), viking);
    }
}