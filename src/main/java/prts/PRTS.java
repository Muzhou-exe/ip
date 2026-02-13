package prts;

import java.util.Scanner;

import prts.task.Deadline;
import prts.task.Event;
import prts.task.Task;
import prts.task.Todo;

public class PRTS {

    private static final String LOGO = """
                      _____                    _____                _____                    _____
                     /\\    \\                  /\\    \\              /\\    \\                  /\\    \\
                    /::\\    \\                /::\\    \\            /::\\    \\                /::\\    \\
                   /::::\\    \\              /::::\\    \\           \\:::\\    \\              /::::\\    \\
                  /::::::\\    \\            /::::::\\    \\           \\:::\\    \\            /::::::\\    \\
                 /:::/\\:::\\    \\          /:::/\\:::\\    \\           \\:::\\    \\          /:::/\\:::\\    \\
                /:::/__\\:::\\    \\        /:::/__\\:::\\    \\           \\:::\\    \\        /:::/__\\:::\\    \\
               /::::\\   \\:::\\    \\      /::::\\   \\:::\\    \\          /::::\\    \\       \\:::\\   \\:::\\    \\
              /::::::\\   \\:::\\    \\    /::::::\\   \\:::\\    \\        /::::::\\    \\    ___\\:::\\   \\:::\\    \\
             /:::/\\:::\\   \\:::\\____\\  /:::/\\:::\\   \\:::\\____\\      /:::/\\:::\\    \\  /\\   \\:::\\   \\:::\\    \\
            /:::/  \\:::\\   \\:::|    |/:::/  \\:::\\   \\:::|    |    /:::/  \\:::\\____\\/::\\   \\:::\\   \\:::\\____\\
            \\::/    \\:::\\  /:::|____|\\::/   |::::\\  /:::|____|   /:::/    \\::/    /\\:::\\   \\:::\\   \\::/    /
             \\/_____/\\:::\\/:::/    /  \\/____|:::::\\/:::/    /   /:::/    / \\/____/  \\:::\\   \\:::\\   \\/____/
                      \\::::::/    /         |:::::::::/    /   /:::/    /            \\:::\\   \\:::\\    \\
                       \\::::/    /          |::|\\::::/    /   /:::/    /              \\:::\\   \\:::\\____\\
                        \\::/____/           |::| \\::/____/    \\::/    /                \\:::\\  /:::/    /
                         ~~                 |::|  ~|           \\/____/                  \\:::\\/:::/    /
                                            |::|   |                                     \\::::::/    /
                                            \\::|   |                                      \\::::/    /
                                             \\:|   |                                       \\::/    /
                                              \\|___|                                        \\/____/
""";
    private final Storage storage;
    private final TaskList taskList;
    private final Ui ui;

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
            ui.showError("Error loading file, starting with empty list.");
            temp = new TaskList(100);
        }
        taskList = temp;
    }

    public static void main(String[] args) {
        new PRTS("data/tasks.txt").run();
    }

    public void run() {
        ui.showLogo(LOGO);
        ui.showWelcome();

        Scanner sc = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning && sc.hasNextLine()) {
            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;

            ParsedCommand cmd = Parser.parse(input);

            try {
                switch (cmd.type) {
                    case BYE:
                        isRunning = false;
                        ui.showBye();
                        break;
                    case ERROR:
                        ui.showError(cmd.errorMessage);
                        break;
                    case LIST:
                        ui.showList(taskList);
                        break;
                    case TODO: {
                        Task t = new Todo(cmd.description);
                        taskList.addTask(t);
                        ui.showAdded(t, taskList.size());
                        storage.save(taskList);
                        break;
                    }
                    case DEADLINE: {
                        Task t = new Deadline(cmd.description, cmd.byDate);
                        taskList.addTask(t);
                        ui.showAdded(t, taskList.size());
                        storage.save(taskList);
                        break;
                    }
                    case EVENT: {
                        Task t = new Event(cmd.description, cmd.from, cmd.to);
                        taskList.addTask(t);
                        ui.showAdded(t, taskList.size());
                        storage.save(taskList);
                        break;
                    }
                    case DELETE: {
                        Task removed = taskList.delete(cmd.index);
                        ui.showDeleted(removed, taskList.size());
                        storage.save(taskList);
                        break;
                    }
                    case MARK: {
                        Task t = taskList.mark(cmd.index);
                        ui.showMarked(t);
                        storage.save(taskList);
                        break;
                    }
                    case UNMARK: {
                        Task t = taskList.unmark(cmd.index);
                        ui.showUnmarked(t);
                        storage.save(taskList);
                        break;
                    }
                    case FIND: {
                        ui.showFindResult(taskList.find(cmd.description));
                        break;
                    }
                    case CHEER: {
                        java.util.List<String> cheers = storage.loadCheers();
                        java.util.Random r = new java.util.Random();
                        if (!cheers.isEmpty()) {
                            ui.showCheer(cheers.get(r.nextInt(cheers.size())));
                        } else {
                            ui.showCheer("Keep going!");
                        }
                        break;
                    }

                    default:
                        // should not reach
                        ui.showError("OOPS!!! I'm sorry, but I don't know what that means :-(");
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                // Catch both Argument error (index out of bounds) and State error (list full)
                ui.showError(e.getMessage());
            }
        }
    }
}