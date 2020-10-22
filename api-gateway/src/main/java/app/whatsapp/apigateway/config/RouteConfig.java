package app.whatsapp.apigateway.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.DedupeResponseHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.function.Function;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("profile-service", r -> r.path("/profile-service/**").filters(dedupeResponseHeaders()).uri("lb://whatsapp-profile-service"))
                .route("gateway-service", r -> r.path("/gateway-service/**").filters(dedupeResponseHeaders()).uri("lb:ws://whatsapp-gateway-service"))
                .route("sessions-service", r -> r.path("/sessions-service/**").filters(dedupeResponseHeaders()).uri("lb://whatsapp-sessions-service"))
                .build();
    }

    private Function<GatewayFilterSpec, UriSpec> dedupeResponseHeaders() {
        String strategy = DedupeResponseHeaderGatewayFilterFactory.Strategy.RETAIN_LAST.name();
        return f -> f.dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, strategy)
                .dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, strategy)
                .dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, strategy)
                .dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, strategy);
    }

    @Bean
    public GlobalFilter globalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            String origin = exchange.getRequest().getHeaders().getOrigin();
            if (StringUtils.isNotBlank(origin)) {
                exchange.getResponse().getHeaders().setAccessControlAllowOrigin(origin);
            }else{
                exchange.getResponse().getHeaders().setAccessControlAllowOrigin(CorsConfiguration.ALL);
            }
        }));
    }

    @Bean
    public CorsConfiguration corsConfiguration(RoutePredicateHandlerMapping routePredicateHandlerMapping) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList(CorsConfiguration.ALL));
        corsConfiguration.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
        corsConfiguration.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(true);
        routePredicateHandlerMapping.setCorsConfigurations(Collections.singletonMap("/**", corsConfiguration));
        return corsConfiguration;
    }

}
