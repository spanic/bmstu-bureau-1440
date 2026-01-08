package io;

import com.bmstu_bureau_1440.io.IOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStyle;

@RequiredArgsConstructor
@AllArgsConstructor
public enum Operation implements IOperation {

    SHOW_ANIMALS("show", "Показать всех животных"),
    SHOW_SICK_ANIMALS("show_sick", "Показать больных животных"),
    SHOW_HUNGRY_ANIMALS("show_hungry", "Показать голодных животных"),
    SHOW_ANIMALS_NEEDS_CLEANUP("show_needs_cleanup", "Показать животных, у кого нужна уборка"),
    HEAL_ANIMAL("heal", "Лечить животное"),
    FEED_ANIMAL("feed", "Покормить животное"),
    CLEAN_ANIMALS_AREA("clean", "Убрать за животным");

    @NonNull
    @Getter
    private final String operation;

    @NonNull
    @Getter
    private final String text;

    @Getter
    private AttributedStyle style;

}
