package fc.be.app.common.authentication.provider.strategy;

@FunctionalInterface
public interface TokenGenerator {
    String generate();
}
