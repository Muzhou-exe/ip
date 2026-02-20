package prts;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import prts.task.Deadline;
import prts.task.Event;
import prts.task.Task;
import prts.task.Todo;

/**
 * Main logic class for PRTS. Provides responses for GUI interaction.
 */
public class PRTS {

    private final Storage storage;
    private final TaskList taskList;
    private final Ui ui;

    private final Deque<List<String>> undoStack = new ArrayDeque<>();

    public PRTS(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList temp;
        try {
            temp = new TaskList(100);
            for (Task task : storage.load()) {
                temp.addTask(task);
            }
        } catch (Exception e) {
            temp = new TaskList(100);
        }
        taskList = temp;
    }

    public String getResponse(String input) {
        assert input != null : "GUI should not pass null input";

        if (input.trim().isEmpty()) {
            return "";
        }

        ParsedCommand command = Parser.parse(input);

        try {
            switch (command.type) {
                case BYE:
                    return ui.getBye();
                case LIST:
                    return ui.getListString(taskList);
                case FIND:
                    return ui.getFindResultString(taskList.find(command.description));
                case CHEER:
                    return handleCheer();
                case UNDO:
                    return handleUndo();
                case TODO:
                    snapshot();
                    return handleAdd(new Todo(command.description));
                case DEADLINE:
                    snapshot();
                    return handleAdd(new Deadline(command.description, command.byDate));
                case EVENT:
                    snapshot();
                    return handleAdd(new Event(command.description, command.from, command.to));
                case DELETE:
                    snapshot();
                    return handleDelete(command.index);
                case MARK:
                    snapshot();
                    return handleMark(command.index);
                case UNMARK:
                    snapshot();
                    return handleUnmark(command.index);
                case ERROR:
                    return ui.getErrorString(command.errorMessage);
                default:
                    return ui.getErrorString("Unknown command.");
            }
        } catch (Exception e) {
            return ui.getErrorString(e.getMessage());
        }
    }

    private String handleCheer() {
        List<String> cheers = storage.loadCheers();
        String message = cheers.isEmpty()
                ? "Keep going!"
                : cheers.get(new Random().nextInt(cheers.size()));
        return ui.getCheerString(message);
    }

    private String handleAdd(Task task) {
        taskList.addTask(task);
        storage.save(taskList);
        return ui.getAddedString(task, taskList.size());
    }

    private String handleDelete(int index) {
        Task removed = taskList.delete(index);
        storage.save(taskList);
        return ui.getDeletedString(removed, taskList.size());
    }

    private String handleMark(int index) {
        Task task = taskList.mark(index);
        storage.save(taskList);
        return ui.getMarkedString(task);
    }

    private String handleUnmark(int index) {
        Task task = taskList.unmark(index);
        storage.save(taskList);
        return ui.getUnmarkedString(task);
    }

    private void snapshot() {
        List<String> lines = new ArrayList<>();
        for (Task task : taskList.toList()) {
            lines.add(task.toStorageString());
        }
        undoStack.push(lines);
    }

    private String handleUndo() {
        if (undoStack.isEmpty()) {
            return ui.getNothingToUndoString();
        }

        List<String> previousLines = undoStack.pop();
        List<Task> previousTasks = storage.parseTaskLines(previousLines);
        taskList.replaceAll(previousTasks);
        storage.save(taskList);

        return ui.getUndoString();
    }
}