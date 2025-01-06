package itcast.application;

import itcast.domain.user.User;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.jwt.repository.UserRepository;
import itcast.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCheckService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public void isAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ItCastApplicationException(ErrorCodes.USER_NOT_FOUND));
        String email = user.getKakaoEmail();
        if (!adminRepository.existsByEmail(email)) {
            throw new ItCastApplicationException(ErrorCodes.INVALID_USER);
        }
    }
}
