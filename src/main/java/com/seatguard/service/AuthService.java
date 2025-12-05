package com.seatguard.service;

import com.seatguard.dto.LoginRequest;
import com.seatguard.dto.SignupRequest;
import com.seatguard.model.Role;
import com.seatguard.model.User;
import com.seatguard.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public User signup(SignupRequest req) {
        if (userRepository.findByLoginId(req.getLoginId()).isPresent()) {
            throw new RuntimeException("이미 존재하는 ID입니다.");
        }
        User user = new User();
        user.setLoginId(req.getLoginId());
        user.setPassword(req.getPassword()); // 실무에선 BCrypt 써야 함
        user.setName(req.getName());
        user.setRole(Role.valueOf(req.getRole()));
        
        return userRepository.save(user);
    }

    public User login(LoginRequest req) {
        return userRepository.findByLoginId(req.getLoginId())
                .filter(u -> u.getPassword().equals(req.getPassword()))
                .orElse(null);
    }
}
