import animals.Animal;
import animals.Region;
import animals.impl.Parrot;
import animals.impl.Shark;
import animals.impl.Tiger;
import com.bmstu_bureau_1440.io.IO;
import com.bmstu_bureau_1440.io.IOperation;
import facilities.Home;
import io.Operation;
import reports.ReportGenerator;
import reports.impl.HungryAnimalsInfoProvider;
import reports.impl.NeedsCleanupAnimalsInfoProvider;
import reports.impl.SickAnimalsInfoProvider;
import staff.impl.Cleaner;
import staff.impl.Doctor;
import staff.impl.ZooKeeper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.bmstu_bureau_1440.io.Operation.EXIT;

public class Main {

    public static void main(String[] args) {

        // Добавляем животных в зоопарк
        Animal tiger = new Tiger("Гоша", (short) 10, new Home(), new Region[]{Region.AFRICA}, (short) 10);
        Animal parrot = new Parrot("Кеша", (short) 2, new Home(), new Region[]{Region.ASIA, Region.AUSTRALIA}, (short) 2);
        Animal shark = new Shark("Луиза", (short) 1, new Home(), new Region[]{Region.PACIFIC_OCEAN}, (short) 10);

        var animals = new Animal[]{tiger, parrot, shark};

        // Устанавливаем начальное состояние животным
        tiger.setHealthy(false); // тигр болеет
        shark.setLastFeedingDateTime(LocalDateTime.now().minusDays(1)); // акула голодная
        parrot.getHome().setClean(false); // у попугая в вольере нужна уборка

        // Добавляем персонал
        var cleaner = new Cleaner();
        var zookeeper = new ZooKeeper();
        var doctor = new Doctor();

        // Настраиваем модуль отчета
        var reportGenerator = new ReportGenerator();

        while (true) {

            try {

                List<IOperation> operations = new ArrayList<>(List.of(Operation.values()));
                operations.add(EXIT);

                var option = IO.displayMenu(operations.toArray(new IOperation[0]));

                if (option.equals(EXIT.getOperation())) {

                    break;

                } else if (Operation.SHOW_ANIMALS.getOperation().equals(option)) {

                    System.out.println("В зоопарке живут: "
                            + String.join(", ", Stream.of(animals).map(Animal::toString).toArray(String[]::new)));

                } else if (Operation.SHOW_ANIMALS_NEEDS_CLEANUP.getOperation().equals(option)) {

                    reportGenerator.setInfoProvider(new NeedsCleanupAnimalsInfoProvider(animals));
                    reportGenerator.generateReport();

                } else if (Operation.SHOW_HUNGRY_ANIMALS.getOperation().equals(option)) {

                    reportGenerator.setInfoProvider(new HungryAnimalsInfoProvider(animals));
                    reportGenerator.generateReport();

                } else if (Operation.SHOW_SICK_ANIMALS.getOperation().equals(option)) {

                    reportGenerator.setInfoProvider(new SickAnimalsInfoProvider(animals));
                    reportGenerator.generateReport();

                } else if (Operation.FEED_ANIMAL.getOperation().equals(option)) {

                    var selectedAnimalId = IO.inputWithAutocomplete(
                            "Выберите животное, которое нужно покормить:",
                            animals,
                            Animal::getId,
                            (animal) -> String.format("%s, %s", animal.toString(), animal.isHungry() ? "Голодное" : "Сытое")
                    );
                    var selectedAnimal = Stream.of(animals).filter(animal -> animal.getId().equals(selectedAnimalId)).findFirst();

                    selectedAnimal.ifPresent(animal -> {
                        zookeeper.feed(animal);
                        IO.displaySuccess(String.format("Животное %s накормлено", animal));
                    });

                } else if (Operation.HEAL_ANIMAL.getOperation().equals(option)) {

                    var selectedAnimalId = IO.inputWithAutocomplete(
                            "Выберите животное, которое нужно вылечить:",
                            animals,
                            Animal::getId,
                            (animal) -> String.format("%s, %s", animal.toString(), animal.isHealthy() ? "Здоровое" : "Больное")
                    );
                    var selectedAnimal = Stream.of(animals).filter(animal -> animal.getId().equals(selectedAnimalId)).findFirst();

                    selectedAnimal.ifPresent(animal -> {
                        doctor.heal(animal);
                        IO.displaySuccess(String.format("Животное %s вылечено", animal));
                    });

                } else if (Operation.CLEAN_ANIMALS_AREA.getOperation().equals(option)) {

                    var selectedAnimalId = IO.inputWithAutocomplete(
                            "Выберите животное, у которого нужно провести уборку:",
                            animals,
                            Animal::getId,
                            (animal) -> String.format("%s, %s", animal.toString(), animal.getHome().isClean() ? "Убрано" : "Нужна уборка")
                    );
                    var selectedAnimal = Stream.of(animals).filter(animal -> animal.getId().equals(selectedAnimalId)).findFirst();

                    selectedAnimal.ifPresent(animal -> {
                        cleaner.clean(animal);
                        IO.displaySuccess(String.format("У животного %s была проведена уборка", animal));
                    });

                }
            } catch (Exception e) {
                IO.displayError(e);
            }

        }

    }

}
