package com.havlin.daniel.russian;

import com.havlin.daniel.russian.utils.StressedWordConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StressMarkTests {
    @Test
    public void addStressMarks() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("Э́то", StressedWordConverter.addStressMarks("Э'то")),
                () -> Assertions.assertEquals("охраня́ть", StressedWordConverter.addStressMarks("охраня'ть")),
                () -> Assertions.assertEquals("людьми́", StressedWordConverter.addStressMarks("людьми'")),
                () -> Assertions.assertEquals("мо́жет", StressedWordConverter.addStressMarks("мо'жет"))
        );
    }

    @Test
    public void removeStressMarks() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("Э'то", StressedWordConverter.removeStressMarks("Э́то")),
                () -> Assertions.assertEquals("охраня'ть", StressedWordConverter.removeStressMarks("охраня́ть")),
                () -> Assertions.assertEquals("людьми'", StressedWordConverter.removeStressMarks("людьми́")),
                () -> Assertions.assertEquals("мо'жет", StressedWordConverter.removeStressMarks("мо́жет"))
        );
    }
}
