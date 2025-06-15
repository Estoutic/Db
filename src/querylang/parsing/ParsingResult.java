package querylang.parsing;

import java.util.Objects;
import java.util.function.Function;

// КЛАСС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
//   НО РАЗРЕШАЕТСЯ ДОБАВЛЯТЬ НОВЫЕ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ, НЕ МЕНЯЮЩИЕ ЛОГИКУ КЛАССА
public class ParsingResult<T> {
    private final T value;
    private final String errorMessage;

    private ParsingResult(T value, String errorMessage) {
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public static <T> ParsingResult<T> of(T value) {
        Objects.requireNonNull(value);
        return new ParsingResult<>(value, null);
    }

    public static <T> ParsingResult<T> error(String errorMessage) {
        Objects.requireNonNull(errorMessage);
        return new ParsingResult<>(null, errorMessage);
    }

    public T getValue() {
        Objects.requireNonNull(value);
        return value;
    }

    public String getErrorMessage() {
        Objects.requireNonNull(errorMessage);
        return errorMessage;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isError() {
        return errorMessage != null;
    }

    public <R> ParsingResult<R> map(Function<? super T, ? extends R> mapper) {
        if (isPresent()) {
            return ParsingResult.of(mapper.apply(value));
        } else {
            return ParsingResult.error(errorMessage);
        }
    }
}
