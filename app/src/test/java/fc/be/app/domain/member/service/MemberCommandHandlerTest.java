package fc.be.app.domain.member.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.member.vo.AuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class MemberCommandHandlerTest {
    @InjectMocks
    private MemberCommandHandler memberCommandHandler;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("자체 회원가입을 수행한 유저를")
    class Register_Member_is {
        @Test
        @DisplayName("등록할 수 있다.")
        void _success_by_email() {
            // given
            Member member = Member.builder()
                    .email("test@test.com")
                    .password("password")
                    .nickname("nickname")
                    .provider(AuthProvider.NONE)
                    .providedId("none")
                    .profile("test_profile_url")
                    .build();
            given(memberRepository.existsByProviderAndEmail(AuthProvider.NONE, "test@test.com")).willReturn(false);
            given(passwordEncoder.encode("password")).willReturn(BCrypt.hashpw("password", BCrypt.gensalt()));

            // when
            MemberCommand.MemberRegisterRequest memberRegisterRequest =
                    new MemberCommand.MemberRegisterRequest("test@test.com", "password", "nickname", "test_profile_url");
            memberCommandHandler.register(memberRegisterRequest);

            // then
            verify(memberRepository, atLeastOnce()).save(member);
        }

        @Test
        @DisplayName("기존의 유저면 저장하지 않는다.")
        void _success_when_already_member() {
            // given
            Member member = Member.builder()
                    .email("test@test.com")
                    .password("password")
                    .nickname("nickname")
                    .provider(AuthProvider.NONE)
                    .providedId("none")
                    .profile("test_profile_url")
                    .build();
            given(memberRepository.existsByProviderAndEmail(AuthProvider.NONE, "test@test.com")).willReturn(true);

            // when
            MemberCommand.MemberRegisterRequest memberRegisterRequest =
                    new MemberCommand.MemberRegisterRequest("test@test.com", "password", "nickname", "test_profile_url");
            memberCommandHandler.register(memberRegisterRequest);

            // then
            verify(memberRepository, never()).save(member);
        }
    }

    @Nested
    @DisplayName("OAuth2 회원가입을 수행한 유저를")
    class Register_Provider_Member_is {
        @Test
        @DisplayName("등록할 수 있다.")
        void _success_by_email() {
            // given
            Member member = Member.builder()
                    .email("test@test.com")
                    .nickname("kakao_nickname")
                    .provider(AuthProvider.KAKAO)
                    .providedId("kakao_provier_id")
                    .profile("kakao_profile_url")
                    .build();
            given(memberRepository.existsByProviderAndEmail(AuthProvider.KAKAO, "test@test.com")).willReturn(false);

            // when
            MemberCommand.ProviderMemberRegisterRequest providerMemberRegisterRequest =
                    new MemberCommand.ProviderMemberRegisterRequest("test@test.com", "nickname", "kakao_profile_url", AuthProvider.KAKAO, "kakao_provier_id");
            memberCommandHandler.register(providerMemberRegisterRequest);

            // then
            verify(memberRepository, atLeastOnce()).save(member);
        }

        @Test
        @DisplayName("기존의 유저면 저장하지 않는다.")
        void _success_when_already_member() {
            // given
            Member member = Member.builder()
                    .email("test@test.com")
                    .nickname("kakao_nickname")
                    .provider(AuthProvider.KAKAO)
                    .providedId("kakao_provier_id")
                    .profile("kakao_profile_url")
                    .build();
            given(memberRepository.existsByProviderAndEmail(AuthProvider.KAKAO, "test@test.com")).willReturn(true);

            // when
            MemberCommand.ProviderMemberRegisterRequest providerMemberRegisterRequest =
                    new MemberCommand.ProviderMemberRegisterRequest("test@test.com", "nickname", "kakao_profile_url", AuthProvider.KAKAO, "kakao_provier_id");
            memberCommandHandler.register(providerMemberRegisterRequest);

            // then
            verify(memberRepository, never()).save(member);
        }
    }
}
