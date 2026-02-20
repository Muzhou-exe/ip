package prts;

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

    /**
     * Processes user input and returns the chatbot response.
     *
     * @param input User input.
     * @return Chatbot response.
     */
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
                case TODO: {
                    Task task = new Todo(command.description);
                    taskList.addTask(task);
                    storage.save(taskList);
                    return ui.getAddedString(task, taskList.size());
                }
                case DEADLINE: {
                    Task task = new Deadline(command.description, command.byDate);
                    taskList.addTask(task);
                    storage.save(taskList);
                    return ui.getAddedString(task, taskList.size());
                }
                case EVENT: {
                    Task task = new Event(command.description, command.from, command.to);
                    taskList.addTask(task);
                    storage.save(taskList);
                    return ui.getAddedString(task, taskList.size());
                }
                case DELETE: {
                    Task removed = taskList.delete(command.index);
                    storage.save(taskList);
                    return ui.getDeletedString(removed, taskList.size());
                }
                case MARK: {
                    Task task = taskList.mark(command.index);
                    storage.save(taskList);
                    return ui.getMarkedString(task);
                }
                case UNMARK: {
                    Task task = taskList.unmark(command.index);
                    storage.save(taskList);
                    return ui.getUnmarkedString(task);
                }
                case FIND:
                    return ui.getFindResultString(taskList.find(command.description));
                case CHEER: {
                    List<String> cheers = storage.loadCheers();
                    String message = cheers.isEmpty()
                            ? "Keep going!"
                            : cheers.get(new Random().nextInt(cheers.size()));
                    return ui.getCheerString(message);
                }
                case ERROR:
                    return ui.getErrorString(command.errorMessage);
                default:
                    return ui.getErrorString("Unknown command.");
            }
        } catch (Exception e) {
            return ui.getErrorString(e.getMessage());
        }
    }
}