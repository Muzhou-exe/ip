package prts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void parse_deadline_validDate_success() {
        ParsedCommand cmd = Parser.parse("deadline return book /by 2019-12-02");

        assertEquals(ParsedCommand.Type.DEADLINE, cmd.type);
        assertEquals("return book", cmd.description);
        assertEquals(LocalDate.of(2019, 12, 2), cmd.byDate);
    }

    @Test
    public void parse_deadline_invalidDate_errorMessageMatches() {
        ParsedCommand cmd = Parser.parse("deadline return book /by 2019-99-99");

        assertEquals(ParsedCommand.Type.ERROR, cmd.type);
        assertEquals("OOPS!!! Please use date format yyyy-mm-dd, e.g., 2019-10-15", cmd.errorMessage);
    }

    @Test
    public void parse_deleteMissingIndex_errorMessageMatches() {
        ParsedCommand cmd = Parser.parse("delete");

        assertEquals(ParsedCommand.Type.ERROR, cmd.type);
        assertEquals("OOPS!!! Please provide a task number. Usage: delete <index>", cmd.errorMessage);
    }

    @Test
    public void parse_unknownCommand_errorMessageMatches() {
        ParsedCommand cmd = Parser.parse("whatever");

        assertEquals(ParsedCommand.Type.ERROR, cmd.type);
        assertEquals("OOPS!!! I'm sorry, but I don't know what that means :-(", cmd.errorMessage);
    }
}

