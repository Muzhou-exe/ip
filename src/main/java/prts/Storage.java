package prts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import prts.task.Deadline;
import prts.task.Event;
import prts.task.Task;
import prts.task.Todo;

/**
 * Handles loading and saving task data to disk.
 * <p>
 * This class manages file I/O operations for persistent storage.
 * </p>
 */
public class Storage {

    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return A list of tasks loaded from disk.
     * @throws IOException If reading from the file fails.
     */
    public List<Task> load() throws IOException {
        try {
            ensureDataFileExists();

            List<String> lines = Files.readAllLines(Paths.get(filePath));
            ArrayList<Task> result = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Task t = parseTask(line);
                if (t != null) {
                    result.add(t);
                }
            }
            return result;
        } catch (IOException e) {
            throw e;
        }
    }

    public void save(TaskList taskList) {
        try {
            ensureDataFileExists();
            List<String> lines = new ArrayList<>();
            for (Task t : taskList.toList()) {
                lines.add(t.toStorageString());
            }
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            System.out.println("OOPS!!! Error saving tasks: " + e.getMessage());
        }
    }

    private void ensureDataFileExists() throws IOException {
        Path p = Paths.get(filePath);
        if (!Files.exists(p)) {
            if (p.getParent() != null) {
                Files.createDirectories(p.getParent());
            }
            Files.createFile(p);
        }
    }

    private Task parseTask(String line) {
        // Expected format: TYPE | DONE | DESC | [EXTRA]
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean done = "1".equals(parts[1]);
        String desc = parts[2];

        Task t = null; // Initialize to null

        // Fixed switch-case indentation
        switch (type) {
            case "T":
                t = new Todo(desc);
                break;
            case "D":
                if (parts.length < 4) return null;
                String byPart = parts[3].trim();
                try {
                    LocalDate d = LocalDate.parse(byPart);
                    t = new Deadline(desc, d);
                } catch (DateTimeParseException e) {
                    t = new Deadline(desc, byPart); // backward compatible
                }
                break;
            case "E":
                if (parts.length < 5) return null;
                t = new Event(desc, parts[3].trim(), parts[4].trim());
                break;
            default:
                return null;
        }

        if (t != null && done) {
            t.markDone();
        }
        return t;
    }

    public java.util.List<String> loadCheers() {
        java.util.List<String> lines = new java.util.ArrayList<>();
        try {
            Path p = Paths.get("data/cheer.txt");
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                Files.writeString(p, "Keep going!\n");
            }
            for (String line : Files.readAllLines(p)) {
                String s = line.trim();
                if (!s.isEmpty()) {
                    lines.add(s);
                }
            }
        } catch (IOException e) {
            lines.add("You can do it!");
        }
        return lines;
    }
}