package animals;

import facilities.Home;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class Animal {

    @Getter
    @NonNull
    private final String id = UUID.randomUUID().toString();
    @Getter
    @NonNull
    private final String name;
    @NonNull
    short age;
    @NonNull
    @Getter
    Home home;
    @NonNull
    Region[] origin;
    @NonNull
    @Getter
    Short feedingIntervalHrs;
    @Getter
    @Setter
    LocalDateTime lastFeedingDateTime = LocalDateTime.now();
    @Getter
    @Setter
    boolean isHealthy = true;

    public abstract void move();

    public boolean isHungry() {
        return LocalDateTime.now().isAfter(this.getLastFeedingDateTime().plusHours(this.getFeedingIntervalHrs()));
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.getName(), this.getClass().getSimpleName());
    }
}
