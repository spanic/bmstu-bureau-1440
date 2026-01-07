package staff.impl;

import animals.Animal;
import lombok.NonNull;
import staff.Heals;

public class Doctor implements Heals {

    @Override
    public void heal(@NonNull Animal animal) {
        if (!animal.isHealthy()) {
            animal.setHealthy(true);
        } else {
            throw new IllegalStateException("Животное здорово!");
        }
    }

}
