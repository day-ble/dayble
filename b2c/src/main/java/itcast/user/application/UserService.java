package itcast.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itcast.domain.user.User;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.jwt.repository.UserRepository;
import itcast.user.dto.request.ProfileCreateRequest;
import itcast.user.dto.request.ProfileUpdateRequest;
import itcast.user.dto.response.ProfileCreateResponse;
import itcast.user.dto.response.ProfileUpdateResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public ProfileCreateResponse createProfile(ProfileCreateRequest request, Long id) {
        User existingUser = findUserByIdOrThrow(id);
        validateConstraints(request.nickname(), request.email(), request.phoneNumber());

        if (ArticleType.NEWS.equals(request.articleType())) {
            request = new ProfileCreateRequest(
                    request.nickname(),
                    request.articleType(),
                    Interest.NEWS,
                    request.sendingType(),
                    request.email(),
                    request.phoneNumber()
            );
        }
        User updatedUser = request.toEntity(existingUser);
        User savedUser = userRepository.save(updatedUser);
        return ProfileCreateResponse.fromEntity(savedUser);
    }

    @Transactional
    public ProfileUpdateResponse updateProfile(ProfileUpdateRequest request, Long id) {
        User existingUser = findUserByIdOrThrow(id);
        validateConstraints(request.nickname(), request.email(), request.phoneNumber());
        if (ArticleType.NEWS.equals(request.articleType())) {
            request = new ProfileUpdateRequest(
                    request.nickname(),
                    request.articleType(),
                    Interest.NEWS,
                    request.sendingType(),
                    request.email(),
                    request.phoneNumber()
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
                .orElseThrow(() -> new ItCastApplicationException(ErrorCodes.USER_NOT_FOUND));
    }
    private void validateConstraints(String nickname, String email, String phoneNumber) {
        if (userRepository.existsByNickname(nickname)) {
            throw new ItCastApplicationException(ErrorCodes.NICKNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(email)) {
            throw new ItCastApplicationException(ErrorCodes.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ItCastApplicationException(ErrorCodes.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }
}