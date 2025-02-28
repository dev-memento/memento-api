package com.official.memento.global.resolver;

import com.official.memento.auth.service.JwtUtil;
import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    private static final String EMPTY = "";
    private final JwtUtil jwtUtil;
    @Value("${ADMIN.TOKEN_PREFIX}")
    private String AUTHORIZATION_HEADER_ADMIN_PREFIX;

    public AuthArgumentResolver(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authorization.class);
    }

    @Override
    public AuthorizationUser resolveArgument(MethodParameter parameter,
                                             ModelAndViewContainer mavContainer,
                                             NativeWebRequest webRequest,
                                             WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token = parseToken(authorizationHeaderValue);
        Long memberId = validateToken(token);
        return new AuthorizationUser(memberId);
    }

    private Long validateToken(String token) {
        if (token.startsWith(AUTHORIZATION_HEADER_ADMIN_PREFIX)) {
            String adminId = token.substring(AUTHORIZATION_HEADER_ADMIN_PREFIX.length());
            return Long.parseLong(adminId);
        }
        if (jwtUtil.validateToken(token)) {
            return Long.parseLong(jwtUtil.getUserIdFromToken(token));
        } else {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }


    private String parseToken(String token) {
        if (token == null || !token.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        return token.replace(AUTHORIZATION_HEADER_PREFIX, EMPTY);
    }
}
