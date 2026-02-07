package staff.impl;

import animals.Animal;
import lombok.NonNull;
import staff.Feeds;

import java.time.LocalDateTime;

public class ZooKeeper implements Feeds {

    @Override
    public void feed(@NonNull Animal animal) throws IllegalStateException {
        if (animal.isHungry()) {
            animal.setLastFeedingDateTime(LocalDateTime.now());
        } else {
            throw new IllegalStateException("Животное не голодно!");
        }
    }

}
