package ru.msnigirev.oris.authorisation.session.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.msnigirev.oris.authorisation.session.entity.User;
import ru.msnigirev.oris.authorisation.session.repository.interfaces.UserRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper;

    private final static String GET_BY_USERNAME = "select * from users where username = ?";
    private final static String GET_ALL = "select * from users";
    private final static String GET_ALL_PAGEABLE = "select * from users offset ? limit ?";
    private final static String DELETE_SESSION_ID = "UPDATE users SET session_id = NULL WHERE session_id = ?";
    private final static String ADD_SESSION_ID = "UPDATE users SET session_id = ? WHERE username = ?";
    private final static String GET_BY_ID_SESSION = "select * from users where session_id = ?";
    private final static String ADD_NEW_USER = "INSERT INTO users (username, email, phone_number, password) " +
            "VALUES (?, ?, ?, ?);";

    public UserRepositoryImpl(DataSource dataSource, RowMapper<User> rowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<User> getById(String id) {
        List<User> users = jdbcTemplate.query(GET_BY_USERNAME, rowMapper, id);
        return optionalSingleResult(users);
    }

    private Optional<User> optionalSingleResult(List<User> users) {
        if (users.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1);
        } else {
            return users.stream().findAny();
        }
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL, rowMapper);
    }

    @Override
    public List<User> getAll(int offset, int size) {
        return jdbcTemplate.query(GET_ALL_PAGEABLE, rowMapper, offset, size);
    }

    @Override
    public boolean update(User type) {
        return false;
    }

    @Override
    public boolean delete(User type) {
        return false;
    }


    @Override
    public List<User> getAllByName(String name) {
        return null;
    }

    @Override
    public void deleteSessionId(String sessionId) {
        jdbcTemplate.update(DELETE_SESSION_ID, sessionId);
    }

    @Override
    public void addSessionId(String sessionId, String username) {
        jdbcTemplate.update(ADD_SESSION_ID, sessionId, username);
    }

    @Override
    public boolean sessionIdExists(String sessionId) {
        List<User> users = jdbcTemplate.query(GET_BY_ID_SESSION, rowMapper, sessionId);
        return optionalSingleResult(users).isPresent();
    }

    @Override
    public void addNewUser(String username, String email, String phoneNumber, String password) {
        jdbcTemplate.update(ADD_NEW_USER, username, email, phoneNumber, password);
    }

}
