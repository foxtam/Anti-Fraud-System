package net.foxtam.antifraudsystem.controller;

import net.foxtam.antifraudsystem.Lock;
import net.foxtam.antifraudsystem.model.User;
import net.foxtam.antifraudsystem.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/auth/user")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userService.register(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (UsernameAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/api/auth/list")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<?> delete(@PathVariable String username) {
        boolean deleted = userService.deleteUser(username);
        if (deleted) {
            Map<String, String> responseMap = Map.of("username", username, "status", "Deleted successfully!");
            return ResponseEntity.ok(responseMap);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<?> changeRole(@Valid @RequestBody UserRole userRole) {
        try {
            User user = userService.changeRoleByUsername(userRole.username(), userRole.role());
            return ResponseEntity.ok(user);
        } catch (WrongRoleException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundUserException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RoleAlreadyProvidedException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/api/auth/access")
    public ResponseEntity<?> changeLock(@Valid @RequestBody UserLock userLock) {
        try {
            userService.changeLockStatus(userLock.username(), userLock.operation());
            String lockInfo = userLock.operation() == Lock.LOCK ? "locked" : "unlocked";
            String userInfo = "User %s %s!".formatted(userLock.username(), lockInfo);
            return ResponseEntity.ok(Map.of("status", userInfo));
        } catch (NotFoundUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (WrongRoleException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
