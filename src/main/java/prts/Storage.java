package prts;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import prts.task.Task;
import prts.task.Todo;
import prts.task.Deadline;
import prts.task.Event;

public class Storage {

    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> load() {
        try {
            ensureDataFileExists();

            List<String> lines = Files.readAllLines(Paths.get(filePath));
            ArrayList<Task> result = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Task t = parseTaskFromLine(line);
                if (t != null) {
                    result.add(t);
                }
            }
            return result;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void save(TaskList tasks) {
        try {
            ensureDataFileExists();
            Path file = Paths.get(filePath);
            try (BufferedWriter bw = Files.newBufferedWriter(file)) {
                for (Task t : tasks.toList()) {
                    bw.write(t.toStorageString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: failed to save tasks to disk.");
        }
    }

    private void ensureDataFileExists() throws IOException {
        Path file = Paths.get(filePath);
        Path parent = file.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
    }

    private Task parseTaskFromLine(String line) {
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

        if (done) {
            t.markDone();
        }
        return t;
    }
}
