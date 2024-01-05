package fc.be.tourapi.exception;


import org.springframework.http.HttpStatus;

@Deprecated(forRemoval = true)
public interface ErrorMessage {
    HttpStatus getStatus();

    String getMessage();
}