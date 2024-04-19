package plus.suja.teach.teachshop.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import plus.suja.teach.teachshop.dao.SessionRepository;
import plus.suja.teach.teachshop.entity.Session;
import plus.suja.teach.teachshop.util.HttpRequestUtil;
import plus.suja.teach.teachshop.util.UserContextUtil;

public class HttpInterceptor implements HandlerInterceptor {
    public static final String SESSION_ID = "_SESSION_ID";
    SessionRepository sessionRepository;

    public HttpInterceptor(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    private void setCurrentUserIfPresentCookie(HttpServletRequest request) {
        HttpRequestUtil.getCookie(request)
                .flatMap(sessionRepository::findByCookie)
                .map(Session::getMember)
                .ifPresent(UserContextUtil::setCurrentUser);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setCurrentUserIfPresentCookie(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextUtil.setCurrentUser(null);
    }
}
