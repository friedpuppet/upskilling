package ua.yelisieiev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.yelisieiev.exceptions.UnsupportedCommandException;

import static org.junit.jupiter.api.Assertions.*;
import static ua.yelisieiev.CommandType.DML;
import static ua.yelisieiev.CommandType.SELECT;

class CommandTest {

    @DisplayName("With correct SELECT text - create command - read type")
    @Test
    void text_correctSelect() throws UnsupportedCommandException {
        Command command = new Command("select * from emp");
        assertEquals(command.getType(), SELECT);
    }

    @DisplayName("With correct INSERT text - create command - read type")
    @Test
    void text_correctInsert() throws UnsupportedCommandException {
        Command command = new Command("insert into emp(id, name) values(1, 'Susanna)");
        assertEquals(command.getType(), DML);
    }

    @DisplayName("With correct SELECT text - create command - read text")
    @Test
    void test_() throws UnsupportedCommandException {
        Command command = new Command("select * from emp");
        assertEquals(command.getText(), "select * from emp");

    }
}