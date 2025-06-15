package querylang;

import querylang.db.Database;
import querylang.parsing.ParsingResult;
import querylang.parsing.QueryParser;
import querylang.queries.Query;
import querylang.result.QueryResult;

import java.util.Scanner;

// КЛАСС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QueryParser parser = new QueryParser();
        Database database = new Database();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            ParsingResult<Query> query = parser.parse(line);
            if (query.isError()) {
                System.out.println("ERROR: " + query.getErrorMessage());
                continue;
            }
            QueryResult queryResult = query.getValue().execute(database);
            System.out.println(queryResult.message());
        }
    }
}
