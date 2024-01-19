package fc.be.app.common.authentication.model;

public interface Token {
    Object getTokenValue();

    boolean isAuthenticated();
}
