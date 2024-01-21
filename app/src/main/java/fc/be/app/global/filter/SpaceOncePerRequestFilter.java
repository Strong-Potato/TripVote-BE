package fc.be.app.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class SpaceOncePerRequestFilter extends OncePerRequestFilter {
    private final List<Pattern> whiteListPatterns = new ArrayList<>();

    public SpaceOncePerRequestFilter() {
        whiteListPatterns.add(Pattern.compile("/spaces/\\d+"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!isInWhiteList(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        // 응답은 ContentCachingResponseWrapper로 감싸 interceptor에서 ResponseBody 값을 처리할 수 있게 함.
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);

        // 캐스팅한 객체로 Filter 수행.
        filterChain.doFilter(request, wrappingResponse);
        wrappingResponse.copyBodyToResponse();
    }

    private boolean isInWhiteList(String url) {
        for (Pattern pattern : whiteListPatterns) {
            if (pattern.matcher(url).matches()) {
                return true;
            }
        }
        return false;
    }
}
