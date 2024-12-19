package itcast.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "Auth Log")
public class AuthLoggingAspect {

    @Before("execution(* itcast.auth.controller.AuthController.getAccessToken(..))")
    public void logBeforeGetAccessToken(final JoinPoint joinPoint) {
        final String code = (String) joinPoint.getArgs()[0];
        log.info("카카오 토큰 발급 메서드 호출, 인증 코드: {}", code);
    }

    @AfterThrowing(pointcut = "execution(* itcast.auth.controller.AuthController.*(..))", throwing = "ex")
    public void logAuthException(final JoinPoint joinPoint, final Throwable ex) {
        handleErrorLogging(joinPoint, ex);
    }

    private void handleErrorLogging(final JoinPoint joinPoint, final Throwable ex) {
        log.error("예외 발생! 요청 메서드: {}, 에러 메시지: {}", joinPoint.getSignature(), ex.getMessage());
    }
}
