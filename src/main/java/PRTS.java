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

    // ---------- Level-4: Task types ----------

    private static class Task {
        protected final String description;
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

        protected String statusIcon() {
            return "[" + (isDone ? "X" : " ") + "]";
        }

        @Override
        public String toString() {
            // Base format: [ ] description
            return statusIcon() + " " + description;
        }
    }

    private static class Todo extends Task {
        Todo(String description) {
            super(description);
        }

        @Override
        public String toString() {
            return "[T]" + super.toString();
        }
    }

    private static class Deadline extends Task {
        private final String by;

        Deadline(String description, String by) {
            super(description);
            this.by = by;
        }

        @Override
        public String toString() {
            return "[D]" + super.toString() + " (by: " + by + ")";
        }
    }

    private static class Event extends Task {
        private final String from;
        private final String to;

        Event(String description, String from, String to) {
            super(description);
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
        }
    }

    // ---------- Main program ----------

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

            // ---------- Level-4 commands ----------
            if (input.startsWith("todo ")) {
                if (taskCount >= tasks.length) {
                    System.out.println("Task list is full (max " + tasks.length + ").");
                    continue;
                }

                String desc = input.substring(5).trim();
                if (desc.isEmpty()) {
                    System.out.println("Invalid todo format. Usage: todo <description>");
                    continue;
                }

                tasks[taskCount++] = new Todo(desc);
                System.out.println("added: " + desc);
                continue;
            }

            if (input.startsWith("deadline ")) {
                if (taskCount >= tasks.length) {
                    System.out.println("Task list is full (max " + tasks.length + ").");
                    continue;
                }

                String rest = input.substring(9).trim();
                String[] parts = rest.split(" /by ", 2);
                if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    System.out.println("Invalid deadline format. Usage: deadline <description> /by <by>");
                    continue;
                }

                String desc = parts[0].trim();
                String by = parts[1].trim();

                tasks[taskCount++] = new Deadline(desc, by);
                System.out.println("added: " + desc);
                continue;
            }

            if (input.startsWith("event ")) {
                if (taskCount >= tasks.length) {
                    System.out.println("Task list is full (max " + tasks.length + ").");
                    continue;
                }

                String rest = input.substring(6).trim();
                int fromPos = rest.indexOf(" /from ");
                int toPos = rest.indexOf(" /to ");

                if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                    System.out.println("Invalid event format. Usage: event <description> /from <from> /to <to>");
                    continue;
                }

                String desc = rest.substring(0, fromPos).trim();
                String from = rest.substring(fromPos + 7, toPos).trim(); // 7 = " /from ".length()
                String to = rest.substring(toPos + 5).trim();           // 5 = " /to ".length()

                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    System.out.println("Invalid event format. Usage: event <description> /from <from> /to <to>");
                    continue;
                }

                tasks[taskCount++] = new Event(desc, from, to);
                System.out.println("added: " + desc);
                continue;
            }

            // ---------- Existing Level-3 commands ----------
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

            // ---------- Default behavior ----------
            // Keep old behavior: any other input becomes a generic Task
            if (taskCount >= tasks.length) {
                System.out.println("Task list is full (max " + tasks.length + ").");
                continue;
            }

            tasks[taskCount++] = new Task(input);
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