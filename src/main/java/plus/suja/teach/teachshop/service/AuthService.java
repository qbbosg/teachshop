package plus.suja.teach.teachshop.service;

import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.config.Interceptor;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Session;
import plus.suja.teach.teachshop.exception.HttpException;

@Service
public class AuthService {
    public Session getSession() {
        Member currentUser = Interceptor.UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new HttpException(401, "Unauthorized");
        } else {
            Session session = new Session();
            session.setMember(currentUser);
            return session;
        }
    }
}
