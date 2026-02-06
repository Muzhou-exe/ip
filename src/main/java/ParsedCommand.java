import java.time.LocalDate;

public class ParsedCommand {

    enum Type {
        TODO, DEADLINE, EVENT,
        DELETE, MARK, UNMARK,
        LIST, BYE,
        ERROR
    }

    final Type type;

    // Common fields
    final String description;

    // Deadline fields
    final LocalDate byDate;

    // Event fields
    final String from;
    final String to;

    // Index fields (1-based)
    final int index;

    // Error
    final String errorMessage;

    private ParsedCommand(Type type, String description, LocalDate byDate,
                          String from, String to, int index, String errorMessage) {
        this.type = type;
        this.description = description;
        this.byDate = byDate;
        this.from = from;
        this.to = to;
        this.index = index;
        this.errorMessage = errorMessage;
    }

    static ParsedCommand bye() {
        return new ParsedCommand(Type.BYE, null, null, null, null, -1, null);
    }

    static ParsedCommand list() {
        return new ParsedCommand(Type.LIST, null, null, null, null, -1, null);
    }

    static ParsedCommand todo(String desc) {
        return new ParsedCommand(Type.TODO, desc, null, null, null, -1, null);
    }

    static ParsedCommand deadline(String desc, LocalDate byDate) {
        return new ParsedCommand(Type.DEADLINE, desc, byDate, null, null, -1, null);
    }

    static ParsedCommand event(String desc, String from, String to) {
        return new ParsedCommand(Type.EVENT, desc, null, from, to, -1, null);
    }

    static ParsedCommand delete(int index) {
        return new ParsedCommand(Type.DELETE, null, null, null, null, index, null);
    }

    static ParsedCommand mark(int index) {
        return new ParsedCommand(Type.MARK, null, null, null, null, index, null);
    }

    static ParsedCommand unmark(int index) {
        return new ParsedCommand(Type.UNMARK, null, null, null, null, index, null);
    }

    static ParsedCommand error(String msg) {
        return new ParsedCommand(Type.ERROR, null, null, null, null, -1, msg);
    }
}
