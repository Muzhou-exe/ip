package prts.task;

/**
 * Represents a generic task in the task list.
 * This is an abstract class that cannot be instantiated directly.
 */
public abstract class Task {

    protected final String description;
    private boolean isDone;

    /**
     * Constructs a new Task with the given description.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        assert description != null : "Description should not be null";
        assert !description.trim().isEmpty() : "Description should not be empty";

        this.description = description;
        this.isDone = false;
    }

    public void markDone() {
        isDone = true;
    }

    public void unmarkDone() {
        isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    private String getStatusIcon() {
        return "[" + (isDone ? "X" : " ") + "]";
    }

    public abstract String toStorageString();

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}