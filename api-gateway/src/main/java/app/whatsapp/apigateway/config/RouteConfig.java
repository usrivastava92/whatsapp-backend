package app.whatsapp.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
        .route("profile-service", r -> r.path("/profile-service/**").uri("lb://whatsapp-profile-service"))
        .route("gateway-service", r -> r.path("/gateway-service/**").uri("lb://whatsapp-gateway-service"))
        .route("sessions-service", r -> r.path("/sessions-service/**").uri("lb://whatsapp-sessions-service"))
        .build();
    }

}
