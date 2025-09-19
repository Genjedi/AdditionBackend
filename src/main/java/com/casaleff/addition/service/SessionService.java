package com.casaleff.addition.service;

import com.casaleff.addition.error.BaseException;
import com.casaleff.addition.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SessionService {

    private static final String EXPECTED_PASSWORD_BASE64 = "cGFzc3dvcmQ="; // base64 for "password"
    private static final long SESSION_TIMEOUT_HOURS = 24;

    private record Session(UUID uuid, LocalDateTime start) {}

    private final List<Session> sessions = new ArrayList<>();

    /**
     * Creates a new session if the provided password matches the expected one.
     */
    public UUID createSession(String password) {
        log.info("Attempting to create session");

        if (password == null) {
            log.error("Password is null");
            throw new BaseException(ErrorType.BAD_REQUEST, "Password is null");
        }

        if (password.isEmpty()) {
            log.error("Password is empty");
            throw new BaseException(ErrorType.BAD_REQUEST, "Password is empty");
        }

        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        if (!EXPECTED_PASSWORD_BASE64.equals(encodedPassword)) {
            log.warn("Password mismatch");
            throw new BaseException(ErrorType.UNAUTHORIZED, "Password does not match");
        }

        UUID uuid = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        sessions.add(new Session(uuid, start));

        log.info("Session created with UUID: {}", uuid);
        return uuid;
    }

    /**
     * Checks if the session is valid and not expired.
     */
    public void checkSession(UUID uuid) {
        log.info("Checking session with UUID: {}", uuid);

        if (sessions.isEmpty()) {
            log.warn("Session list is empty");
            throw new BaseException(ErrorType.BAD_REQUEST, "Session list is empty");
        }

        Session foundSession = null;
        for (Session session : sessions) {
            if (session.uuid.equals(uuid)) {
                foundSession = session;
                break;
            }
        }

        if (foundSession == null) {
            log.warn("Session ID not found: {}", uuid);
            throw new BaseException(ErrorType.UNAUTHORIZED, "Session ID not found");
        }

        long hoursElapsed = Duration.between(foundSession.start(), LocalDateTime.now()).toHours();
        if (hoursElapsed >= SESSION_TIMEOUT_HOURS) {
            sessions.remove(foundSession);
            log.warn("Session expired for UUID: {}", uuid);
            throw new BaseException(ErrorType.UNAUTHORIZED, "Session expired");
        }

        log.info("Session is valid for UUID: {}", uuid);
    }
}
