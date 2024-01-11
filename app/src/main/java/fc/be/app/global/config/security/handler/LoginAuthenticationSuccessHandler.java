package fc.be.app.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.app.global.http.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

// TODO: 세션은 토큰(Access-token, Refresh-token)으로 유지하기로 한다.
// TODO: code를 가지고 여행스페이스에 접근하는 요청의 경우,
//  Session(Redis)를 발급해서 그 안에 초대받은 여행스페이스 정보를 가지고 있을 것이고,
//  인증이 성공했을 때, SSE로 발송해주어야 한다.
// TODO: AbstractAuthenticationTargetUrlRequestHandler, RedirectAttributes, RequestMappingHandlerAdapter.handleInternal(),
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), ApiResponse.ok());
    }
}
