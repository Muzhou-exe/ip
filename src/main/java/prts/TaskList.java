package prts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import prts.task.Task;

/**
 * Represents a list of tasks managed by the chatbot.
 * <p>
 * Task numbers used by this class are 1-based, meaning task number 1 refers
 * to the first task shown to the user.
 * </p>
 */
public class TaskList {

    private final int MAX_SIZE;
    private final ArrayList<Task> tasks = new ArrayList<>();

    public TaskList(int maxSize) {
        assert maxSize > 0 : "Max size must be positive";
        this.MAX_SIZE = maxSize;
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int oneBasedIndex) {
        assert oneBasedIndex > 0 : "Index should be positive";

        int i = oneBasedIndex - 1;

        if (i < 0 || i >= tasks.size()) {
            throw new IllegalArgumentException("OOPS!!! That task number is out of range.");
        }
        return tasks.get(i);
    }

    public void addTask(Task t) {
        assert t != null : "Task should not be null";

        if (tasks.size() >= MAX_SIZE) {
            throw new IllegalStateException(
                    "OOPS!!! Your task list is full (max " + MAX_SIZE + " tasks).");
        }
        tasks.add(t);
    }

    public Task delete(int oneBasedIndex) {
        assert oneBasedIndex > 0 : "Index should be positive";

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

    public List<Task> find(String keyword) {
        assert keyword != null : "Keyword should not be null";

        return tasks.stream()
                .filter(t -> t.toString().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<Task> toList() {
        return new ArrayList<>(tasks);
    }

    /**
     * Replaces the current task list contents with the given tasks.
     *
     * @param newTasks Tasks to replace with.
     */
    public void resetTo(List<Task> newTasks) {
        assert newTasks != null : "New task list should not be null";

        tasks.clear();
        for (Task t : newTasks) {
            addTask(t);
        }
    }
}