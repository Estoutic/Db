package querylang.parsing;

import querylang.db.User;
import querylang.queries.*;
import querylang.util.FieldGetter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class QueryParser {

    // ЗАПРЕЩАЕТСЯ МЕНЯТЬ СИГНАТУРУ, ВОЗВРАЩАЕМЫЙ ТИП И МОДИФИКАТОРЫ ДАННОГО МЕТОДА!
    public ParsingResult<Query> parse(String line) {
        String[] parts = line.strip().split("\\s+", 2);
        String queryName = parts[0].strip().toUpperCase();
        String arguments = parts.length > 1 ? parts[1].strip() : "";
        return switch (queryName) {
            case "CLEAR" -> parseClear(arguments);
            case "INSERT" -> parseInsert(arguments);
            case "REMOVE" -> parseRemove(arguments);
            case "SELECT" -> parseSelect(arguments);
            default -> ParsingResult.error("Unexpected query name '" + queryName + "'");
        };
    }

    private ParsingResult<Query> parseClear(String arguments) {
        if (!arguments.isEmpty()) {
            return ParsingResult.error("'CLEAR' does not accept arguments");
        }
        return ParsingResult.of(new ClearQuery());
    }

    private ParsingResult<Query> parseInsert(String arguments) {
        if (!arguments.startsWith("(") || !arguments.endsWith(")")) {
            return ParsingResult.error("Invalid INSERT syntax");
        }

        String content = arguments.substring(1, arguments.length() - 1);
        String[] fields = content.split(",");

        if (fields.length != 4) {
            return ParsingResult.error("INSERT requires exactly 4 parameters");
        }

        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }

        if (fields[0].isEmpty() || fields[1].isEmpty() || fields[2].isEmpty()) {
            return ParsingResult.error("String values cannot be empty");
        }

        int age;
        try {
            age = Integer.parseInt(fields[3]);
            if (age < 0) {
                return ParsingResult.error("Age must be non-negative");
            }
        } catch (NumberFormatException e) {
            return ParsingResult.error("Age must be a valid integer");
        }

        User user = new User(0, fields[0], fields[1], fields[2], age);
        return ParsingResult.of(new InsertQuery(user));
    }

    private ParsingResult<Query> parseRemove(String arguments) {
        try {
            int id = Integer.parseInt(arguments);
            return ParsingResult.of(new RemoveQuery(id));
        } catch (NumberFormatException e) {
            return ParsingResult.error("Invalid id format");
        }
    }

    private ParsingResult<Query> parseSelect(String arguments) {
        List<FieldGetter> getters = new ArrayList<>();
        Predicate<User> predicate = null;
        Comparator<User> comparator = null;

        String fieldsStr;
        String rest = "";

        if (arguments.startsWith("*")) {
            getters.add(user -> String.valueOf(user.id()));
            getters.add(user -> user.firstName());
            getters.add(user -> user.lastName());
            getters.add(user -> user.city());
            getters.add(user -> String.valueOf(user.age()));
            rest = arguments.substring(1).trim();
        } else if (arguments.startsWith("(")) {
            int closeIndex = arguments.indexOf(")");
            if (closeIndex == -1) {
                return ParsingResult.error("Missing closing parenthesis");
            }
            fieldsStr = arguments.substring(1, closeIndex);
            rest = arguments.substring(closeIndex + 1).trim();

            String[] fields = fieldsStr.split(",");
            for (String field : fields) {
                String fieldName = field.trim();
                FieldGetter getter = createFieldGetter(fieldName);
                if (getter == null) {
                    return ParsingResult.error("Unknown field: " + fieldName);
                }
                getters.add(getter);
            }
        } else {
            return ParsingResult.error("Invalid SELECT syntax");
        }

        while (!rest.isEmpty()) {
            if (rest.toUpperCase().startsWith("FILTER(")) {
                int startIdx = rest.indexOf("(");
                int endIdx = findMatchingParen(rest, startIdx);
                if (endIdx == -1) {
                    return ParsingResult.error("Invalid FILTER syntax");
                }
                String filterContent = rest.substring(startIdx + 1, endIdx);
                String[] filterParts = filterContent.split(",", 2);
                if (filterParts.length != 2) {
                    return ParsingResult.error("FILTER requires exactly 2 parameters");
                }

                String fieldName = filterParts[0].trim();
                String value = filterParts[1].trim();

                Predicate<User> newPredicate = createPredicate(fieldName, value);
                if (newPredicate == null) {
                    return ParsingResult.error("Unknown field in FILTER: " + fieldName);
                }

                if (predicate == null) {
                    predicate = newPredicate;
                } else {
                    predicate = predicate.and(newPredicate);
                }

                rest = rest.substring(endIdx + 1).trim();
            } else if (rest.toUpperCase().startsWith("ORDER(")) {
                if (comparator != null) {
                    return ParsingResult.error("ORDER can be used only once");
                }
                int startIdx = rest.indexOf("(");
                int endIdx = findMatchingParen(rest, startIdx);
                if (endIdx == -1) {
                    return ParsingResult.error("Invalid ORDER syntax");
                }
                String orderContent = rest.substring(startIdx + 1, endIdx);
                String[] orderParts = orderContent.split(",", 2);
                if (orderParts.length != 2) {
                    return ParsingResult.error("ORDER requires exactly 2 parameters");
                }

                String fieldName = orderParts[0].trim();
                String direction = orderParts[1].trim();

                if (!direction.equalsIgnoreCase("ASC") && !direction.equalsIgnoreCase("DESC")) {
                    return ParsingResult.error("ORDER direction must be ASC or DESC");
                }

                comparator = createComparator(fieldName, direction.equalsIgnoreCase("ASC"));
                if (comparator == null) {
                    return ParsingResult.error("Unknown field in ORDER: " + fieldName);
                }

                rest = rest.substring(endIdx + 1).trim();
            } else {
                return ParsingResult.error("Unexpected token: " + rest);
            }
        }

        return ParsingResult.of(new SelectQuery(getters, predicate, comparator));
    }

    private int findMatchingParen(String str, int openIndex) {
        int count = 1;
        for (int i = openIndex + 1; i < str.length(); i++) {
            if (str.charAt(i) == '(') count++;
            else if (str.charAt(i) == ')') {
                count--;
                if (count == 0) return i;
            }
        }
        return -1;
    }

    private FieldGetter createFieldGetter(String fieldName) {
        return switch (fieldName) {
            case "id" -> user -> String.valueOf(user.id());
            case "firstName" -> User::firstName;
            case "lastName" -> User::lastName;
            case "city" -> User::city;
            case "age" -> user -> String.valueOf(user.age());
            default -> null;
        };
    }

    private Predicate<User> createPredicate(String fieldName, String value) {
        return switch (fieldName) {
            case "id" -> user -> String.valueOf(user.id()).equals(value);
            case "firstName" -> user -> user.firstName().equals(value);
            case "lastName" -> user -> user.lastName().equals(value);
            case "city" -> user -> user.city().equals(value);
            case "age" -> user -> String.valueOf(user.age()).equals(value);
            default -> null;
        };
    }

    private Comparator<User> createComparator(String fieldName, boolean ascending) {
        Comparator<User> baseComparator = switch (fieldName) {
            case "id" -> Comparator.comparingInt(User::id);
            case "firstName" -> Comparator.comparing(User::firstName);
            case "lastName" -> Comparator.comparing(User::lastName);
            case "city" -> Comparator.comparing(User::city);
            case "age" -> Comparator.comparingInt(User::age);
            default -> null;
        };

        if (baseComparator == null) return null;

        if (!ascending) {
            baseComparator = baseComparator.reversed();
        }

        return baseComparator.thenComparing(User::id);
    }
}