package com.casaleff.addition.service;

import com.casaleff.addition.error.BaseException;
import com.casaleff.addition.error.ErrorType;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class SessionService {

    private record Session(UUID uuid, LocalDateTime start) {}

    String passwordBase64 = "cGFzc3dvcmQ=";

    private List<Session> sessions = new ArrayList<Session>();

    public UUID createSession(String password){
        if (password == null)
            throw new BaseException(ErrorType.BAD_REQUEST, "password is null");
        else if(password.isEmpty())
            throw new BaseException(ErrorType.BAD_REQUEST, "password is empty");
        else if (!passwordBase64.equals(Base64.getEncoder().encodeToString(password.getBytes()))) {
            throw new BaseException(ErrorType.UNAUTHORIZED, "password is not match");
        }
        UUID uuid = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        sessions.add(new Session(uuid, start));
        return uuid;
    }


    public void checkSession(UUID uuid) {
        if (sessions.isEmpty())
            throw new BaseException(ErrorType.BAD_REQUEST, "session is empty");

        boolean result = false;
        Session foundSession = null;

        for (Session session : sessions) {
            if (session.uuid.equals(uuid)) {
                result = true;
                foundSession = session;
                break;
            }
        }
        if (result) {
            // check if session is older than 24 hours
            if (Duration.between(foundSession.start(), LocalDateTime.now()).toHours() >= 24) {
                sessions.remove(foundSession); // remove expired session
                throw new BaseException(ErrorType.UNAUTHORIZED, "session expired");
            }
            return;
        }
        throw new BaseException(ErrorType.UNAUTHORIZED, "session id not found");
    }

}
