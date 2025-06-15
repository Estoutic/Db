package querylang.queries;

import querylang.db.Database;
import querylang.result.QueryResult;

// ИНТЕРФЕЙС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
public interface Query {
    QueryResult execute(Database database);
}
