package querylang.result;

// КЛАСС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
public class ClearQueryResult implements QueryResult {
    private final int total;

    public ClearQueryResult(int total) {
        this.total = total;
    }

    @Override
    public String message() {
        return total + " users were removed successfully";
    }
}
