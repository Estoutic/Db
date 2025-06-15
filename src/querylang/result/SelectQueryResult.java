package querylang.result;

import java.util.List;

public class SelectQueryResult implements QueryResult {
    private final List<List<String>> selectedValues;

    public SelectQueryResult(List<List<String>> selectedValues) {
        this.selectedValues = List.copyOf(selectedValues);
    }

    @Override
    public String message() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedValues.size(); i++) {
            List<String> row = selectedValues.get(i);
            for (int j = 0; j < row.size(); j++) {
                sb.append(row.get(j));
                if (j < row.size() - 1) {
                    sb.append(", ");
                }
            }
            if (i < selectedValues.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}