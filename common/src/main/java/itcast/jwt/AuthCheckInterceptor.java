package itcast.jwt;

import java.util.Optional;

import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import itcast.domain.user.User;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.jwt.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthCheckInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;

            if (handlerMethod.hasMethodAnnotation(CheckAuth.class)) {
                String token = null;
                // 쿠키에서 토큰을 찾을 수 없는 경우, Authorization 헤더에서 토큰 확인
                String cookieHeader = request.getHeader("Cookie");
                if (cookieHeader != null && cookieHeader.contains("Authorization=")) {
                    String[] cookies = cookieHeader.split(";");
                    for (String cookie : cookies) {
                        if (cookie.trim().startsWith("Authorization=")) {
                            token = cookie.split("=")[1];
                            break;
                        }
                    }
                }
                if (token == null) {
                    token = request.getHeader("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }
                }
                if (token == null) {
                    throw new ItCastApplicationException(ErrorCodes.UNAUTHORIZED_ACCESS);
                }

                Long userId = jwtUtil.getUserIdFromToken(token);
                Optional<User> user = userRepository.findById(userId);
                if (user.isEmpty()) {
                    throw new ItCastApplicationException(ErrorCodes. USER_NOT_FOUND);
                }
                request.setAttribute("userId", userId);

                String requestId = UUID.randomUUID().toString();
                MDC.put("request_id", requestId);
                MDC.put("controller", request.getRequestURI());
                MDC.put("method", request.getMethod());
                MDC.put("userId", userId.toString());
                return true;
            }
        }
        return true;
    }
}
