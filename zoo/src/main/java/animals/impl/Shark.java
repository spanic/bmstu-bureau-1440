package animals.impl;

import animals.Animal;
import animals.Region;
import facilities.Home;
import lombok.NonNull;

public class Shark extends Animal {

    public Shark(@NonNull String name,
                 @NonNull short age,
                 @NonNull Home home,
                 @NonNull Region[] origin,
                 @NonNull short feedingIntervalHrs) {
        super(name, age, home, origin, feedingIntervalHrs);
    }

    @Override
    public void move() {
        System.out.println("Я – акула, я плаваю!");
    }

}
