package task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import prts.task.Deadline;

/**
 * Tests the Deadline task model, specifically string formatting for display and storage.
 */
public class DeadlineTest {

    @Test
    public void toString_formatsDateAsMmmDdYyyy() {
        // Positive test: verify display format for LocalDate deadlines
        Deadline d = new Deadline("return book", LocalDate.of(2019, 12, 2));
        assertEquals("[D][ ] return book (by: Dec 02 2019)", d.toString());
    }

    @Test
    public void toStorageString_usesIsoDateAndDoneFlag() {
        // Positive test: verify storage format with 'done' status
        Deadline d = new Deadline("return book", LocalDate.of(2019, 12, 2));

        assertEquals("D | 0 | return book | 2019-12-02", d.toStorageString());

        d.markDone();
        assertEquals("D | 1 | return book | 2019-12-02", d.toStorageString());
    }

    @Test
    public void toStorageString_fallbackRawDate_isPreserved() {
        // Positive test: verify backward compatibility with raw string dates
        Deadline d = new Deadline("return book", "2/12/2019 1800");
        assertEquals("D | 0 | return book | 2/12/2019 1800", d.toStorageString());
    }
}