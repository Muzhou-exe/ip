package task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import prts.task.Deadline;

public class DeadlineTest {

    @Test
    public void toString_formatsDateAsMmmDdYyyy() {
        Deadline d = new Deadline("return book", LocalDate.of(2019, 12, 2));
        assertEquals("[D][ ] return book (by: Dec 02 2019)", d.toString());
    }

    @Test
    public void toStorageString_usesIsoDateAndDoneFlag() {
        Deadline d = new Deadline("return book", LocalDate.of(2019, 12, 2));

        assertEquals("D | 0 | return book | 2019-12-02", d.toStorageString());

        d.markDone();
        assertEquals("D | 1 | return book | 2019-12-02", d.toStorageString());
    }

    @Test
    public void toStorageString_fallbackRawDate_isPreserved() {
        Deadline d = new Deadline("return book", "2/12/2019 1800");
        assertEquals("D | 0 | return book | 2/12/2019 1800", d.toStorageString());
    }
}