package com.official.memento.global.config;

import com.official.memento.global.resolver.AuthArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthorizationConfig implements WebMvcConfigurer {

    private final AuthArgumentResolver authArgumentResolver;

    public AuthorizationConfig(AuthArgumentResolver authArgumentResolver) {
        this.authArgumentResolver = authArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }
}
