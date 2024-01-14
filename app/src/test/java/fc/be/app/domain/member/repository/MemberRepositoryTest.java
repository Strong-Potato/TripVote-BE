package fc.be.app.domain.member.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.vo.AuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("자체 회원가입을 한 member를")
    class Member_is {
        @Test
        @DisplayName("저장할 수 있다.")
        void _will_save() {
            // given
            Member member = Member.builder()
                    .email("test@test.com")
                    .password("{encoded}password")
                    .nickname("nickname")
                    .provider(AuthProvider.NONE)
                    .providedId("none")
                    .profile("test_profile_url")
                    .build();
            Member newMember = memberRepository.saveAndFlush(member);

            // when
            Member findMember = memberRepository.findById(newMember.getId()).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

            // then
            assertThat(findMember)
                    .extracting("id", "email", "password", "nickname", "provider", "providedId", "profile")
                    .containsExactly(newMember.getId(), "test@test.com", "{encoded}password", "nickname", AuthProvider.NONE, "none", "test_profile_url");
        }

        @Test
        @DisplayName("이메일과 Provider로 찾을 수 있다.")
        void _will_find_by_email_and_provider() {
            // given
            Member member = Member.builder()
                    .email("test@test.com")
                    .password("{encoded}password")
                    .nickname("nickname")
                    .provider(AuthProvider.NONE)
                    .providedId("none")
                    .profile("test_profile_url")
                    .build();
            Member newMember = memberRepository.saveAndFlush(member);

            // when
            Member findMember = memberRepository.findByProviderAndEmail(AuthProvider.NONE, "test@test.com").orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

            // then
            assertThat(findMember)
                    .extracting("id", "email", "password", "nickname", "provider", "providedId", "profile")
                    .containsExactly(newMember.getId(), "test@test.com", "{encoded}password", "nickname", AuthProvider.NONE, "none", "test_profile_url");
        }

        @Test
        @DisplayName("이메일과 Provider로 유저의 존부를 확인할 수 있다.")
        void _will_exists_by_email_and_provider() {
            // given
            Member member = Member.builder()
                    .email("test@test.com")
                    .password("{encoded}password")
                    .nickname("nickname")
                    .provider(AuthProvider.NONE)
                    .providedId("none")
                    .profile("test_profile_url")
                    .build();
            Member newMember = memberRepository.saveAndFlush(member);

            // when
            boolean isExists = memberRepository.existsByProviderAndEmail(AuthProvider.NONE, "test@test.com");

            // then
            assertThat(isExists)
                    .isTrue();
        }
    }
}