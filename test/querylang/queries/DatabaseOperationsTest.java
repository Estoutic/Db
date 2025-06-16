package querylang.queries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.User;
import querylang.util.FieldGetter;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseOperъationsTest {
    private Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
    }

    @Test
    void testInsertAndSelect() {
        User user = new User(0, "Sasha", "Belov", "Moscow", 28);
        InsertQuery insertQuery = new InsertQuery(user);
        var insertResult = insertQuery.execute(database);

        assertEquals("User with id 0 was added successfully", insertResult.message());
        assertEquals(1, database.size());

        List<FieldGetter> getters = Arrays.asList(
                u -> String.valueOf(u.id()),
                u -> u.firstName(),
                u -> u.lastName(),
                u -> u.city(),
                u -> String.valueOf(u.age())
        );

        SelectQuery selectQuery = new SelectQuery(getters, null, null);
        var selectResult = selectQuery.execute(database);

        assertEquals("0, Sasha, Belov, Moscow, 28", selectResult.message());
    }

    @Test
    void testRemove() {
        database.add(new User(0, "Mukha", "Mukhov", "Petersburg", 35));

        RemoveQuery removeQuery = new RemoveQuery(0);
        var result = removeQuery.execute(database);

        assertEquals("User with id 0 was removed successfully", result.message());
        assertEquals(0, database.size());

        RemoveQuery removeQuery2 = new RemoveQuery(999);
        var result2 = removeQuery2.execute(database);

        assertEquals("User with id 999 does not exist", result2.message());
    }

    @Test
    void testClear() {
        database.add(new User(0, "Kosmos", "Pchelkin", "Moscow", 27));
        database.add(new User(0, "Phil", "Philippovich", "Moscow", 26));
        database.add(new User(0, "Pchela", "Pchelkin", "Moscow", 29));

        ClearQuery clearQuery = new ClearQuery();
        var result = clearQuery.execute(database);

        assertEquals("3 users were removed successfully", result.message());
        assertEquals(0, database.size());
    }

    @Test
    void testSelectWithFilter() {
        database.add(new User(0, "Sasha", "Belov", "Moscow", 28));
        database.add(new User(0, "Farkhad", "Abdullaev", "Petersburg", 45));

        List<FieldGetter> getters = Arrays.asList(u -> u.firstName());

        SelectQuery query = new SelectQuery(
                getters,
                user -> user.city().equals("Moscow"),
                null
        );
        var result = query.execute(database);

        assertEquals("Sasha", result.message());
    }
}