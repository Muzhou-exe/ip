package prts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import prts.task.Task;

/**
 * Represents a list of tasks managed by the chatbot.
 * Task numbers used by this class are 1-based.
 */
public class TaskList {

    private final int maxSize;
    private final ArrayList<Task> tasks = new ArrayList<>();

    public TaskList(int maxSize) {
        assert maxSize > 0 : "maxSize should be positive";
        this.maxSize = maxSize;
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int oneBasedIndex) {
        assert oneBasedIndex >= 1 : "Task index is 1-based and should be >= 1";

        int zeroBasedIndex = oneBasedIndex - 1;
        if (zeroBasedIndex < 0 || zeroBasedIndex >= tasks.size()) {
            throw new IllegalArgumentException("OOPS!!! That task number is out of range.");
        }
        return tasks.get(zeroBasedIndex);
    }

    public void addTask(Task task) {
        assert task != null : "Task to add should not be null";

        if (tasks.size() >= maxSize) {
            throw new IllegalStateException("OOPS!!! Your task list is full (max " + maxSize + " tasks).");
        }
        tasks.add(task);
    }

    public Task delete(int oneBasedIndex) {
        Task task = get(oneBasedIndex);
        tasks.remove(oneBasedIndex - 1);
        return task;
    }

    public Task mark(int oneBasedIndex) {
        Task task = get(oneBasedIndex);
        task.markDone();
        return task;
    }

    public Task unmark(int oneBasedIndex) {
        Task task = get(oneBasedIndex);
        task.unmarkDone();
        return task;
    }

    public List<Task> find(String keyword) {
        assert keyword != null : "Keyword should not be null";

        String trimmedKeyword = keyword.trim();
        if (trimmedKeyword.isEmpty()) {
            return List.of();
        }

        return tasks.stream()
                .filter(Objects::nonNull)
                .filter(task -> task.toString().contains(trimmedKeyword))
                .collect(Collectors.toList());
    }

    public List<Task> toList() {
        return new ArrayList<>(tasks);
    }

    /**
     * Replaces all tasks with the provided tasks.
     *
     * @param newTasks Tasks to replace with.
     */
    public void replaceAll(List<Task> newTasks) {
        assert newTasks != null : "newTasks should not be null";

        tasks.clear();
        for (Task task : newTasks) {
            addTask(task);
        }
    }
}