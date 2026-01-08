package reports.impl;

import animals.Animal;
import com.bmstu_bureau_1440.io.IO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reports.ProvidesInfo;

import java.util.Arrays;

@RequiredArgsConstructor
public class NeedsCleanupAnimalsInfoProvider implements ProvidesInfo {

    @NonNull
    Animal[] animals;

    @Override
    public void provideInfo() {

        var animalsNeedsCleanup = Arrays.stream(animals).filter(animal -> !animal.getHome().isClean()).toList();

        if (animalsNeedsCleanup.isEmpty()) {
            IO.displaySuccess("Всё в порядке, уборка не нужна!");
        } else {
            IO.displayWarning("Нужно провести уборку у: "
                    + String.join(", ", animalsNeedsCleanup.stream().map(Animal::toString).toList()));
        }
    }
}
