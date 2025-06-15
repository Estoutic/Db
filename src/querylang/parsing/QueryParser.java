package querylang.parsing;

import querylang.queries.ClearQuery;
import querylang.queries.Query;

public class QueryParser {

    // ЗАПРЕЩАЕТСЯ МЕНЯТЬ СИГНАТУРУ, ВОЗВРАЩАЕМЫЙ ТИП И МОДИФИКАТОРЫ ДАННОГО МЕТОДА!
    public ParsingResult<Query> parse(String line) {
        String[] parts = line.strip().split("\\s+", 2);
        String queryName = parts[0].strip().toUpperCase();
        String arguments = parts.length > 1 ? parts[1].strip() : "";
        return switch (queryName) {
            case "CLEAR" -> parseClear(arguments);
            // TODO: Реализовать оставшиеся команды
            default -> ParsingResult.error("Unexpected query name '" + queryName + "'");
        };
    }

    private ParsingResult<Query> parseClear(String arguments) {
        if (!arguments.isEmpty()) {
            return ParsingResult.error("'CLEAR' doesn't accept arguments");
        }
        return ParsingResult.of(new ClearQuery());
    }
}
