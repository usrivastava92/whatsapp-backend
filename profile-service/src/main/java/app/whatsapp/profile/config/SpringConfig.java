package app.whatsapp.profile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class SpringConfig implements WebMvcConfigurer {

    private HandlerInterceptor baseInterceptor;

    public SpringConfig(HandlerInterceptor baseInterceptor) {
        this.baseInterceptor = baseInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.trace("Registering interceptor : {} ", baseInterceptor.getClass());
        registry.addInterceptor(baseInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

}
