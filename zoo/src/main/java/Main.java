import java.time.LocalDateTime;
import java.util.stream.Stream;

import com.bmstu_bureau_1440.shared.io.IO;
import com.bmstu_bureau_1440.shared.io.MenuSelector;

import animals.Animal;
import animals.Region;
import animals.impl.Parrot;
import animals.impl.Shark;
import animals.impl.Tiger;
import facilities.Home;
import io.Operation;
import reports.ReportGenerator;
import reports.impl.HungryAnimalsInfoProvider;
import reports.impl.NeedsCleanupAnimalsInfoProvider;
import reports.impl.SickAnimalsInfoProvider;
import staff.impl.Cleaner;
import staff.impl.Doctor;
import staff.impl.ZooKeeper;

public class Main {

    public static void main(String[] args) {
        new Main.Zoo().run();
    }

    private final static class Zoo extends MenuSelector {

        {
            Animal tiger = new Tiger("Гоша", (short) 10, new Home(), new Region[] { Region.AFRICA }, (short) 10);
            Animal parrot = new Parrot("Кеша", (short) 2, new Home(), new Region[] { Region.ASIA, Region.AUSTRALIA },
                    (short) 2);
            Animal shark = new Shark("Луиза", (short) 1, new Home(), new Region[] { Region.PACIFIC_OCEAN }, (short) 10);

            // Setting inittial animals' conditions
            tiger.setHealthy(false);
            shark.setLastFeedingDateTime(LocalDateTime.now().minusDays(1));
            parrot.getHome().setClean(false);

            // Adding animals
            var animals = new Animal[] { tiger, parrot, shark };

            // Adding staff
            var cleaner = new Cleaner();
            var zookeeper = new ZooKeeper();
            var doctor = new Doctor();

            var reportGenerator = new ReportGenerator();

            executors.put(Operation.SHOW_ANIMALS, () -> System.out.println("В зоопарке живут: "
                    + String.join(", ", Stream.of(animals).map(Animal::toString).toArray(String[]::new))));

            executors.put(Operation.SHOW_ANIMALS_NEEDS_CLEANUP, () -> {
                reportGenerator.setInfoProvider(new NeedsCleanupAnimalsInfoProvider(animals));
                reportGenerator.generateReport();
            });

            executors.put(Operation.SHOW_HUNGRY_ANIMALS, () -> {
                reportGenerator.setInfoProvider(new HungryAnimalsInfoProvider(animals));
                reportGenerator.generateReport();
            });

            executors.put(Operation.SHOW_SICK_ANIMALS, () -> {
                reportGenerator.setInfoProvider(new SickAnimalsInfoProvider(animals));
                reportGenerator.generateReport();
            });

            executors.put(Operation.FEED_ANIMAL, () -> {
                var selectedAnimal = IO.inputWithAutocomplete(
                        "Выберите животное, которое нужно покормить:",
                        animals,
                        Animal::getId,
                        (animal) -> String.format("%s, %s", animal.toString(),
                                animal.isHungry() ? "Голодное" : "Сытое"));

                zookeeper.feed(selectedAnimal);

                IO.displaySuccess(String.format("Животное %s накормлено", selectedAnimal));
            });

            executors.put(Operation.HEAL_ANIMAL, () -> {
                var selectedAnimal = IO.inputWithAutocomplete(
                        "Выберите животное, которое нужно вылечить:",
                        animals,
                        Animal::getId,
                        (animal) -> String.format("%s, %s", animal.toString(),
                                animal.isHealthy() ? "Здоровое" : "Больное"));

                doctor.heal(selectedAnimal);

                IO.displaySuccess(String.format("Животное %s вылечено", selectedAnimal));
            });

            executors.put(Operation.CLEAN_ANIMALS_AREA, () -> {
                var selectedAnimal = IO.inputWithAutocomplete(
                        "Выберите животное, у которого нужно провести уборку:",
                        animals,
                        Animal::getId,
                        (animal) -> String.format("%s, %s", animal.toString(),
                                animal.getHome().isClean() ? "Убрано" : "Нужна уборка"));

                cleaner.clean(selectedAnimal);

                IO.displaySuccess(String.format("У животного %s была проведена уборка", selectedAnimal));
            });
        }

        @Override
        protected boolean loop() {
            return true;
        }

    }

}
