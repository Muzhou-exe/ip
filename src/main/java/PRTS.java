import java.util.Scanner;

public class PRTS {

    private static final String LOGO = """
                      _____                    _____                _____                    _____         \s
                     /\\    \\                  /\\    \\              /\\    \\                  /\\    \\        \s
                    /::\\    \\                /::\\    \\            /::\\    \\                /::\\    \\       \s
                   /::::\\    \\              /::::\\    \\           \\:::\\    \\              /::::\\    \\      \s
                  /::::::\\    \\            /::::::\\    \\           \\:::\\    \\            /::::::\\    \\     \s
                 /:::/\\:::\\    \\          /:::/\\:::\\    \\           \\:::\\    \\          /:::/\\:::\\    \\    \s
                /:::/__\\:::\\    \\        /:::/__\\:::\\    \\           \\:::\\    \\        /:::/__\\:::\\    \\   \s
               /::::\\   \\:::\\    \\      /::::\\   \\:::\\    \\          /::::\\    \\       \\:::\\   \\:::\\    \\  \s
              /::::::\\   \\:::\\    \\    /::::::\\   \\:::\\    \\        /::::::\\    \\    ___\\:::\\   \\:::\\    \\ \s
             /:::/\\:::\\   \\:::\\____\\  /:::/\\:::\\   \\:::\\____\\      /:::/\\:::\\    \\  /\\   \\:::\\   \\:::\\    \\\s
            /:::/  \\:::\\   \\:::|    |/:::/  \\:::\\   \\:::|    |    /:::/  \\:::\\____\\/::\\   \\:::\\   \\:::\\____\\
            \\::/    \\:::\\  /:::|____|\\::/   |::::\\  /:::|____|   /:::/    \\::/    /\\:::\\   \\:::\\   \\::/    /
             \\/_____/\\:::\\/:::/    /  \\/____|:::::\\/:::/    /   /:::/    / \\/____/  \\:::\\   \\:::\\   \\/____/\s
                      \\::::::/    /         |:::::::::/    /   /:::/    /            \\:::\\   \\:::\\    \\    \s
                       \\::::/    /          |::|\\::::/    /   /:::/    /              \\:::\\   \\:::\\____\\   \s
                        \\::/____/           |::| \\::/____/    \\::/    /                \\:::\\  /:::/    /   \s
                         ~~                 |::|  ~|           \\/____/                  \\:::\\/:::/    /    \s
                                            |::|   |                                     \\::::::/    /     \s
                                            \\::|   |                                      \\::::/    /      \s
                                             \\:|   |                                       \\::/    /       \s
                                              \\|___|                                        \\/____/        \s
            
            
            
            
                       
""";

    // Minimal Task for Level-3
    private static class Task {
        private final String description;
        private boolean isDone;

        Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        boolean isDone() {
            return isDone;
        }

        void markDone() {
            isDone = true;
        }

        void unmarkDone() {
            isDone = false;
        }

        @Override
        public String toString() {
            return "[" + (isDone ? "X" : " ") + "] " + description;
        }
    }

    public static void main(String[] args) {
        System.out.print(LOGO);
        System.out.println("Welcome back, Doctor!");
        System.out.println("What can I do for you?");

        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            if (input.isEmpty()) {
                // small stability improvement: ignore empty lines
                System.out.println("(empty input ignored)");
                continue;
            }

            if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                continue;
            }

            if (input.startsWith("mark ")) {
                Integer index = parseIndex(input.substring(5).trim());
                if (index == null || index < 1 || index > taskCount) {
                    System.out.println("Invalid task number for mark.");
                    continue;
                }

                Task task = tasks[index - 1];
                if (!task.isDone()) {
                    task.markDone();
                }

                System.out.println("Nice! I've marked this task as done:");
                System.out.println(task);
                continue;
            }

            if (input.startsWith("unmark ")) {
                Integer index = parseIndex(input.substring(7).trim());
                if (index == null || index < 1 || index > taskCount) {
                    System.out.println("Invalid task number for unmark.");
                    continue;
                }

                Task task = tasks[index - 1];
                if (task.isDone()) {
                    task.unmarkDone();
                }

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(task);
                continue;
            }

            // default: add a task (Level-2 requirement)
            if (taskCount >= tasks.length) {
                System.out.println("Task list is full (max " + tasks.length + ").");
                continue;
            }

            tasks[taskCount] = new Task(input);
            taskCount++;

            System.out.println("added: " + input);
        }
    }

    private static Integer parseIndex(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}