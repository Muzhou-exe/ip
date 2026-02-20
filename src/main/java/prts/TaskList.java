package prts;

import java.util.ArrayList;
import java.util.List;

import prts.task.Task;

/**
 * Represents a list of tasks managed by the chatbot.
 * <p>
 * Task numbers used by this class are 1-based, meaning task number 1 refers
 * to the first task shown to the user.
 * </p>
 */
public class TaskList {

    private final int MAX_SIZE; // Coding standard: CONSTANT_CASE
    private final ArrayList<Task> tasks = new ArrayList<>(); // Initialize at declaration

    public TaskList(int maxSize) {
        this.MAX_SIZE = maxSize;
    }

    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the given 1-based task number.
     *
     * @param oneBasedIndex Task number shown to the user (1-based).
     * @return The task at the specified task number.
     * @throws IllegalArgumentException If the task number is out of range.
     */
    public Task get(int oneBasedIndex) throws IllegalArgumentException {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= tasks.size()) {
            throw new IllegalArgumentException("OOPS!!! That task number is out of range.");
        }
        return tasks.get(i);
    }

    /**
     * Adds a task to the list.
     *
     * @param t The task to add.
     * @throws IllegalStateException If the task list is full.
     */
    public void addTask(Task t) {
        if (tasks.size() >= MAX_SIZE) {
            // Defensive coding: Throw exception instead of silent failure/break
            throw new IllegalStateException("OOPS!!! Your task list is full (max " + MAX_SIZE + " tasks).");
        }
        tasks.add(t);
    }

    /**
     * Deletes and returns the task at the given 1-based task number.
     *
     * @param oneBasedIndex The 1-based index of the task.
     * @return The removed task.
     * @throws IllegalArgumentException If the task number is out of range.
     */
    public Task delete(int oneBasedIndex) {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= tasks.size()) {
            throw new IllegalArgumentException("OOPS!!! That task number is out of range.");
        }
        return tasks.remove(i);
    }

    public Task mark(int oneBasedIndex) {
        Task t = get(oneBasedIndex); // utilize get() which handles bounds check
        t.markDone();
        return t;
    }

    public Task unmark(int oneBasedIndex) {
        Task t = get(oneBasedIndex);
        t.unmarkDone();
        return t;
    }

    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        for (Task t : tasks) {
            if (t.toString().contains(keyword)) {
                matches.add(t);
            }
        }
        return matches;
    }

    public List<Task> toList() {
        return new ArrayList<>(tasks);
    }
}