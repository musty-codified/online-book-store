package com.mustycodified.online_book_store.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class UrlAccessDecisionManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final Logger log = LoggerFactory.getLogger(UrlAccessDecisionManager.class);
    private final PermissionConfig permissionConfig;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        List<String> whitelistedUrls = Arrays.asList("/swagger-ui/**", "/v3/api-docs/**", "/auth/**", "/h2-console/**", "/configuration/**",
                "/webjars/**", "/users");
        String requestedUrl = object.getRequest().getRequestURI().substring(contextPath.length()).toLowerCase();
        log.info("Requested URL: {}", requestedUrl);

        String requestedMethod = object.getRequest().getMethod();
        log.info("Requested method: {}", requestedMethod);
        if (whitelistedUrls.stream().anyMatch(e -> pathMatcher.match(e, requestedUrl))) {
            return new AuthorizationDecision(true);
        }
        boolean match = authentication.get().getAuthorities().parallelStream()
                .map(GrantedAuthority::getAuthority)
                .map(String::toLowerCase)
                .anyMatch(permission -> matchesPermission(permission, requestedUrl, requestedMethod));

        if (match) {
            return new AuthorizationDecision(true);

        } else {
            String message = "Access denied. No matching authority in user found for the requested URL and method.";
            log.debug("{} User: {}, URL: {}, Method: {}", message, authentication.get().getName(), requestedUrl, requestedMethod);
            return new AuthorizationDecisionHandler(false, message);
        }

    }

    private boolean matchesPermission(String permission, String requestedUrl, String requestMethod) {
        System.out.println(permission);
        return permissionConfig.getPermissions().stream()
                .filter(e -> e.getPermission().equals(permission))
                .anyMatch(mapping -> mapping.getMethods().contains(requestMethod) &&
                        mapping.getPatterns().stream().anyMatch(pattern -> pathMatcher.match(pattern, requestedUrl)));
    }
}
