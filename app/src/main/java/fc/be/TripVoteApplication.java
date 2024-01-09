package fc.be;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.openapi.OpenAPIRoot;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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

    @Bean
    public CommandLineRunner run(MemberRepository memberRepository) {
        log.info("[Member 가 추가됨]");

        return (String[] args) -> {
            Member member = Member.builder()
                .email("ok@gmail.com")
                .nickname("ok")
                .password("1234")
                .build();

            memberRepository.save(member);
        };
    }
}
