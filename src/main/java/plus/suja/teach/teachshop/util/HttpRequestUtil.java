package plus.suja.teach.teachshop.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.stream.Stream;

import static plus.suja.teach.teachshop.config.HttpInterceptor.SESSION_ID;

public class HttpRequestUtil {
    public static Optional<String> getCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        Cookie[] cookies = request.getCookies();
        return Stream.of(cookies).filter(cookie -> cookie.getName().equals(SESSION_ID))
                .map(Cookie::getValue)
                .findFirst();
    }
}
