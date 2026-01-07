package animals.impl;

import animals.Animal;
import animals.MakesSounds;
import animals.Region;
import facilities.Home;
import lombok.NonNull;

public class Parrot extends Animal implements MakesSounds {

    public Parrot(@NonNull String name,
                  @NonNull short age,
                  @NonNull Home home,
                  @NonNull Region[] origin,
                  @NonNull short feedingIntervalHrs) {
        super(name, age, home, origin, feedingIntervalHrs);
    }

    @Override
    public void move() {
        System.out.println("Я – попугай, я летаю!");
    }

    @Override
    public void makeSound() {
        System.out.println("Я – попугай, я подражаю звукам других животных!");
    }

}
