package staff;

import animals.Animal;
import lombok.NonNull;

public interface Cleans {
    void clean(@NonNull Animal animal);
}
