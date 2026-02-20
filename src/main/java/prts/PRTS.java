package prts;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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
            for (Task t : storage.load()) {
                temp.addTask(t);
            }
        } catch (Exception e) {
            temp = new TaskList(100);
        }
        taskList = temp;
    }

    /**
     * Processes user input and returns the chatbot response.
     */
    public String getResponse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        ParsedCommand cmd = Parser.parse(input);

        try {
            switch (cmd.type) {
                case BYE:
                    return ui.getBye();

                case LIST:
                    return ui.getListString(taskList);

                case FIND:
                    return ui.getFindResultString(taskList.find(cmd.description));

                case CHEER:
                    List<String> cheers = storage.loadCheers();
                    String msg = cheers.isEmpty() ? "Keep going!"
                            : cheers.get(new java.util.Random().nextInt(cheers.size()));
                    return ui.getCheerString(msg);

                case UNDO:
                    return undoLastChange();

                case TODO:
                    saveSnapshot();
                    Task t = new Todo(cmd.description);
                    taskList.addTask(t);
                    storage.save(taskList);
                    return ui.getAddedString(t, taskList.size());

                case DEADLINE:
                    saveSnapshot();
                    Task d = new Deadline(cmd.description, cmd.byDate);
                    taskList.addTask(d);
                    storage.save(taskList);
                    return ui.getAddedString(d, taskList.size());

                case EVENT:
                    saveSnapshot();
                    Task e = new Event(cmd.description, cmd.from, cmd.to);
                    taskList.addTask(e);
                    storage.save(taskList);
                    return ui.getAddedString(e, taskList.size());

                case DELETE:
                    saveSnapshot();
                    Task removed = taskList.delete(cmd.index);
                    storage.save(taskList);
                    return ui.getDeletedString(removed, taskList.size());

                case MARK:
                    saveSnapshot();
                    Task m = taskList.mark(cmd.index);
                    storage.save(taskList);
                    return ui.getMarkedString(m);

                case UNMARK:
                    saveSnapshot();
                    Task u = taskList.unmark(cmd.index);
                    storage.save(taskList);
                    return ui.getUnmarkedString(u);

                case ERROR:
                    return ui.getErrorString(cmd.errorMessage);

                default:
                    return ui.getErrorString("Unknown command.");
            }
        } catch (Exception ex) {
            return ui.getErrorString(ex.getMessage());
        }
    }

    private void saveSnapshot() {
        List<String> lines = new ArrayList<>();
        for (Task task : taskList.toList()) {
            lines.add(task.toStorageString());
        }
        undoStack.push(lines);
    }

    private String undoLastChange() throws Exception {
        if (undoStack.isEmpty()) {
            return ui.getNothingToUndoString();
        }

        List<String> previousLines = undoStack.pop();
        List<Task> previousTasks = storage.parseTaskLines(previousLines);

        taskList.resetTo(previousTasks);
        storage.save(taskList);

        return ui.getUndoSuccessString();
    }
}