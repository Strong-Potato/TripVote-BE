package fc.be.app.global.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.service.SpaceTokenService;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpaceInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper;
    private final SpaceTokenService spaceVisitService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        if (response.getStatus() != 200 || !request.getMethod().equalsIgnoreCase("GET")) {
            return;
        }
        if (response instanceof ContentCachingResponseWrapper wrappingResponse) {
            byte[] contentByteArray = wrappingResponse.getContentAsByteArray();
            JsonNode data = objectMapper.readTree(contentByteArray).get("data");
            if (data == null) {
                return;
            }
            SpaceResponse spaceResponse = objectMapper.readValue(data.toString(), SpaceResponse.class);

            boolean inJoinedMember = spaceResponse.members().stream()
                    .anyMatch(memberInfo -> memberInfo.id().equals(getUserPrincipal()));

            if (inJoinedMember) {
                spaceVisitService.saveVisitedSpace(getUserPrincipal(), spaceResponse.id(), spaceResponse.endDate());
                log.info("body" + spaceResponse);
            }
        }
    }

    private static Long getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.id();
    }
}
