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

    /**
     * Marks the task as done.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void unmarkDone() {
        isDone = false;
    }

    /**
     * Returns true if the task is done, false otherwise.
     *
     * @return The completion status of the task.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the status icon (X for done, space for not done).
     * Private helper method for toString.
     */
    private String getStatusIcon() {
        return "[" + (isDone ? "X" : " ") + "]";
    }

    /**
     * Returns the string representation of the task for storage.
     *
     * @return The formatted string for file storage.
     */
    public abstract String toStorageString();

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}