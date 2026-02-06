import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Locale;

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

    // Level-7/8 storage file (relative to project root)
    private static final String DATA_FILE_PATH = "data/prts.txt";

    // Level-8: output format (e.g., Oct 15 2019)
    private static final DateTimeFormatter OUT_DATE_FMT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

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

        boolean isDone() {
            return isDone;
        }

        protected String statusIcon() {
            return "[" + (isDone ? "X" : " ") + "]";
        }

        /**
         * Convert this task into a single-line storage format.
         * Subclasses should override this to include their extra fields.
         */
        String toStorageString() {
            // default as Todo
            return "T | " + (isDone ? 1 : 0) + " | " + description;
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
        String toStorageString() {
            return "T | " + (isDone() ? 1 : 0) + " | " + description;
        }

        @Override
        public String toString() {
            return "[T]" + super.toString();
        }
    }

    /**
     * Level-8: store 'by' as LocalDate (when available).
     * Also keeps a raw string fallback for backward compatibility (old saves).
     */
    private static class Deadline extends Task {
        private final LocalDate byDate; // preferred (Level-8)
        private final String byRaw;     // fallback if date can't be parsed (old data)

        // New constructor: strict date
        Deadline(String description, LocalDate byDate) {
            super(description);
            this.byDate = byDate;
            this.byRaw = null;
        }

        // Fallback constructor: raw string (for loading old data)
        Deadline(String description, String byRaw) {
            super(description);
            this.byDate = null;
            this.byRaw = byRaw;
        }

        @Override
        String toStorageString() {
            // Save in ISO yyyy-mm-dd when we have a LocalDate; otherwise save raw
            String byPart = (byDate != null) ? byDate.toString() : byRaw;
            return "D | " + (isDone() ? 1 : 0) + " | " + description + " | " + byPart;
        }

        @Override
        public String toString() {
            if (byDate != null) {
                return "[D]" + super.toString() + " (by: " + byDate.format(OUT_DATE_FMT) + ")";
            }
            // fallback printing for old data
            return "[D]" + super.toString() + " (by: " + byRaw + ")";
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
        String toStorageString() {
            return "E | " + (isDone() ? 1 : 0) + " | " + description + " | " + from + " | " + to;
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
        int taskCount = loadTasks(tasks);

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
                saveTasks(tasks, taskCount);
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
                saveTasks(tasks, taskCount);
                continue;
            }

            // ---------------- deadline (Level-8) ----------------
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
                    System.out.println("OOPS!!! The format for deadline is: deadline <description> /by <yyyy-mm-dd>");
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

                LocalDate byDate;
                try {
                    // Level-8 minimal: accept yyyy-mm-dd
                    byDate = LocalDate.parse(by);
                } catch (DateTimeParseException e) {
                    System.out.println("OOPS!!! Please use date format yyyy-mm-dd, e.g., 2019-10-15");
                    continue;
                }

                Task t = new Deadline(desc, byDate);
                tasks[taskCount++] = t;
                printAddedTask(t, taskCount);
                saveTasks(tasks, taskCount);
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
                saveTasks(tasks, taskCount);
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
                saveTasks(tasks, taskCount);
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
                saveTasks(tasks, taskCount);
                continue;
            }

            // ---------------- unknown command ----------------
            System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    // ===================== UI Helpers =====================

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

    // ===================== Level-7/8 Storage =====================

    private static void ensureDataFileExists() throws IOException {
        Path file = Paths.get(DATA_FILE_PATH);
        Path parent = file.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
    }

    private static int loadTasks(Task[] tasks) {
        try {
            ensureDataFileExists();
            List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_PATH));

            int count = 0;
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Task t = parseTaskFromLine(line);
                if (t == null) {
                    // Corrupted / unknown line -> skip (do not crash)
                    continue;
                }
                if (count >= tasks.length) {
                    break;
                }
                tasks[count++] = t;
            }
            return count;
        } catch (IOException e) {
            // If loading fails, start with empty list (do not crash)
            return 0;
        }
    }

    private static void saveTasks(Task[] tasks, int taskCount) {
        try {
            ensureDataFileExists();
            Path file = Paths.get(DATA_FILE_PATH);
            try (BufferedWriter bw = Files.newBufferedWriter(file)) {
                for (int i = 0; i < taskCount; i++) {
                    bw.write(tasks[i].toStorageString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            // Saving failure should not crash the app
            System.out.println("Warning: failed to save tasks to disk.");
        }
    }

    private static Task parseTaskFromLine(String line) {
        // Format: TYPE | doneFlag | desc | ...
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        String doneFlag = parts[1].trim();
        String desc = parts[2].trim();

        boolean done = "1".equals(doneFlag);

        Task t;
        switch (type) {
            case "T":
                t = new Todo(desc);
                break;
            case "D":
                if (parts.length < 4) {
                    return null;
                }
                String byPart = parts[3].trim();
                // Level-8: try parse as yyyy-mm-dd; if fails, keep raw (backward compatible)
                try {
                    LocalDate d = LocalDate.parse(byPart);
                    t = new Deadline(desc, d);
                } catch (DateTimeParseException e) {
                    t = new Deadline(desc, byPart);
                }
                break;
            case "E":
                if (parts.length < 5) {
                    return null;
                }
                t = new Event(desc, parts[3].trim(), parts[4].trim());
                break;
            default:
                return null;
        }

        if (done) {
            t.markDone();
        }
        return t;
    }
}
