package ru.msnigirev.oris.authorisation.session.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.msnigirev.oris.authorisation.session.config.DataSourceConfiguration;
import ru.msnigirev.oris.authorisation.session.repository.impl.UserRepositoryImpl;
import ru.msnigirev.oris.authorisation.session.repository.interfaces.UserRepository;
import ru.msnigirev.oris.authorisation.session.repository.mapper.UserRowMapper;
import ru.msnigirev.oris.authorisation.session.service.UserService;
import ru.msnigirev.oris.authorisation.session.service.UserServiceImpl;

import java.io.IOException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {
    private final static Logger logger = LogManager.getLogger(ContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("application.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        DataSourceConfiguration configuration =
                new DataSourceConfiguration(properties);

        logger.debug("Data base configured " + properties);

        UserRepository userRepository =
                new UserRepositoryImpl(configuration.hikariDataSource(), new UserRowMapper());

        UserService userService = new UserServiceImpl(userRepository);

        ServletContext servletContext = sce.getServletContext();

        servletContext.setAttribute("userService", userService);

        logger.debug("Context initialized");
    }
}
