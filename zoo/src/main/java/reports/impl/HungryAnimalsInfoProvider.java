package reports.impl;

import animals.Animal;
import com.bmstu_bureau_1440.io.IO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reports.ProvidesInfo;

import java.util.Arrays;

@RequiredArgsConstructor
public class HungryAnimalsInfoProvider implements ProvidesInfo {

    @NonNull
    Animal[] animals;

    @Override
    public void provideInfo() {
        var hungryAnimals = Arrays.stream(animals).filter(Animal::isHungry).toList();

        if (hungryAnimals.isEmpty()) {
            IO.displaySuccess("Всё в порядке, голодных животных нет!");
        } else {
            IO.displayWarning("Голодные животные: "
                    + String.join(", ", hungryAnimals.stream().map(Animal::toString).toList()));
        }
    }
}
