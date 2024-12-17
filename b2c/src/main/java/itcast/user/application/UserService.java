package itcast.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itcast.domain.user.User;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.jwt.repository.UserRepository;
import itcast.user.dto.request.ProfileCreateRequest;
import itcast.user.dto.request.ProfileUpdateRequest;
import itcast.user.dto.response.ProfileCreateResponse;
import itcast.user.dto.response.ProfileUpdateResponse;
import itcast.user.exception.EmailAlreadyExistsException;
import itcast.user.exception.NicknameAlreadyExistsException;
import itcast.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public ProfileCreateResponse createProfile(ProfileCreateRequest request, Long id) {
        User existingUser = findUserByIdOrThrow(id);
        validateConstraints(request.nickname(), request.email());

        if (ArticleType.NEWS.equals(request.articleType())) {
            request = new ProfileCreateRequest(
                    request.nickname(),
                    request.articleType(),
                    Interest.NEWS,
                    request.sendingType(),
                    request.email()
            );
        }
        User updatedUser = request.toEntity(existingUser);
        User savedUser = userRepository.save(updatedUser);
        return ProfileCreateResponse.fromEntity(savedUser);
    }

    @Transactional
    public ProfileUpdateResponse updateProfile(ProfileUpdateRequest request, Long id) {
        User existingUser = findUserByIdOrThrow(id);
        validateConstraints(request.nickname(), request.email());
        if (ArticleType.NEWS.equals(request.articleType())) {
            request = new ProfileUpdateRequest(
                    request.nickname(),
                    request.articleType(),
                    Interest.NEWS,
                    request.sendingType(),
                    request.email()
            );
        }
        User updatedUser = request.toEntity(existingUser);
        User savedUser = userRepository.save(updatedUser);
        return ProfileUpdateResponse.fromEntity(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserByIdOrThrow(id);
        userRepository.delete(user);
        }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }
    private void validateConstraints(String nickname, String email) {
        if (userRepository.existsByNickname(nickname)) {
            throw new NicknameAlreadyExistsException("이미 사용 중인 닉네임입니다.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }
    }
}