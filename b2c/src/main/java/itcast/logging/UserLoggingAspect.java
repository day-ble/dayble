package itcast.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;

@Aspect
@Component
@Slf4j(topic = "User Log")
public class UserLoggingAspect {

    @Before("execution(* itcast.user.controller.UserController.createProfile(..))")
    public void logBeforeCreateProfile(final JoinPoint joinPoint) {
        final Long userId = (Long) joinPoint.getArgs()[0];
        log.info("회원 정보 작성 메서드 호출, 사용자 ID: {}", userId);
    }

    @Before("execution(* itcast.user.controller.UserController.updateProfile(..))")
    public void logBeforeUpdateProfile(final JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        log.info("회원 정보 수정 메서드 호출, 사용자 ID: {}", userId);
    }

    @Before("execution(* itcast.user.controller.UserController.deleteUser(..))")
    public void logBeforeDeleteUser(final JoinPoint joinPoint) {
        final Long userId = (Long) joinPoint.getArgs()[0];
        log.info("회원 탈퇴 메서드 호출, 사용자 ID: {}", userId);
    }

    @AfterThrowing(pointcut = "execution(* itcast.user.controller.UserController.*(..))", throwing = "ex")
    public void logUserException(final JoinPoint joinPoint, final Throwable ex) {
        handleErrorLogging(joinPoint, ex);
    }

    private void handleErrorLogging(final JoinPoint joinPoint, final Throwable ex) {
        if (ex instanceof ItCastApplicationException itCastEx) {
            final ErrorCodes errorCode = itCastEx.getErrorCodes();
            final Long userId = (Long) joinPoint.getArgs()[0];
            log.error("회원 관련 예외 발생! 사용자 ID: {}, 요청 메서드: {}, 에러 코드: {}, 에러 메시지: {}, 상태: {}",
                    userId,
                    joinPoint.getSignature(),
                    errorCode.getCode(),
                    errorCode.getMessage(),
                    errorCode.getStatus());
        }
    }
}
