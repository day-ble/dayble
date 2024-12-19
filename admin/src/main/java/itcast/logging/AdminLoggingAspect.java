package itcast.logging;

import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "Admin Log")
public class AdminLoggingAspect {

    @Before("execution(* itcast.controller.AdminBlogController.*(..))")
    public void logBeforeBlogOperations(final JoinPoint joinPoint) {
        logMethodInvocation("블로그", joinPoint);
    }

    @Before("execution(* itcast.controller.AdminNewsController.*(..))")
    public void logBeforeNewsOperations(final JoinPoint joinPoint) {
        logMethodInvocation("뉴스", joinPoint);
    }

    private void logMethodInvocation(final String operation, final JoinPoint joinPoint) {
        log.info("{} 관련 메서드 호출, 관리자 ID: {}, 요청 메서드: {}", operation, MDC.get("userId"), joinPoint.getSignature());
    }

    @AfterThrowing(pointcut = "execution(* itcast.controller.Admin*Controller.*(..))", throwing = "ex")
    public void logException(final JoinPoint joinPoint, final Throwable ex) {
        handleErrorLogging(ex, joinPoint);
    }

    private void handleErrorLogging(final Throwable ex, final JoinPoint joinPoint) {
        if (ex instanceof ItCastApplicationException itCastEx) {
            final ErrorCodes errorCode = itCastEx.getErrorCodes();
            log.error("예외 발생! 관리자 ID: {}, 요청 메서드: {}, 에러 코드: {}, 에러 메시지: {} 상태: {}",
                    MDC.get("userId"), joinPoint.getSignature(), errorCode.getCode(), errorCode.getMessage(), errorCode.getStatus());
        }
    }
}
