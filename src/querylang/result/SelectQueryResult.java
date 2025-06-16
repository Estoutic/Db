package querylang.result;

import java.util.ArrayList;
import java.util.List;

public class SelectQueryResult implements QueryResult {
    private final List<List<String>> selectedValues;

    public SelectQueryResult(List<List<String>> selectedValues) {
        this.selectedValues = List.copyOf(selectedValues);
    }

    @Override
    public String message() {
        List<String> lines = new ArrayList<>();

        for (List<String> row : selectedValues) {
            String line = String.join(", ", row);
            lines.add(line);
        }

        return String.join("\n", lines);
    }
}