package ru.msnigirev.oris.authorisation.session.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.msnigirev.oris.authorisation.session.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .username(resultSet.getString("username"))
                .password(resultSet.getString("password"))
                .sessionId(resultSet.getString("session_id"))
                .email(resultSet.getString("email"))
                .phoneNumber(resultSet.getString("phone_number"))
                .build();
    }

}
