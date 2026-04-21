package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {

    private final CopyOnWriteArrayList<Viking> vikings = new CopyOnWriteArrayList<>();
    private final VikingFactory vikingFactory;

    @Autowired
    public VikingService(VikingFactory vikingFactory) {
        this.vikingFactory = vikingFactory;
    }

    public List<Viking> findAll() {
        return List.copyOf(vikings);
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        vikings.add(viking);
        return viking;
    }

    public Viking createViking(CreateVikingRequest request) {
        Viking viking = new Viking(
                request.name(),
                request.age(),
                request.heightCm(),
                request.hairColor(),
                request.beardStyle(),
                request.equipment()
        );
        vikings.add(viking);
        return viking;
    }

    public boolean deleteVikingByIndex(int index) {
        if (index >= 0 && index < vikings.size()) {
            vikings.remove(index);
            return true;
        }
        return false;
    }

    public boolean deleteVikingByName(String name) {
        return vikings.removeIf(viking -> viking.name().equalsIgnoreCase(name));
    }

    public Viking updateViking(int index, UpdateVikingRequest request) {
        if (index < 0 || index >= vikings.size()) {
            throw new RuntimeException("Viking not found at index: " + index);
        }

        Viking oldViking = vikings.get(index);
        Viking updatedViking = new Viking(
                request.name() != null ? request.name() : oldViking.name(),
                request.age() != null ? request.age() : oldViking.age(),
                request.heightCm() != null ? request.heightCm() : oldViking.heightCm(),
                request.hairColor() != null ? request.hairColor() : oldViking.hairColor(),
                request.beardStyle() != null ? request.beardStyle() : oldViking.beardStyle(),
                request.equipment() != null ? request.equipment() : oldViking.equipment()
        );

        vikings.set(index, updatedViking);
        return updatedViking;
    }
}