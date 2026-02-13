package prts;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses raw user input into executable commands.
 * <p>
 * This class is responsible for validating user input and converting it
 * into {@link ParsedCommand} objects.
 * </p>
 */
public class Parser {

    public static ParsedCommand parse(String input) {

        // bye / list
        if (input.equals("bye")) {
            return ParsedCommand.bye();
        }
        if (input.equals("cheer")) {
            return ParsedCommand.cheer();
        }
        if (input.equals("list")) {
            return ParsedCommand.list();
        }
        // find
        if (input.equals("find")) {
            return ParsedCommand.error("OOPS!!! Usage: find <keyword>");
        }
        if (input.startsWith("find ")) {
            String keyword = input.substring(5).trim();
            if (keyword.isEmpty()) {
                return ParsedCommand.error("OOPS!!! Usage: find <keyword>");
            }
            return ParsedCommand.find(keyword);
        }

        // delete
        if (input.equals("delete")) {
            return ParsedCommand.error("OOPS!!! Please provide a task number. Usage: delete <index>");
        }
        if (input.startsWith("delete ")) {
            Integer idx = parseIndex(input.substring(7));
            if (idx == null) {
                return ParsedCommand.error("OOPS!!! Please provide a valid task number.");
            }
            if (idx < 1) {
                return ParsedCommand.error("OOPS!!! That task number is out of range.");
            }
            return ParsedCommand.delete(idx);
        }

        // mark / unmark
        if (input.equals("mark")) {
            return ParsedCommand.error("OOPS!!! Please provide a task number. Usage: mark <index>");
        }
        if (input.startsWith("mark ")) {
            Integer idx = parseIndex(input.substring(5));
            if (idx == null) {
                return ParsedCommand.error("OOPS!!! Please provide a valid task number.");
            }
            if (idx < 1) {
                return ParsedCommand.error("OOPS!!! That task number is out of range.");
            }
            return ParsedCommand.mark(idx);
        }

        if (input.equals("unmark")) {
            return ParsedCommand.error("OOPS!!! Please provide a task number. Usage: unmark <index>");
        }
        if (input.startsWith("unmark ")) {
            Integer idx = parseIndex(input.substring(7));
            if (idx == null) {
                return ParsedCommand.error("OOPS!!! Please provide a valid task number.");
            }
            if (idx < 1) {
                return ParsedCommand.error("OOPS!!! That task number is out of range.");
            }
            return ParsedCommand.unmark(idx);
        }

        // Task creation
        if (input.equals("todo")) {
            return ParsedCommand.error("OOPS!!! The description of a todo cannot be empty.");
        }
        if (input.startsWith("todo ")) {
            String desc = input.substring(5).trim();
            if (desc.isEmpty()) {
                return ParsedCommand.error("OOPS!!! The description of a todo cannot be empty.");
            }
            return ParsedCommand.todo(desc);
        }

        if (input.equals("deadline")) {
            return ParsedCommand.error("OOPS!!! Usage: deadline <desc> /by <date>");
        }
        if (input.startsWith("deadline ")) {
            String body = input.substring(9).trim();
            int byIdx = body.indexOf("/by");
            if (byIdx == -1) {
                return ParsedCommand.error("OOPS!!! Usage: deadline <desc> /by <date>");
            }
            String desc = body.substring(0, byIdx).trim();
            String byStr = body.substring(byIdx + 3).trim();
            if (desc.isEmpty() || byStr.isEmpty()) {
                return ParsedCommand.error("OOPS!!! Description and deadline cannot be empty.");
            }

            try {
                LocalDate d = LocalDate.parse(byStr);
                return ParsedCommand.deadline(desc, d);
            } catch (DateTimeParseException e) {
                // Fallback or error?
                // Spec says we can accept yyyy-mm-dd. Let's force it or fail.
                return ParsedCommand.error("OOPS!!! Invalid date format. Please use yyyy-mm-dd.");
            }
        }

        if (input.equals("event")) {
            return ParsedCommand.error("OOPS!!! Usage: event <desc> /from <start> /to <end>");
        }
        if (input.startsWith("event ")) {
            String body = input.substring(6).trim();
            int fromIdx = body.indexOf("/from");
            int toIdx = body.indexOf("/to");
            if (fromIdx == -1 || toIdx == -1) {
                return ParsedCommand.error("OOPS!!! Usage: event <desc> /from <start> /to <end>");
            }
            if (fromIdx > toIdx) {
                return ParsedCommand.error("OOPS!!! /from must come before /to");
            }

            String desc = body.substring(0, fromIdx).trim();
            String from = body.substring(fromIdx + 5, toIdx).trim();
            String to = body.substring(toIdx + 3).trim();

            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                return ParsedCommand.error("OOPS!!! All event fields cannot be empty.");
            }
            return ParsedCommand.event(desc, from, to);
        }

        // unknown
        return ParsedCommand.error("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    private static Integer parseIndex(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}