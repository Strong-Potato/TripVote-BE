package fc.be.app.common.authentication.model;

public abstract class RandomKeyToken implements Token {
    private final String prefix;
    private final String tokenValue;
    private final Object target;
    private boolean authenticated;

    public RandomKeyToken(String prefix, String tokenValue, Object target, boolean authenticated) {
        this.prefix = prefix;
        this.tokenValue = tokenValue;
        this.target = target;
        this.authenticated = authenticated;
    }

    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getTokenValue() {
        return this.tokenValue;
    }

    public Object getTarget() {
        return this.target;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RandomKeyToken test)) {
            return false;
        }
        if (!this.tokenValue.equals(test.tokenValue)) {
            return false;
        }
        if ((this.target == null) && (test.target != null)) {
            return false;
        }
        if ((this.target != null) && (test.target == null)) {
            return false;
        }
        if ((this.target != null) && (!this.target.equals(test.target))) {
            return false;
        }
        return this.isAuthenticated() == test.isAuthenticated();
    }
}
