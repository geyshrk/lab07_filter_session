package ru.msnigirev.oris.authorisation.session.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.msnigirev.oris.authorisation.session.entity.User;
import ru.msnigirev.oris.authorisation.session.service.UserService;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "usercheck", value = "/usercheck")
public class UserCheckServlet extends HttpServlet {
    private final static Logger logger = LogManager.getLogger(UserCheckServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        UserService userService = (UserService) req.getServletContext().getAttribute("userService");
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        try{
            String password = req.getParameter("password");
            String username = req.getParameter("username");
            logger.info("User is trying to login using username {} and password {}", username, password);

            User user = userService.getUser(username);
            if (user != null && bCrypt.matches(password, user.getPassword())) {
                logger.info("Successful. Generating token");
                String csrfToken = UUID.randomUUID().toString();
                String rememberMe = req.getParameter("rememberMe");

                if (rememberMe != null) {
                    res.addCookie(new Cookie("csrf_token", csrfToken));
                    userService.addSessionId(csrfToken, username);
                } else {
                    Cookie sessionCookie = new Cookie("csrf_token", csrfToken);
                    sessionCookie.setMaxAge(-1);
                    res.addCookie(sessionCookie);
                }
                req.getSession().setAttribute("csrf_token", csrfToken);
                logger.info("User {} entered with password {}",
                        username, password);

                res.sendRedirect("/users");
            } else {
                res.sendRedirect("/login");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
