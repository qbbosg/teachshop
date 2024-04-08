package plus.suja.teach.teachshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import plus.suja.teach.teachshop.dao.SessionDao;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    SessionDao sessionDao;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor(sessionDao));
    }
}
