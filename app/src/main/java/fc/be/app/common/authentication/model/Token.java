package fc.be.app.common.authentication.model;

public interface Token {
    String getTokenValue();

    boolean isAuthenticated();
}
