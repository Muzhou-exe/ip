import java.util.ArrayList;
import java.util.List;

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
    public Task get(int oneBasedIndex) {
        int i = oneBasedIndex - 1;
        if (i < 0 || i >= tasks.size()) {
            throw new IllegalArgumentException("OOPS!!! That task number is out of range.");
        }
        return tasks.get(i);
    }

    public void add(Task t) {
        if (tasks.size() >= maxSize) {
            throw new IllegalArgumentException("OOPS!!! Your task list is full (max " + maxSize + " tasks).");
        }
        tasks.add(t);
    }

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
