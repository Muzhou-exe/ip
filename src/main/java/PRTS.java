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

    // ===================== Task classes =====================

    private static class Task {
        protected final String description;
        private boolean isDone;

        Task(String description) {
            this.description = description;
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
        Task[] tasks = new Task[MAX_TASKS];
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

            // ===================== Level-6: delete =====================
            if (input.equals("delete")) {
                System.out.println("OOPS!!! Please provide a task number. Usage: delete <index>");
                continue;
            }
            if (input.startsWith("delete ")) {
                Integer index = parseIndex(input.substring(7));
                if (index == null) {
                    System.out.println("OOPS!!! Please provide a valid task number.");
                    continue;
                }
                if (index < 1 || index > taskCount) {
                    System.out.println("OOPS!!! That task number is out of range.");
                    continue;
                }

                Task removed = tasks[index - 1];

                // shift left to fill the gap
                for (int i = index; i < taskCount; i++) {
                    tasks[i - 1] = tasks[i];
                }
                tasks[taskCount - 1] = null;
                taskCount--;

                printDeletedTask(removed, taskCount);
                continue;
            }

            // ---------------- todo ----------------
            if (input.equals("todo")) {
                System.out.println("OOPS!!! The description of a todo cannot be empty.");
                continue;
            }
            if (input.startsWith("todo ")) {
                if (taskCount >= tasks.length) {
                    System.out.println("OOPS!!! Your task list is full (max " + MAX_TASKS + " tasks).");
                    continue;
                }

                String desc = input.substring(5).trim();
                if (desc.isEmpty()) {
                    System.out.println("OOPS!!! The description of a todo cannot be empty.");
                    continue;
                }

                Task t = new Todo(desc);
                tasks[taskCount++] = t;
                printAddedTask(t, taskCount);
                continue;
            }

            // ---------------- deadline ----------------
            if (input.equals("deadline")) {
                System.out.println("OOPS!!! The description of a deadline cannot be empty.");
                continue;
            }
            if (input.startsWith("deadline ")) {
                if (taskCount >= tasks.length) {
                    System.out.println("OOPS!!! Your task list is full (max " + MAX_TASKS + " tasks).");
                    continue;
                }

                if (!input.contains(" /by ")) {
                    System.out.println("OOPS!!! The format for deadline is: deadline <description> /by <by>");
                    continue;
                }

                String rest = input.substring(8).trim(); // after "deadline"
                String[] parts = rest.split(" /by ", 2);

                String desc = parts.length > 0 ? parts[0].trim() : "";
                String by = parts.length > 1 ? parts[1].trim() : "";

                if (desc.isEmpty()) {
                    System.out.println("OOPS!!! The description of a deadline cannot be empty.");
                    continue;
                }
                if (by.isEmpty()) {
                    System.out.println("OOPS!!! The /by part of a deadline cannot be empty.");
                    continue;
                }

                Task t = new Deadline(desc, by);
                tasks[taskCount++] = t;
                printAddedTask(t, taskCount);
                continue;
            }

            // ---------------- event ----------------
            if (input.equals("event")) {
                System.out.println("OOPS!!! The description of an event cannot be empty.");
                continue;
            }
            if (input.startsWith("event ")) {
                if (taskCount >= tasks.length) {
                    System.out.println("OOPS!!! Your task list is full (max " + MAX_TASKS + " tasks).");
                    continue;
                }

                if (!input.contains(" /from ") || !input.contains(" /to ")) {
                    System.out.println("OOPS!!! The format for event is: event <description> /from <from> /to <to>");
                    continue;
                }

                String rest = input.substring(5).trim(); // after "event"
                int fromPos = rest.indexOf(" /from ");
                int toPos = rest.indexOf(" /to ");

                if (fromPos < 0 || toPos < 0 || toPos < fromPos) {
                    System.out.println("OOPS!!! The format for event is: event <description> /from <from> /to <to>");
                    continue;
                }

                String desc = rest.substring(0, fromPos).trim();
                String from = rest.substring(fromPos + 7, toPos).trim();
                String to = rest.substring(toPos + 5).trim();

                if (desc.isEmpty()) {
                    System.out.println("OOPS!!! The description of an event cannot be empty.");
                    continue;
                }
                if (from.isEmpty()) {
                    System.out.println("OOPS!!! The /from part of an event cannot be empty.");
                    continue;
                }
                if (to.isEmpty()) {
                    System.out.println("OOPS!!! The /to part of an event cannot be empty.");
                    continue;
                }

                Task t = new Event(desc, from, to);
                tasks[taskCount++] = t;
                printAddedTask(t, taskCount);
                continue;
            }

            // ---------------- mark / unmark ----------------
            if (input.equals("mark")) {
                System.out.println("OOPS!!! Please provide a task number. Usage: mark <index>");
                continue;
            }
            if (input.startsWith("mark ")) {
                Integer index = parseIndex(input.substring(5));
                if (index == null) {
                    System.out.println("OOPS!!! Please provide a valid task number.");
                    continue;
                }
                if (index < 1 || index > taskCount) {
                    System.out.println("OOPS!!! That task number is out of range.");
                    continue;
                }

                tasks[index - 1].markDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(tasks[index - 1]);
                continue;
            }

            if (input.equals("unmark")) {
                System.out.println("OOPS!!! Please provide a task number. Usage: unmark <index>");
                continue;
            }
            if (input.startsWith("unmark ")) {
                Integer index = parseIndex(input.substring(7));
                if (index == null) {
                    System.out.println("OOPS!!! Please provide a valid task number.");
                    continue;
                }
                if (index < 1 || index > taskCount) {
                    System.out.println("OOPS!!! That task number is out of range.");
                    continue;
                }

                tasks[index - 1].unmarkDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(tasks[index - 1]);
                continue;
            }

            // ---------------- unknown command ----------------
            System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void printDeletedTask(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static Integer parseIndex(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
