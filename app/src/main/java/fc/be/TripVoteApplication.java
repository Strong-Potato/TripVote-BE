package fc.be;

import fc.be.openapi.OpenAPIRoot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
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
