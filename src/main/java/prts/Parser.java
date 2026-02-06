/**
 * Parses raw user input into executable commands.
 * <p>
 * This class is responsible for validating user input and converting it
 * into {@link ParsedCommand} objects.
 * </p>
 */

package prts;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import prts.task.Task;
import prts.task.Todo;
import prts.task.Deadline;
import prts.task.Event;

public class Parser {
    /**
     * Parses the user's input string into a {@link ParsedCommand}.
     *
     * @param fullCommand Raw input entered by the user.
     * @return A ParsedCommand representing the user's intent, or an error command if invalid.
     */

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
        // find (Level-9)
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

        // todo
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

        // deadline (Level-8)
        if (input.equals("deadline")) {
            return ParsedCommand.error("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (input.startsWith("deadline ")) {
            if (!input.contains(" /by ")) {
                return ParsedCommand.error("OOPS!!! The format for deadline is: deadline <description> /by <yyyy-mm-dd>");
            }
            String rest = input.substring(8).trim();
            String[] parts = rest.split(" /by ", 2);

            String desc = parts.length > 0 ? parts[0].trim() : "";
            String by = parts.length > 1 ? parts[1].trim() : "";

            if (desc.isEmpty()) {
                return ParsedCommand.error("OOPS!!! The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                return ParsedCommand.error("OOPS!!! The /by part of a deadline cannot be empty.");
            }

            try {
                LocalDate byDate = LocalDate.parse(by);
                return ParsedCommand.deadline(desc, byDate);
            } catch (DateTimeParseException e) {
                return ParsedCommand.error("OOPS!!! Please use date format yyyy-mm-dd, e.g., 2019-10-15");
            }
        }

        // event
        if (input.equals("event")) {
            return ParsedCommand.error("OOPS!!! The description of an event cannot be empty.");
        }
        if (input.startsWith("event ")) {
            if (!input.contains(" /from ") || !input.contains(" /to ")) {
                return ParsedCommand.error("OOPS!!! The format for event is: event <description> /from <from> /to <to>");
            }

            String rest = input.substring(5).trim();
            int fromPos = rest.indexOf(" /from ");
            int toPos = rest.indexOf(" /to ");

            if (fromPos < 0 || toPos < 0 || toPos < fromPos) {
                return ParsedCommand.error("OOPS!!! The format for event is: event <description> /from <from> /to <to>");
            }

            String desc = rest.substring(0, fromPos).trim();
            String from = rest.substring(fromPos + 7, toPos).trim();
            String to = rest.substring(toPos + 5).trim();

            if (desc.isEmpty()) {
                return ParsedCommand.error("OOPS!!! The description of an event cannot be empty.");
            }
            if (from.isEmpty()) {
                return ParsedCommand.error("OOPS!!! The /from part of an event cannot be empty.");
            }
            if (to.isEmpty()) {
                return ParsedCommand.error("OOPS!!! The /to part of an event cannot be empty.");
            }

            return ParsedCommand.event(desc, from, to);
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
