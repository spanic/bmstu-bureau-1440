package reports.impl;

import animals.Animal;
import com.bmstu_bureau_1440.io.IO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reports.ProvidesInfo;

import java.util.Arrays;

@RequiredArgsConstructor
public class SickAnimalsInfoProvider implements ProvidesInfo {

    @NonNull
    Animal[] animals;

    @Override
    public void provideInfo() {
        var sickAnimals = Arrays.stream(animals).filter(animal -> !animal.isHealthy()).toList();

        if (sickAnimals.isEmpty()) {
            IO.displaySuccess("Всё в порядке, больных животных нет!");
        } else {
            IO.displayWarning("Больные животные: "
                    + String.join(", ", sickAnimals.stream().map(Animal::toString).toList()));
        }
    }

}
