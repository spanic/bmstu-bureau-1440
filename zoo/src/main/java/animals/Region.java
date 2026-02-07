package animals;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Region {

    AFRICA("Africa"),
    ASIA("Asia"),
    AUSTRALIA("Australia"),
    EUROPE("Europe"),
    NORTH_AMERICA("North America"),
    SOUTH_AMERICA("South America"),
    OCEANIA("Oceania"),
    PACIFIC_OCEAN("Pacific Ocean"),
    ANTARCTICA("Antarctica");

    @NonNull
    @Getter
    private final String name;

}
