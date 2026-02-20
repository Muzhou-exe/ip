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

    public List<String> loadCheers() {
        List<String> lines = new ArrayList<>();
        try {
            Path path = Paths.get("data/cheer.txt");

            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "Keep going!\n");
            }

            for (String line : Files.readAllLines(path)) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    lines.add(trimmed);
                }
            }
        } catch (IOException e) {
            lines.add("You can do it!");
        }
        return lines;
    }

    /**
     * Parses multiple storage lines into tasks.
     */
    public List<Task> parseTaskLines(List<String> lines) {
        List<Task> tasks = new ArrayList<>();

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            Task task = parseTaskLine(line);
            if (task != null) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    /**
     * Parses a single storage line into a task.
     */
    public Task parseTaskLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean isDone = "1".equals(parts[1].trim());
        String description = parts[2].trim();

        Task task;

        switch (type) {
            case "T":
                task = new Todo(description);
                break;

            case "D":
                if (parts.length < 4) {
                    return null;
                }
                task = parseDeadline(description, parts[3].trim());
                break;

            case "E":
                if (parts.length < 5) {
                    return null;
                }
                task = new Event(description, parts[3].trim(), parts[4].trim());
                break;

            default:
                return null;
        }

        if (isDone) {
            task.markDone();
        }

        return task;
    }

    private Task parseDeadline(String description, String byPart) {
        try {
            LocalDate date = LocalDate.parse(byPart);
            return new Deadline(description, date);
        } catch (DateTimeParseException e) {
            return new Deadline(description, byPart);
        }
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
}