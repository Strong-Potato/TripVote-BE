package fc.be.app.domain.member.provider.strategy;

@FunctionalInterface
public interface TokenGenerator {
    String generate();
}
