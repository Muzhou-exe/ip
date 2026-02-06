package prts.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Deadline extends Task {

    private static final DateTimeFormatter OUT_DATE_FMT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate byDate; // preferred
    private final String byRaw;     // fallback

    public Deadline(String description, LocalDate byDate) {
        super(description);
        this.byDate = byDate;
        this.byRaw = null;
    }

    public Deadline(String description, String byRaw) {
        super(description);
        this.byDate = null;
        this.byRaw = byRaw;
    }

    @Override
    public String toStorageString() {
        String byPart = (byDate != null) ? byDate.toString() : byRaw;
        return "D | " + (isDone() ? 1 : 0) + " | " + description + " | " + byPart;
    }

    @Override
    public String toString() {
        if (byDate != null) {
            return "[D]" + super.toString() + " (by: " + byDate.format(OUT_DATE_FMT) + ")";
        }
        return "[D]" + super.toString() + " (by: " + byRaw + ")";
    }
}
