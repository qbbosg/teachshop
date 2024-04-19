package plus.suja.teach.teachshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import plus.suja.teach.teachshop.dao.SessionRepository;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    SessionRepository sessionRepository;

    @Autowired
    public WebConfig(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor(sessionRepository));
    }
}
