package fc.be;

import fc.be.openapi.OpenAPIRoot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {
        OpenAPIRoot.class,
        NotificationRoot.class,
        TripVoteApplication.class
})
public class TripVoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripVoteApplication.class, args);
    }

}
