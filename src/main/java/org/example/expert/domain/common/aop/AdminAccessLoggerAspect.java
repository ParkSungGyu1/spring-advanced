package org.example.expert.domain.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminAccessLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(AdminAccessLoggerAspect.class);

    private final HttpServletRequest request; // HttpServletRequest 주입

    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public void adminApi() {}

    @Around("adminApi()")
    public Object logAdminAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        // 요청 정보
        String requestUrl = request.getRequestURI();

        // 요청 본문 읽기
        String requestBody = "";
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof String) {
                requestBody = (String) arg; // 예를 들어 JSON 문자열로 가정
            } else if (arg instanceof UserRoleChangeRequest) {
                requestBody = ((UserRoleChangeRequest) arg).getRole(); // UserRoleChangeRequest의 toString() 메서드 활용
            }
            // 필요에 따라 다른 요청 본문 타입 처리 추가
        }

        // 메서드 실행
        Object result = joinPoint.proceed();

        // 응답 본문을 로그에 기록 (반환값을 사용)
        String responseBody = result != null ? result.toString() : "null"; // 응답이 null인 경우 처리

        // 로그 기록
        Long userId = (Long) request.getAttribute("userId"); // JWT에서 추출한 사용자 ID
        logger.info("Admin Access Log: UserID: {}, RequestTime: {}, RequestURL: {}, RequestBody: {}, Response: {}",
                userId, System.currentTimeMillis(), requestUrl, requestBody, responseBody);

        return result; // 결과 반환
    }
}

