package com.casaleff.addition.interceptor;

import com.casaleff.addition.error.BaseException;
import com.casaleff.addition.error.ErrorType;
import com.casaleff.addition.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    private final SessionService sessionService; // where you keep checkSession()

    public SessionInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String sessionId = request.getHeader("X-Session-Id");
        if (sessionId == null || sessionId.isBlank()) {
            throw new BaseException(ErrorType.UNAUTHORIZED, "Missing session ID");
        }

        try {
            UUID uuid = UUID.fromString(sessionId);
            sessionService.checkSession(uuid); // may throw expired/invalid session
        } catch (IllegalArgumentException e) {
            throw new BaseException(ErrorType.UNAUTHORIZED, "Invalid session ID format");
        }
        return true; // allow request to proceed
    }
}