package ua.yelisieiev.web.servlet;

import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlet.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.yelisieiev.entity.Product;
import ua.yelisieiev.service.ProductServiceException;
import ua.yelisieiev.service.SecurityService;
import ua.yelisieiev.web.PageWriter;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.*;

public class LoginServlet extends HttpServlet {
    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SecurityService securityService;
    private final PageWriter pageWriter = new PageWriter();

    public LoginServlet(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Writer writer = resp.getWriter();
        try {
            pageWriter.writePage("/login.html", null, writer);
        } catch (TemplateNotFoundException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Template not found: " + e.getMessage());
        } catch (TemplateException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Can't apply a template: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            if (String.valueOf(login).isEmpty() || String.valueOf(password).isEmpty()) {
                throw new ServletException("Both login and password must be submitted");
            }
            if (!securityService.isLoginPassValid(login, password)) {
                resp.sendError(SC_FORBIDDEN);
                return;
            }
            String token = securityService.createToken(login, req.getSession().getId());
            resp.addCookie(new Cookie("auth-token", token));
            resp.sendRedirect("/products");
        } catch (ServletException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        }
    }
}
