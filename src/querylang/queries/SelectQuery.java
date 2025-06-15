package querylang.queries;

import querylang.db.Database;
import querylang.db.User;
import querylang.result.SelectQueryResult;
import querylang.util.FieldGetter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SelectQuery implements Query {
    private final List<? extends FieldGetter> getters;
    private final Predicate<? super User> predicate;
    private final Comparator<? super User> comparator;

    public SelectQuery(
            List<? extends FieldGetter> getters,
            Predicate<? super User> predicate,
            Comparator<? super User> comparator
    ) {
        this.getters = getters;
        this.predicate = predicate;
        this.comparator = comparator;
    }

    @Override
    public SelectQueryResult execute(Database database) {
        List<User> users = new ArrayList<>(database.getAll());

        if (predicate != null) {
            users.removeIf(user -> !predicate.test(user));
        }

        if (comparator != null) {
            users.sort(comparator);
        } else {
            users.sort(Comparator.comparingInt(User::id));
        }

        List<List<String>> result = new ArrayList<>();
        for (User user : users) {
            List<String> row = new ArrayList<>();
            for (FieldGetter getter : getters) {
                row.add(getter.getFieldValue(user));
            }
            result.add(row);
        }

        return new SelectQueryResult(result);
    }
}