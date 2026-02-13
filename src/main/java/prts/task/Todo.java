package prts.task;

/**
 * Represents a Todo task without any specific date or time.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task.
     *
     * @param description The description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toStorageString() {
        // "T" specifically marks a Todo task, resolving ambiguity
        return "T | " + (isDone() ? 1 : 0) + " | " + description;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}