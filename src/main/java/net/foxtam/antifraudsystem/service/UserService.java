package net.foxtam.antifraudsystem.service;

import lombok.NonNull;
import net.foxtam.antifraudsystem.Lock;
import net.foxtam.antifraudsystem.Role;
import net.foxtam.antifraudsystem.exceptions.AlreadyExistsException;
import net.foxtam.antifraudsystem.exceptions.NotFoundException;
import net.foxtam.antifraudsystem.exceptions.RoleAlreadyProvidedException;
import net.foxtam.antifraudsystem.exceptions.WrongRoleException;
import net.foxtam.antifraudsystem.model.User;
import net.foxtam.antifraudsystem.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) throws AlreadyExistsException {
        long count = userRepository.count();
        if (count == 0) user.setRole(Role.ADMINISTRATOR);
        else user.setRole(Role.MERCHANT);
        
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
            throw new AlreadyExistsException(user.getUsername() + " already exists");
        }
        if (user.getRole() == Role.ADMINISTRATOR) {
            user.setLocked(false);
        }
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.getByOrderByIdAsc();
    }

    public boolean deleteUser(String username) {
        long deletedCount = userRepository.deleteByUsernameIgnoreCase(username);
        return deletedCount > 0;
    }

    public User changeRoleByUsername(@NonNull String username, @NonNull Role role) throws WrongRoleException, NotFoundException, RoleAlreadyProvidedException {
        if (role == Role.ADMINISTRATOR) throw new WrongRoleException(role.toString());
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        if (userOptional.isEmpty()) throw new NotFoundException(username);
        User user = userOptional.get();
        if (user.getRole() == role) throw new RoleAlreadyProvidedException(username);
        user.setRole(role);
        userRepository.save(user);
        return user;
    }

    public void changeLockStatus(@NonNull String username, @NonNull Lock lock) throws NotFoundException, WrongRoleException {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        if (userOptional.isEmpty()) throw new NotFoundException(username);
        if (userOptional.get().getRole() == Role.ADMINISTRATOR) {
            throw new WrongRoleException("Can't lock/unlock ADMINISTRATOR");
        }
        userOptional.ifPresent(user -> {
            user.setLocked(lock == Lock.LOCK);
            userRepository.save(user);
        });
    }
}
