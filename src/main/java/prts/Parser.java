package prts;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses raw user input into executable commands.
 */
public class Parser {

    public static ParsedCommand parse(String input) {
        String trimmed = input.trim();

        if (trimmed.equals("bye")) {
            return ParsedCommand.bye();
        }
        if (trimmed.equals("cheer")) {
            return ParsedCommand.cheer();
        }
        if (trimmed.equals("list")) {
            return ParsedCommand.list();
        }
        if (trimmed.equals("undo")) {
            return ParsedCommand.undo();
        }

        if (trimmed.equals("find")) {
            return ParsedCommand.error("OOPS!!! Usage: find <keyword>");
        }
        if (trimmed.startsWith("find ")) {
            String keyword = trimmed.substring(5).trim();
            if (keyword.isEmpty()) {
                return ParsedCommand.error("OOPS!!! Usage: find <keyword>");
            }
            return ParsedCommand.find(keyword);
        }

        if (trimmed.equals("delete")) {
            return ParsedCommand.error("OOPS!!! Please provide a task number. Usage: delete <index>");
        }
        if (trimmed.startsWith("delete ")) {
            Integer index = parseIndex(trimmed.substring(7));
            if (index == null) {
                return ParsedCommand.error("OOPS!!! Please provide a valid task number.");
            }
            if (index < 1) {
                return ParsedCommand.error("OOPS!!! That task number is out of range.");
            }
            return ParsedCommand.delete(index);
        }

        if (trimmed.equals("mark")) {
            return ParsedCommand.error("OOPS!!! Please provide a task number. Usage: mark <index>");
        }
        if (trimmed.startsWith("mark ")) {
            Integer index = parseIndex(trimmed.substring(5));
            if (index == null) {
                return ParsedCommand.error("OOPS!!! Please provide a valid task number.");
            }
            if (index < 1) {
                return ParsedCommand.error("OOPS!!! That task number is out of range.");
            }
            return ParsedCommand.mark(index);
        }

        if (trimmed.equals("unmark")) {
            return ParsedCommand.error("OOPS!!! Please provide a task number. Usage: unmark <index>");
        }
        if (trimmed.startsWith("unmark ")) {
            Integer index = parseIndex(trimmed.substring(7));
            if (index == null) {
                return ParsedCommand.error("OOPS!!! Please provide a valid task number.");
            }
            if (index < 1) {
                return ParsedCommand.error("OOPS!!! That task number is out of range.");
            }
            return ParsedCommand.unmark(index);
        }

        if (trimmed.equals("todo")) {
            return ParsedCommand.error("OOPS!!! The description of a todo cannot be empty.");
        }
        if (trimmed.startsWith("todo ")) {
            String description = trimmed.substring(5).trim();
            if (description.isEmpty()) {
                return ParsedCommand.error("OOPS!!! The description of a todo cannot be empty.");
            }
            return ParsedCommand.todo(description);
        }

        if (trimmed.equals("deadline")) {
            return ParsedCommand.error("OOPS!!! Usage: deadline <desc> /by <date>");
        }
        if (trimmed.startsWith("deadline ")) {
            String body = trimmed.substring(9).trim();
            int byIndex = body.indexOf("/by");
            if (byIndex == -1) {
                return ParsedCommand.error("OOPS!!! Usage: deadline <desc> /by <date>");
            }

            String description = body.substring(0, byIndex).trim();
            String byString = body.substring(byIndex + 3).trim();
            if (description.isEmpty() || byString.isEmpty()) {
                return ParsedCommand.error("OOPS!!! Description and deadline cannot be empty.");
            }

            try {
                LocalDate date = LocalDate.parse(byString);
                return ParsedCommand.deadline(description, date);
            } catch (DateTimeParseException e) {
                return ParsedCommand.error("OOPS!!! Invalid date format. Please use yyyy-mm-dd.");
            }
        }

        if (trimmed.equals("event")) {
            return ParsedCommand.error("OOPS!!! Usage: event <desc> /from <start> /to <end>");
        }
        if (trimmed.startsWith("event ")) {
            String body = trimmed.substring(6).trim();
            int fromIndex = body.indexOf("/from");
            int toIndex = body.indexOf("/to");
            if (fromIndex == -1 || toIndex == -1) {
                return ParsedCommand.error("OOPS!!! Usage: event <desc> /from <start> /to <end>");
            }
            if (fromIndex > toIndex) {
                return ParsedCommand.error("OOPS!!! /from must come before /to");
            }

            String description = body.substring(0, fromIndex).trim();
            String from = body.substring(fromIndex + 5, toIndex).trim();
            String to = body.substring(toIndex + 3).trim();

            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                return ParsedCommand.error("OOPS!!! All event fields cannot be empty.");
            }
            return ParsedCommand.event(description, from, to);
        }

        return ParsedCommand.error("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    private static Integer parseIndex(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}