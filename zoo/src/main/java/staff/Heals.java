package staff;

import animals.Animal;
import lombok.NonNull;

public interface Heals {
    void heal(@NonNull Animal animal);
}
