package querylang.result;

import java.util.List;

public class SelectQueryResult implements QueryResult {
    private final List<List<String>> selectedValues;

    public SelectQueryResult(List<List<String>> selectedValues) {
        this.selectedValues = List.copyOf(selectedValues);
    }

    @Override
    public String message() {
        // TODO: Реализовать выбор
        return null;
    }
}
