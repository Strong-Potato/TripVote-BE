package fc.be.openapi.tourapi.exception;

@Deprecated(forRemoval = true)
public class WrongRequestException extends RuntimeException {

    public final String msg;

    public WrongRequestException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
