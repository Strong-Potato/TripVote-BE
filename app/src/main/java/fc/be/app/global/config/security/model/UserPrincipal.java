package fc.be.app.global.config.security.model;

import fc.be.app.domain.member.vo.AuthProvider;

/**
 * <h2>Controller에서 접속 중인 유저의 정보를 받아오는 방법</h2>
 *
 * <h3>
 * Sample code
 * </h3>
 *
 * <pre>
 *     {@code
 *      @RestController
 *      public class MemberController {
 *          @Getmapping("/")
 *          public ResponseEntity<ApiResponse> getCurrentUser(@AuthenticatedPrincipal UserPrincipal userPrincipal) {
 *              Long id = userPrincipal.id();
 *              String email = userPrincipal.email();
 *
 *          }
 *      }
 *      }
 * </pre>
 *
 * @author donghar
 * @see org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver
 * @see org.springframework.security.core.annotation.AuthenticationPrincipal
 */
public record UserPrincipal(
        Long id,
        String email,
        AuthProvider authProvider
) {
}
