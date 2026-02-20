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
 */
public class Storage {

    private static final String CHEER_PATH = "data/cheer.txt";

    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> load() throws IOException {
        ensureDataFileExists();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return parseTaskLines(lines);
    }

    public void save(TaskList taskList) {
        try {
            ensureDataFileExists();

            List<String> lines = new ArrayList<>();
            for (Task task : taskList.toList()) {
                lines.add(task.toStorageString());
            }

            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            System.out.println("OOPS!!! Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Parses multiple storage lines into tasks.
     *
     * @param lines Raw storage lines.
     * @return Parsed tasks.
     */
    public List<Task> parseTaskLines(List<String> lines) {
        List<Task> result = new ArrayList<>();

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            Task task = parseTaskLine(line);
            if (task != null) {
                result.add(task);
            }
        }

        return result;
    }

    /**
     * Parses a single storage line into a task.
     *
     * @param line Raw storage line.
     * @return Task, or null if malformed.
     */
    public Task parseTaskLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean done = "1".equals(parts[1].trim());
        String desc = parts[2].trim();

        Task task;

        switch (type) {
            case "T":
                task = new Todo(desc);
                break;

            case "D":
                task = parseDeadline(parts, desc);
                break;

            case "E":
                task = parseEvent(parts, desc);
                break;

            default:
                return null;
        }

        if (done) {
            task.markDone();
        }
        return task;
    }

    private Task parseDeadline(String[] parts, String desc) {
        if (parts.length < 4) {
            return null;
        }

        String byPart = parts[3].trim();
        try {
            LocalDate date = LocalDate.parse(byPart);
            return new Deadline(desc, date);
        } catch (DateTimeParseException e) {
            return new Deadline(desc, byPart);
        }
    }

    private Task parseEvent(String[] parts, String desc) {
        if (parts.length < 5) {
            return null;
        }
        return new Event(desc, parts[3].trim(), parts[4].trim());
    }

    private void ensureDataFileExists() throws IOException {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.createFile(path);
        }
    }

    public List<String> loadCheers() {
        List<String> lines = new ArrayList<>();

        try {
            Path path = Paths.get(CHEER_PATH);

            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "Keep going!\n");
            }

            for (String line : Files.readAllLines(path)) {
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