package com.dayble.blog.global.interceptor;

import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.global.exception.ErrorCodes;
import com.dayble.blog.user.domain.User;
import com.dayble.blog.user.domain.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthCheckInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

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
                    throw new DaybleApplicationException(ErrorCodes.UNAUTHORIZED_ACCESS);
                }

                Long userId = jwtUtil.getUserIdFromToken(token);
                Optional<User> user = userRepository.findById(userId);
                if (user.isEmpty()) {
                    throw new DaybleApplicationException(ErrorCodes.USER_NOT_FOUND);
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
