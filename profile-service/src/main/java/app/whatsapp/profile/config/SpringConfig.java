package app.whatsapp.profile.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfig.class);

    @Autowired
    private HandlerInterceptor baseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LOGGER.trace("Registering interceptor : {} ", baseInterceptor.getClass());
        registry.addInterceptor(baseInterceptor);
    }
}
