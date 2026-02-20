package prts.task;

/**
 * Represents a todo task.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task.
     *
     * @param description The todo description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toStorageString() {
        return "T | " + (isDone() ? 1 : 0) + " | " + description;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}