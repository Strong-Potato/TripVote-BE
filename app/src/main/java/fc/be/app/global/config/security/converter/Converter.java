package fc.be.app.global.config.security.converter;

public interface Converter<T, R> {
    R convert(T t);
}
