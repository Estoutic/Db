package querylang.result;

public class RemoveQueryResult implements QueryResult {
    private final int id;
    private final boolean success;

    public RemoveQueryResult(int id, boolean success) {
        this.id = id;
        this.success = success;
    }

    @Override
    public String message() {
        return success ? "User with id " + id + " was removed successfully" : "User with id " + id + " does not exist";
    }
}