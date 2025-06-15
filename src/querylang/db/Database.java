package querylang.db;

import java.util.ArrayList;
import java.util.List;

// КЛАСС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
public class Database {
    private final List<User> users = new ArrayList<>();
    private int nextId = 0;

    /**
     * Получение неизменяемого списка всех пользователей в базе данных.
     */
    public List<User> getAll() {
        return List.copyOf(users);
    }

    /**
     * Добавляет пользователя в базу данных.
     * Идентификатор пользователю присваивается автоматически.
     * Возвращается идентификатор добавленного пользователя.
     */
    public int add(User user) {
        int id = nextId++;
        user = new User(id, user.firstName(), user.lastName(), user.city(), user.age());
        users.add(user);
        return id;
    }

    /**
     * Удаляет пользователя с указанным идентификатором.
     * @return false, если такого пользователя не существует;
     *         true, если пользователь существует и был успешно удалён.
     */
    public boolean remove(int id) {
        return users.removeIf(user -> id == user.id());
    }

    /**
     * Возвращает количество записей в базе данных.
     */
    public int size() {
        return users.size();
    }

    /**
     * Очищает базу данных.
     */
    public void clear() {
        users.clear();
    }
}
