package querylang.queries;

import querylang.db.Database;
import querylang.result.ClearQueryResult;
import querylang.result.QueryResult;

// КЛАСС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
public class ClearQuery implements Query {
    @Override
    public ClearQueryResult execute(Database database) {
        int total = database.size();
        database.clear();
        return new ClearQueryResult(total);
    }
}
