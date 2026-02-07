package staff;

import animals.Animal;
import lombok.NonNull;

public interface Feeds {
    void feed(@NonNull Animal animal);
}
