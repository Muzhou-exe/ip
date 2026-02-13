package prts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Tests the parsing logic of user commands.
 */
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
        assertEquals("OOPS!!! Invalid date format. Please use yyyy-mm-dd.", cmd.errorMessage);
    }

    @Test
    public void parse_deleteMissingIndex_errorMessageMatches() {
        ParsedCommand cmd = Parser.parse("delete");
        assertEquals(ParsedCommand.Type.ERROR, cmd.type);
        assertEquals("OOPS!!! Please provide a task number. Usage: delete <index>", cmd.errorMessage);
    }
}