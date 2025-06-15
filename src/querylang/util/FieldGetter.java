package querylang.util;

import querylang.db.User;

// ИНТЕРФЕЙС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
@FunctionalInterface
public interface FieldGetter {
    String getFieldValue(User user);
}
