/**
 * Represents a list of tasks managed by the chatbot.
 * <p>
 * Task numbers used by this class are 1-based, meaning task number 1 refers
 * to the first task shown to the user.
 * </p>
 */

package prts;

import java.util.ArrayList;
import java.util.List;

import prts.task.Task;
import prts.task.Todo;
import prts.task.Deadline;
import prts.task.Event;

public class TaskList {

    private final int maxSize;
    private final ArrayList<Task> tasks = new ArrayList<>();

    public TaskList(int maxSize) {
        this.maxSize = maxSize;
    }

    public int size() {
        return tasks.size();
    }

    // 1-based access for printing
    /**
     * Returns the task at the given 1-based task number.
     *
     * @param taskNumber Task number shown to the user (1-based).
     * @return The task at the specified task number.
     * @throws IllegalArgumentException If the task number is out of range.
     */

    public Task get(int oneBasedIndex) {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= tasks.size()) {
            throw new IllegalArgumentException("OOPS!!! That task number is out of range.");
        }
        return tasks.get(i);
    }
    /**
     * Adds a task to the task list.
     *
     * @param task The task to be added.
     */

    public void add(Task t) {
        if (tasks.size() >= maxSize) {
            throw new IllegalArgumentException("OOPS!!! Your task list is full (max " + maxSize + " tasks).");
        }
        tasks.add(t);
    }
    /**
     * Deletes and returns the task at the given 1-based task number.
     *
     * @param taskNumber Task number shown to the user (1-based).
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
        Task t = get(oneBasedIndex);
        t.markDone();
        return t;
    }

    public Task unmark(int oneBasedIndex) {
        Task t = get(oneBasedIndex);
        t.unmarkDone();
        return t;
    }

    public List<Task> toList() {
        return new ArrayList<>(tasks);
    }

    public void addAll(List<Task> loaded) {
        for (Task t : loaded) {
            if (tasks.size() >= maxSize) {
                break;
            }
            tasks.add(t);
        }
    }
}
