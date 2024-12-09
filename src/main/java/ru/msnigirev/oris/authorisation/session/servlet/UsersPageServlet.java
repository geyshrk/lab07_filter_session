package ru.msnigirev.oris.authorisation.session.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.msnigirev.oris.authorisation.session.entity.User;
import ru.msnigirev.oris.authorisation.session.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "users", value = "/users")
public class UsersPageServlet extends HttpServlet {
    private final static Logger logger = LogManager.getLogger(UsersPageServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();

        UserService userService = (UserService) servletContext.getAttribute("userService");
        List<User> users = userService.getAllUsers();
        req.setAttribute("users", users);
        try {
            req.getRequestDispatcher("/views/users.jsp").forward(req, res);
        } catch (IOException | ServletException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
