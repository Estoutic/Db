package querylang.parsing;

import org.junit.jupiter.api.Test;
import querylang.queries.*;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {
    private QueryParser parser = new QueryParser();

    @Test
    void testClearCommand() {
        var result = parser.parse("CLEAR");
        assertTrue(result.isPresent());
        assertInstanceOf(ClearQuery.class, result.getValue());

        result = parser.parse("CLEAR something");
        assertTrue(result.isError());
    }

    @Test
    void testInsertCommand() {
        var result = parser.parse("INSERT (Sasha, Belov, Moscow, 28)");
        assertTrue(result.isPresent());
        assertInstanceOf(InsertQuery.class, result.getValue());

        result = parser.parse("INSERT (Kosmos, Pchelkin, Moscow, -5)");
        assertTrue(result.isError());

        result = parser.parse("INSERT (, Belov, Moscow, 25)");
        assertTrue(result.isError());
    }

    @Test
    void testRemoveCommand() {
        var result = parser.parse("REMOVE 42");
        assertTrue(result.isPresent());
        assertInstanceOf(RemoveQuery.class, result.getValue());

        result = parser.parse("REMOVE abc");
        assertTrue(result.isError());
    }

    @Test
    void testSelectCommand() {
        var result = parser.parse("SELECT *");
        assertTrue(result.isPresent());

        result = parser.parse("SELECT (firstName, lastName)");
        assertTrue(result.isPresent());

        result = parser.parse("SELECT * FILTER(city, Moscow)");
        assertTrue(result.isPresent());

        result = parser.parse("SELECT * ORDER(age, ASC)");
        assertTrue(result.isPresent());

        result = parser.parse("SELECT (unknown)");
        assertTrue(result.isError());
    }

    @Test
    void testCaseInsensitive() {
        var result = parser.parse("select *");
        assertTrue(result.isPresent());

        result = parser.parse("SeLeCt *");
        assertTrue(result.isPresent());
    }
}