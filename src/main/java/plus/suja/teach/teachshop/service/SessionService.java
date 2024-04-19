package plus.suja.teach.teachshop.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.SessionRepository;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Session;
import plus.suja.teach.teachshop.exception.HttpException;
import plus.suja.teach.teachshop.util.HttpRequestUtil;
import plus.suja.teach.teachshop.util.UserContextUtil;

import java.util.UUID;

import static plus.suja.teach.teachshop.config.HttpInterceptor.SESSION_ID;

@Service
public class SessionService {
    private SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session save(Member member) {
        Session session = new Session();
        session.setMember(member);
        session.setCookie(UUID.randomUUID().toString());
        return sessionRepository.save(session);
    }

    public Session online(HttpServletResponse response) {
        Member currentUser = UserContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new HttpException(401, "Unauthorized");
        } else {
            Session session = new Session();
            session.setMember(currentUser);

            response.setStatus(200);
            return session;
        }
    }

    @Transactional
    public void offline(HttpServletRequest request, HttpServletResponse response) {
        HttpRequestUtil.getCookie(request).ifPresent(sessionRepository::deleteByCookie);

        Cookie cookie = new Cookie(SESSION_ID, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setStatus(204);
    }
}
