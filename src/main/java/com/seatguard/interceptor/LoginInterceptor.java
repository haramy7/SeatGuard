package com.seatguard.interceptor;

import com.seatguard.model.User;
import com.seatguard.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        // 1. 세션에 유저가 있으면 통과
        if (session.getAttribute("user") != null) {
            return true;
        }

        // 2. 세션 없으면 쿠키 뒤짐
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SEATGUARD_TOKEN".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    // DB에서 토큰으로 유저 찾기
                    Optional<User> userOpt = userRepository.findAll().stream()
                            .filter(u -> token.equals(u.getLoginToken()))
                            .findFirst();

                    if (userOpt.isPresent()) {
                        // 유저 찾았으면 세션 복구 (부활!)
                        session.setAttribute("user", userOpt.get());
                        return true;
                    }
                }
            }
        }

        // 3. 다 실패하면 로그인 페이지로 쫓아냄
        response.sendRedirect("/login");
        return false;
    }
}
