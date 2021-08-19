package ua.yelisieiev.web.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.yelisieiev.service.SecurityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.yelisieiev.service.ServiceLocator;

import java.io.IOException;

public class SecurityFilter extends HttpFilter {
    public final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SecurityService securityService;

    public SecurityFilter() {
        securityService = ServiceLocator.getService(SecurityService.class);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String token = null;

        final Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth-token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null || !securityService.isTokenValid(token)) {
            res.sendRedirect("/login");
        } else {
            chain.doFilter(req, res);
        }
    }
}
