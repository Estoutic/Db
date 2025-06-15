package querylang.db;

import java.util.Objects;

// КЛАСС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
public final class User {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String city;
    private final int age;

    public User(int id, String firstName, String lastName, String city, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
    }

    public int id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String city() {
        return city;
    }

    public int age() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        User that = (User) obj;
        return this.id == that.id &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.city, that.city) &&
                this.age == that.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, city, age);
    }

    @Override
    public String toString() {
        return "User[" +
                "id=" + id + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "city=" + city + ", " +
                "age=" + age + ']';
    }
}
