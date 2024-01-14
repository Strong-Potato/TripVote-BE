package fc.be.app.domain.member.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.member.vo.AuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;

@Transactional
@ExtendWith(MockitoExtension.class)
class MemberQueryHandlerTest {
    @InjectMocks
    private MemberQueryHandler memberQueryHandler;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("저장되어 있는 회원을")
    class Find_Member_is {
        @Test
        @DisplayName("id로 조회할 수 있다.")
        void _success_by_id() {
            // given
            Member member = Member.builder()
                    .email("test@test.com")
                    .password("{encoded}password")
                    .nickname("nickname")
                    .provider(AuthProvider.NONE)
                    .providedId("none")
                    .profile("test_profile_url")
                    .build();
            given(memberRepository.findById(1L)).willReturn(Optional.of(member));

            // when
            MemberQuery.MemberResponse memberResponse =
                    memberQueryHandler.findById(1L).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

            // then
            verify(memberRepository, atMost(1)).findById(1L);
            assertThat(memberResponse)
                    .extracting("password", "email", "nickname", "profile", "provider")
                    .containsExactly("{encoded}password", "test@test.com", "nickname", "test_profile_url", AuthProvider.NONE);
        }

        @Nested
        @DisplayName("자체 회원가입을 한 member의 경우")
        class _in_case_of_self_join {
            @Test
            @DisplayName("email 주소만으로 조회할 수 있다")
            void _success_by_email() {
                // given
                Member member = Member.builder()
                        .email("test@test.com")
                        .password("{encoded}password")
                        .nickname("nickname")
                        .provider(AuthProvider.NONE)
                        .providedId("none")
                        .profile("test_profile_url")
                        .build();
                given(memberRepository.findByProviderAndEmail(AuthProvider.NONE, "test@test.com")).willReturn(Optional.of(member));

                // when
                MemberQuery.MemberRequest memberRequest = new MemberQuery.MemberRequest("test@test.com");
                MemberQuery.MemberResponse memberResponse = memberQueryHandler.find(memberRequest).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

                // then
                verify(memberRepository, atMost(1)).findByProviderAndEmail(AuthProvider.NONE, "test@test.com");
                assertThat(memberResponse)
                        .extracting("password", "email", "nickname", "profile", "provider")
                        .containsExactly("{encoded}password", "test@test.com", "nickname", "test_profile_url", AuthProvider.NONE);
            }

            @Test
            @DisplayName("email 형식의 요청이 아니면 실패한다.")
            void _fail_when_not_email_format() {

            }
        }

        @Nested
        @DisplayName("OAuth2 회원가입을 한 member의 경우")
        class _in_case_of_oauth2_join {
            @Test
            @DisplayName("email 주소만으로 조회할 수 있다")
            void _success_by_email() {
                // given
                Member member = Member.builder()
                        .email("test@test.com")
                        .password("{encoded}password")
                        .nickname("nickname")
                        .provider(AuthProvider.NONE)
                        .providedId("none")
                        .profile("test_profile_url")
                        .build();
                given(memberRepository.findByProviderAndEmail(AuthProvider.NONE, "test@test.com")).willReturn(Optional.of(member));

                // when
                MemberQuery.MemberRequest memberRequest = new MemberQuery.MemberRequest("test@test.com");
                MemberQuery.MemberResponse memberResponse = memberQueryHandler.find(memberRequest).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

                // then
                verify(memberRepository, atMost(1)).findByProviderAndEmail(AuthProvider.NONE, "test@test.com");
                assertThat(memberResponse)
                        .extracting("password", "email", "nickname", "profile", "provider")
                        .containsExactly("{encoded}password", "test@test.com", "nickname", "test_profile_url", AuthProvider.NONE);
            }

            @Test
            @DisplayName("email 형식의 요청이 아니면 실패한다.")
            void _fail_when_not_email_format() {

            }

            @Test
            @DisplayName("지원하는 AuthProvider의 요청이 아니면 실패한다.")
            void _fail_when_not_valid_provider() {

            }
        }
    }
}