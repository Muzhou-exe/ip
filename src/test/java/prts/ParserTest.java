package prts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests the command parsing logic in the Parser class.
 * Includes positive cases for valid input and negative cases for invalid formats.
 */
public class ParserTest {

    @Test
    public void parse_deadline_validDate_success() {
        // Positive test: valid deadline command with proper date format
        ParsedCommand cmd = Parser.parse("deadline return book /by 2019-12-02");

        assertEquals(ParsedCommand.Type.DEADLINE, cmd.type);
        assertEquals("return book", cmd.description);
        assertEquals(LocalDate.of(2019, 12, 2), cmd.byDate);
    }

    @Test
    public void parse_deadline_invalidDate_errorMessageMatches() {
        // Negative test: invalid date values (month 99)
        ParsedCommand cmd = Parser.parse("deadline return book /by 2019-99-99");

        assertEquals(ParsedCommand.Type.ERROR, cmd.type);
        assertEquals("OOPS!!! Invalid date format. Please use yyyy-mm-dd.", cmd.errorMessage);
    }

    @Test
    public void parse_deleteMissingIndex_errorMessageMatches() {
        // Negative test: delete command without an index
        ParsedCommand cmd = Parser.parse("delete");

        assertEquals(ParsedCommand.Type.ERROR, cmd.type);
        assertEquals("OOPS!!! Please provide a task number. Usage: delete <index>", cmd.errorMessage);
    }
}