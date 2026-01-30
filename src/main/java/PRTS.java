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

    // ===================== Task classes =====================

    private static class Task {
        protected final String description;
        private boolean isDone;

        Task(String description) {
            this.description = description;
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

    // ===================== Main =====================

    public static void main(String[] args) {
        System.out.print(LOGO);
        System.out.println("Welcome back, Doctor!");
        System.out.println("What can I do for you?");

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine().trim();

            // Level-5: empty input → ignore silently
            if (input.isEmpty()) {
                continue;
            }

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                continue;
            }

            // ---------------- todo ----------------
            if (input.startsWith("todo")) {
                if (!input.startsWith("todo ")) {
                    System.out.println("Invalid todo format. Usage: todo <description>");
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

            // ---------------- deadline ----------------
            if (input.startsWith("deadline")) {
                if (!input.contains(" /by ")) {
                    System.out.println("Invalid deadline format. Usage: deadline <description> /by <by>");
                    continue;
                }

                String rest = input.substring(9).trim();
                String[] parts = rest.split(" /by ", 2);
                if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
                    System.out.println("Invalid deadline format. Usage: deadline <description> /by <by>");
                    continue;
                }

                tasks[taskCount++] = new Deadline(parts[0].trim(), parts[1].trim());
                System.out.println("added: " + parts[0].trim());
                continue;
            }

            // ---------------- event ----------------
            if (input.startsWith("event")) {
                if (!input.contains(" /from ") || !input.contains(" /to ")) {
                    System.out.println("Invalid event format. Usage: event <description> /from <from> /to <to>");
                    continue;
                }

                String rest = input.substring(6).trim();
                int fromPos = rest.indexOf(" /from ");
                int toPos = rest.indexOf(" /to ");

                if (fromPos < 0 || toPos < fromPos) {
                    System.out.println("Invalid event format. Usage: event <description> /from <from> /to <to>");
                    continue;
                }

                String desc = rest.substring(0, fromPos).trim();
                String from = rest.substring(fromPos + 7, toPos).trim();
                String to = rest.substring(toPos + 5).trim();

                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    System.out.println("Invalid event format. Usage: event <description> /from <from> /to <to>");
                    continue;
                }

                tasks[taskCount++] = new Event(desc, from, to);
                System.out.println("added: " + desc);
                continue;
            }

            // ---------------- mark / unmark ----------------
            if (input.startsWith("mark ")) {
                Integer index = parseIndex(input.substring(5));
                if (index == null || index < 1 || index > taskCount) {
                    System.out.println("Invalid task number.");
                    continue;
                }

                tasks[index - 1].markDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(tasks[index - 1]);
                continue;
            }

            if (input.startsWith("unmark ")) {
                Integer index = parseIndex(input.substring(7));
                if (index == null || index < 1 || index > taskCount) {
                    System.out.println("Invalid task number.");
                    continue;
                }

                tasks[index - 1].unmarkDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(tasks[index - 1]);
                continue;
            }

            // ---------------- unknown command ----------------
            System.out.println("I don't understand that command.");
        }
    }

    private static Integer parseIndex(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
