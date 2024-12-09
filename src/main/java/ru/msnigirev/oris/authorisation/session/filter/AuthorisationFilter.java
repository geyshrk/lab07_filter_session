package ru.msnigirev.oris.authorisation.session.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.msnigirev.oris.authorisation.session.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebFilter("/*")
public class AuthorisationFilter extends HttpFilter {
    private final static Logger logger = LogManager.getLogger(AuthorisationFilter.class);
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (req.getServletPath().startsWith("/login") || req.getServletPath().startsWith("/register") ||
                req.getServletPath().startsWith("/usercheck")) {
            logger.debug("User is logging/registering");
            chain.doFilter(req, res);
        } else {
            logger.debug("User is trying to get resource");
            UserService userService = (UserService) req.getServletContext().getAttribute("userService");
            Cookie[] cookies = req.getCookies();

            logger.debug("Server is trying to find token in session");
            Object expectedToken = req.getSession().getAttribute("csrf_token");

            if (cookies == null) {
                logger.debug("User has no cookie. Redirecting to login page");
                res.sendRedirect("/login");
            } else {
                Optional<Cookie> csrfToken = Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals("csrf_token"))
                        .findFirst();
                if (csrfToken.isPresent()) {
                    String actualToken = csrfToken.get().getValue();
                    logger.debug("User has cookie-token");
                    if (expectedToken != null) {
                        if (actualToken.equals(expectedToken)) {
                            logger.debug("Tokens match. Passing user to resource");
                            chain.doFilter(req, res);
                        } else {
                            res.sendRedirect("Cookie-token is incorrect. Deleting cookie");
                            deleteCookie(csrfToken, res);
                            res.sendRedirect("/login");
                        }
                    } else {
                        logger.debug("No token in session");
                        if (userService.sessionIdExists(actualToken)) {
                            req.getSession().setAttribute("csrf_token", actualToken);
                            logger.debug("Token exists, but session don't consist it. " +
                                    "Adding token to session and passing user to resource");
                            chain.doFilter(req, res);
                        } else {
                            logger.debug("Token hasn't been saved in DB. Redirecting to login");
                            res.sendRedirect("/login");
                        }
                    }
                } else {
                    logger.debug("User has no cookie-token. Redirecting to login page");
                    res.sendRedirect("/login");
                }
            }
        }
    }
    private void deleteCookie(Optional<Cookie> csrfToken, HttpServletResponse res){
        Cookie cookie = csrfToken.get();
        cookie.setPath("/");
        cookie.setValue("");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }
}
