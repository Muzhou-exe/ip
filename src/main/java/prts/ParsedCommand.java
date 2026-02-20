package prts;

import java.time.LocalDate;

/**
 * Represents a parsed user command.
 */
public class ParsedCommand {

    enum Type {
        TODO, DEADLINE, EVENT,
        DELETE, MARK, UNMARK,
        LIST, FIND, BYE, CHEER,
        UNDO,
        ERROR
    }

    final Type type;
    final String description;
    final LocalDate byDate;
    final String from;
    final String to;
    final int index;
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

    static ParsedCommand cheer() {
        return new ParsedCommand(Type.CHEER, null, null, null, null, -1, null);
    }

    static ParsedCommand bye() {
        return new ParsedCommand(Type.BYE, null, null, null, null, -1, null);
    }

    static ParsedCommand list() {
        return new ParsedCommand(Type.LIST, null, null, null, null, -1, null);
    }

    static ParsedCommand undo() {
        return new ParsedCommand(Type.UNDO, null, null, null, null, -1, null);
    }

    static ParsedCommand todo(String description) {
        return new ParsedCommand(Type.TODO, description, null, null, null, -1, null);
    }

    static ParsedCommand deadline(String description, LocalDate byDate) {
        return new ParsedCommand(Type.DEADLINE, description, byDate, null, null, -1, null);
    }

    static ParsedCommand event(String description, String from, String to) {
        return new ParsedCommand(Type.EVENT, description, null, from, to, -1, null);
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

    static ParsedCommand find(String keyword) {
        return new ParsedCommand(Type.FIND, keyword, null, null, null, -1, null);
    }

    static ParsedCommand error(String message) {
        return new ParsedCommand(Type.ERROR, null, null, null, null, -1, message);
    }
}