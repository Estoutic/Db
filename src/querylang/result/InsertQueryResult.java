package querylang.result;

public class InsertQueryResult implements QueryResult {
    private final int id;

    public InsertQueryResult(int id) {
        this.id = id;
    }

    @Override
    public String message() {
        return "User with id " + id + " was added successfully";
    }
}