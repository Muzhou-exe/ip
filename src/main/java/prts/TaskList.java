package prts;

import java.util.ArrayList;
import java.util.List;

import prts.task.Task;

/**
 * Represents a list of tasks managed by the chatbot.
 * Task numbers used by this class are 1-based.
 */
public class TaskList {

    private final int maxSize;
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Constructs a TaskList with a maximum size.
     *
     * @param maxSize Maximum number of tasks allowed.
     */
    public TaskList(int maxSize) {
        assert maxSize > 0 : "maxSize should be positive";
        this.maxSize = maxSize;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the given 1-based task number.
     *
     * @param oneBasedIndex Task number shown to the user (1-based).
     * @return The task at the specified task number.
     */
    public Task get(int oneBasedIndex) {
        assert oneBasedIndex >= 1 : "Task index is 1-based and should be >= 1";

        int zeroBasedIndex = oneBasedIndex - 1;
        assert zeroBasedIndex >= 0 : "Converted index should not be negative";

        if (zeroBasedIndex < 0 || zeroBasedIndex >= tasks.size()) {
            throw new IllegalArgumentException("OOPS!!! That task number is out of range.");
        }
        return tasks.get(zeroBasedIndex);
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        assert task != null : "Task to add should not be null";

        if (tasks.size() >= maxSize) {
            throw new IllegalStateException("OOPS!!! Your task list is full (max " + maxSize + " tasks).");
        }
        tasks.add(task);
    }

    /**
     * Deletes and returns the task at the given 1-based task number.
     *
     * @param oneBasedIndex Task number shown to the user (1-based).
     * @return The removed task.
     */
    public Task delete(int oneBasedIndex) {
        Task task = get(oneBasedIndex);
        tasks.remove(oneBasedIndex - 1);
        return task;
    }

    /**
     * Marks a task as done.
     *
     * @param oneBasedIndex Task number shown to the user (1-based).
     * @return The updated task.
     */
    public Task mark(int oneBasedIndex) {
        Task task = get(oneBasedIndex);
        task.markDone();
        return task;
    }

    /**
     * Marks a task as not done.
     *
     * @param oneBasedIndex Task number shown to the user (1-based).
     * @return The updated task.
     */
    public Task unmark(int oneBasedIndex) {
        Task task = get(oneBasedIndex);
        task.unmarkDone();
        return task;
    }

    /**
     * Finds tasks containing the keyword.
     *
     * @param keyword Keyword to search.
     * @return Matching tasks.
     */
    public List<Task> find(String keyword) {
        assert keyword != null : "Keyword should not be null";

        String trimmedKeyword = keyword.trim();
        if (trimmedKeyword.isEmpty()) {
            return List.of();
        }

        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            assert task != null : "Task list should not contain null tasks";
            if (task.toString().contains(trimmedKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns a snapshot list of tasks.
     *
     * @return Copy of tasks.
     */
    public List<Task> toList() {
        return new ArrayList<>(tasks);
    }
}