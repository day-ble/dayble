package itcast.blog.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j(topic = "블로그 크롤링 AOP")
@Aspect
@Component
public class BlogCrawlAspect {

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object schedulerExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        log.info("실행 메서드: {} - 실행 시간: {}ms", joinPoint.getSignature(),  (endTime - startTime));
        return result;
    }
}
