package app.teatracker.api.user;

import app.teatracker.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        boolean userExists = userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (userExists) {
            throw new IllegalArgumentException("User with the same login or email already exists");
        } else {
            user.setPassword(encryptPassword(user.getPassword()));
            return userRepository.save(user);
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }


    private String encryptPassword(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawPassword);
    }
}