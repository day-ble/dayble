package com.dayble.blog.admin.application;

import com.dayble.blog.admin.domain.AdminRepository;
import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.global.exception.ErrorCodes;
import com.dayble.blog.user.domain.User;
import com.dayble.blog.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCheckService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public void isAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new DaybleApplicationException(ErrorCodes.USER_NOT_FOUND));
        String email = user.getKakaoEmail();
        if (!adminRepository.existsByEmail(email)) {
            throw new DaybleApplicationException(ErrorCodes.INVALID_USER);
        }
    }
}
