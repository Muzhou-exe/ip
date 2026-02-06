import java.util.Scanner;

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

    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("data/prts.txt");
        TaskList taskList = new TaskList(MAX_TASKS);

        // Load on startup (Level-7)
        taskList.addAll(storage.load());

        ui.showLogo(LOGO);
        ui.showWelcome();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine().trim();

            // Level-5: empty input → ignore silently
            if (input.isEmpty()) {
                continue;
            }

            ParsedCommand cmd = Parser.parse(input);

            if (cmd.type == ParsedCommand.Type.BYE) {
                ui.showBye();
                break;
            }

            if (cmd.type == ParsedCommand.Type.LIST) {
                ui.showList(taskList);
                continue;
            }

            if (cmd.type == ParsedCommand.Type.ERROR) {
                ui.showError(cmd.errorMessage);
                continue;
            }

            // Mutations
            try {switch (cmd.type) {
                case TODO: {
                    Task t = new Todo(cmd.description);
                    taskList.add(t);
                    ui.showAdded(t, taskList.size());
                    storage.save(taskList);
                    break;
                }
                case DEADLINE: {
                    Task t = new Deadline(cmd.description, cmd.byDate);
                    taskList.add(t);
                    ui.showAdded(t, taskList.size());
                    storage.save(taskList);
                    break;
                }
                case EVENT: {
                    Task t = new Event(cmd.description, cmd.from, cmd.to);
                    taskList.add(t);
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
                default:
                    // should not reach
                    ui.showError("OOPS!!! I'm sorry, but I don't know what that means :-(");
                    break;
            }
            } catch (IllegalArgumentException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}

