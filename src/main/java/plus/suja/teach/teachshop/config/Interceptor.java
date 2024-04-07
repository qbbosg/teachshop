package plus.suja.teach.teachshop.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import plus.suja.teach.teachshop.dao.SessionDao;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Session;

import java.util.stream.Stream;

@Configuration
public class Interceptor {
    @Autowired
    static SessionDao sessionDao;
    public static class UserContext {
        private static ThreadLocal<Member> currentUser = new ThreadLocal<>();

        public static Member getCurrentUser() {
            return currentUser.get();
        }

        public static void setCurrentUser(Member currentUser) {
            UserContext.currentUser.set(currentUser);
        }
    }

    public static class MemberInterceptor implements HandlerInterceptor {
        public static final String SESSION_ID = "_SESSION_ID";

        private static void getCookie(HttpServletRequest request) {
            Cookie[] cookies = request.getCookies();
            Stream.of(cookies).filter(cookie -> cookie.getName().equals(SESSION_ID))
                    .map(Cookie::getValue)
                    .findFirst()
                    .flatMap(sessionDao::findByCookie)
                    .map(Session::getMember)
                    .ifPresent(UserContext::setCurrentUser);
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            getCookie(request);

            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            UserContext.setCurrentUser(null);
        }
    }
}
