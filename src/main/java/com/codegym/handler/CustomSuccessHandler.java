package com.codegym.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request,
                          HttpServletResponse response,
                          Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            System.out.println("can't redirect");
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {
        String url;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<>();

        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }

        if (isTeacher(roles)) {
            url = "/teacher/home";
        } else if (isAdmin(roles)) {
            url = "/admin/home";
        } else if (isStudent(roles)) {
            url = "/students/home";
        } else {
            url = "/accessDenied";
        }

        return url;
    }

    private boolean isStudent(List<String> roles) {
        return roles.contains("ROLE_STUDENT");
    }

    private boolean isAdmin(List<String> roles) {
        return roles.contains("ROLE_ADMIN");
    }

    private boolean isTeacher(List<String> roles) {
        return roles.contains("ROLE_TEACHER");
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}