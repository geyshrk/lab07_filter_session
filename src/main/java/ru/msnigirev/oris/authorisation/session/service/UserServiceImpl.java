package ru.msnigirev.oris.authorisation.session.service;

import lombok.AllArgsConstructor;
import ru.msnigirev.oris.authorisation.session.dto.UserDto;
import ru.msnigirev.oris.authorisation.session.entity.User;
import ru.msnigirev.oris.authorisation.session.repository.interfaces.UserRepository;

import java.util.List;


@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public List<UserDto> getAllUsers(int offset, int size) {
        List<User> all = userRepository.getAll(offset, size);
        return all.stream()
                .map(user -> new UserDto(user.getUsername(), user.getEmail(), user.getPhoneNumber()))
                .toList();
    }

    @Override
    public User getUser(String username) {
        return userRepository.getById(username).orElse(null);
    }

    @Override
    public void deleteSessionId(String sessionId) {
        userRepository.deleteSessionId(sessionId);
    }

    @Override
    public void addSessionId(String sessionId, String username) {
        userRepository.addSessionId(sessionId, username);
    }

    @Override
    public void addNewUser(String username, String email, String phoneNumber, String password) {
        userRepository.addNewUser(username, email, phoneNumber, password);
    }

    @Override
    public boolean sessionIdExists(String sessionId) {
        return userRepository.sessionIdExists(sessionId);
    }

}
