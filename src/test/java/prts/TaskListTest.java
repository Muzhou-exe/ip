package prts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import prts.task.Task;
import prts.task.Todo;

public class TaskListTest {

    @Test
    public void addTask_taskAdded_correctSizeAndContent() {
        TaskList list = new TaskList(100);
        Task t = new Todo("read book");

        list.addTask(t);

        assertEquals(1, list.size());
        assertEquals(t, list.get(1));
    }

    @Test
    public void deleteTask_validIndex_taskRemovedAndShifted() {
        TaskList list = new TaskList(100);
        Task t1 = new Todo("read book");
        Task t2 = new Todo("return book");

        list.addTask(t1);
        list.addTask(t2);

        Task removed = list.delete(1);

        assertEquals(t1, removed);
        assertEquals(1, list.size());
        assertEquals(t2, list.get(1));
    }

    @Test
    public void deleteTask_outOfRange_throwsException() {
        TaskList list = new TaskList(100);
        assertThrows(IllegalArgumentException.class, () -> list.delete(1));
    }
}