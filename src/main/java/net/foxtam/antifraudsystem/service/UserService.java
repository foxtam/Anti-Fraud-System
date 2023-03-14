package net.foxtam.antifraudsystem.service;

import net.foxtam.antifraudsystem.model.User;
import net.foxtam.antifraudsystem.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(User user) {
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
            return false;
        } else {
            userRepository.save(user);
            return true;
        }
    }

    public List<User> getAllUsers() {
        return userRepository.getByOrderByIdAsc();
    }

    public boolean deleteUser(String username) {
        long deletedCount = userRepository.deleteByUsernameIgnoreCase(username);
        return deletedCount > 0;
    }
}
