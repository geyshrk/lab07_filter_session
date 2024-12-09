package ru.msnigirev.oris.authorisation.session.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PSQLException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.msnigirev.oris.authorisation.session.service.UserService;

import java.io.IOException;

@WebServlet(name = "register", value = "/register")
public class RegisterServlet extends HttpServlet {
    private final static Logger logger = LogManager.getLogger(RegisterServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        UserService userService = (UserService) req.getServletContext().getAttribute("userService");
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        try {
            String username = req.getParameter("username");
            String email = req.getParameter("email");
            String phoneNumber = req.getParameter("phoneNumber");
            String password = bCrypt.encode(req.getParameter("password"));

            userService.addNewUser(username, email, phoneNumber, password);

            logger.info("User {} was registered with password {}, email {}, phone number {}",
                    username, password, email, phoneNumber);
            res.sendRedirect("/login");
        } catch (IOException | RuntimeException e) {
            logger.error(e.getMessage());
            res.sendRedirect("/registerpage");
        }
    }
}