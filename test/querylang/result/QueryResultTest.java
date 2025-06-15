package querylang.result;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryResultTest {

    @Test
    void testResultMessages() {
        ClearQueryResult clearResult = new ClearQueryResult(4);
        assertEquals("4 users were removed successfully", clearResult.message());

        InsertQueryResult insertResult = new InsertQueryResult(777);
        assertEquals("User with id 777 was added successfully", insertResult.message());

        RemoveQueryResult removeSuccess = new RemoveQueryResult(1, true);
        assertEquals("User with id 1 was removed successfully", removeSuccess.message());

        RemoveQueryResult removeFail = new RemoveQueryResult(999, false);
        assertEquals("User with id 999 doesn't exist", removeFail.message());

        List<List<String>> data = Arrays.asList(
                Arrays.asList("Sasha", "28"),
                Arrays.asList("Kosmos", "27"),
                Arrays.asList("Phil", "26")
        );
        SelectQueryResult selectResult = new SelectQueryResult(data);
        assertEquals("Sasha, 28\nKosmos, 27\nPhil, 26", selectResult.message());
    }
}