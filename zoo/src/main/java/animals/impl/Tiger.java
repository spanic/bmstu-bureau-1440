package animals.impl;

import animals.Animal;
import animals.MakesSounds;
import animals.Region;
import facilities.Home;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = true)
public class Tiger extends Animal implements MakesSounds {


    public Tiger(@NonNull String name,
                 @NonNull short age,
                 @NonNull Home home,
                 @NonNull Region[] origin,
                 @NonNull short feedingIntervalHrs) {
        super(name, age, home, origin, feedingIntervalHrs);
    }

    @Override
    public void move() {
        System.out.println("Я – тигр, я бегу!");
    }

    @Override
    public void makeSound() {
        System.out.println("Я – тигр, я рычу!");
    }

}
