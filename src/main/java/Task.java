public class Task {
    protected final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
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

    protected String statusIcon() {
        return "[" + (isDone ? "X" : " ") + "]";
    }

    public String toStorageString() {
        return "T | " + (isDone ? 1 : 0) + " | " + description;
    }

    @Override
    public String toString() {
        return statusIcon() + " " + description;
    }
}
