package prts.task;

/**
 * Represents an event task with a start and end time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Constructs an Event task.
     *
     * @param description The event description.
     * @param from The start time.
     * @param to The end time.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toStorageString() {
        return "E | " + (isDone() ? 1 : 0) + " | " + description + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}