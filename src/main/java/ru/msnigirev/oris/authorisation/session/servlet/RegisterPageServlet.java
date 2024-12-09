package ru.msnigirev.oris.authorisation.session.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "registerpage", value = "/registerpage")
public class RegisterPageServlet extends HttpServlet {
    private final static Logger logger = LogManager.getLogger(RegisterPageServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("static/register.html")){
            is.transferTo(res.getOutputStream());
            res.setContentType("text/html");
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
