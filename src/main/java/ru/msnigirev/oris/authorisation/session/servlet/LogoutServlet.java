package ru.msnigirev.oris.authorisation.session.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.msnigirev.oris.authorisation.session.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebServlet(value = "/logout", name = "logout")
public class LogoutServlet extends HttpServlet {
    private final static Logger logger = LogManager.getLogger(LogoutServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        logger.info("User is logging out");
        UserService userService = (UserService) req.getServletContext().getAttribute("userService");
        try {
            String token = (String) req.getSession().getAttribute("csrf_token");
            req.getSession().removeAttribute("csrf_token");
            Optional<Cookie> csrfToken =
                    Arrays.stream(req.getCookies())
                            .filter(cookie -> cookie.getName().equals("csrf_token"))
                            .findFirst();

            if (csrfToken.isPresent()) {
                Cookie cookie = new Cookie("csrf_token", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                res.addCookie(cookie);
            }

            userService.deleteSessionId(token);

            logger.info("Logging out finished successfully. Redirecting to login page");
            res.sendRedirect("/login");
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
