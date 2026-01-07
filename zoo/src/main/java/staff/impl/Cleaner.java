package staff.impl;

import animals.Animal;
import lombok.NonNull;
import staff.Cleans;

public class Cleaner implements Cleans {

    @Override
    public void clean(@NonNull Animal animal) {
        var home = animal.getHome();
        if (!home.isClean()) {
            home.setClean(true);
        } else {
            throw new IllegalStateException("Уборка уже была проведена!");
        }
    }

}
