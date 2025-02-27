package com.dayble.blog.user.application;

import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.global.exception.ErrorCodes;
import com.dayble.blog.user.controller.dto.request.ProfileCreateRequest;
import com.dayble.blog.user.controller.dto.request.ProfileUpdateRequest;
import com.dayble.blog.user.controller.dto.response.ProfileCreateResponse;
import com.dayble.blog.user.controller.dto.response.ProfileUpdateResponse;
import com.dayble.blog.user.domain.User;
import com.dayble.blog.user.domain.UserRepository;
import com.dayble.blog.user.domain.enums.ArticleType;
import com.dayble.blog.user.domain.enums.Interest;
import com.dayble.blog.user.domain.enums.SendingType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public ProfileCreateResponse createProfile(ProfileCreateRequest request, Long id) {
        User existingUser = findUserByIdOrThrow(id);
        validateSendingTypeConstraints(request.sendingType(), request.phoneNumber(), request.email());
        validateVerification(request);
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
        validateSendingTypeConstraints(request.sendingType(), request.phoneNumber(), request.email());
        validateVerification(request);
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
                .orElseThrow(() -> new DaybleApplicationException(ErrorCodes.USER_NOT_FOUND));
    }

    //닉네임, 이메일, 번호 중복 확인
    private void validateConstraints(String nickname, String email, String phoneNumber) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DaybleApplicationException(ErrorCodes.NICKNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(email)) {
            throw new DaybleApplicationException(ErrorCodes.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DaybleApplicationException(ErrorCodes.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    // SendingType에 따라 필수 항목 확인
    private void validateSendingTypeConstraints(SendingType sendingType, String phoneNumber, String email) {
        if (SendingType.MESSAGE.equals(sendingType) && (phoneNumber == null || phoneNumber.isEmpty())) {
            throw new DaybleApplicationException(ErrorCodes.PHONE_NUMBER_REQUIRED);
        }

        if (SendingType.EMAIL.equals(sendingType) && (email == null || email.isEmpty())) {
            throw new DaybleApplicationException(ErrorCodes.EMAIL_REQUIRED);
        }
    }

    //SendingType에 따라 인증 확인
    public void validateVerification(ProfileUpdateRequest request) {
        validateSendingTypeVerification(request.sendingType(), request.phoneNumber(), request.email());
    }

    public void validateVerification(ProfileCreateRequest request) {
        validateSendingTypeVerification(request.sendingType(), request.phoneNumber(), request.email());
    }

    private void validateSendingTypeVerification(SendingType sendingType, String phoneNumber, String email) {
        if (SendingType.MESSAGE.equals(sendingType)) {
            if (!isVerifiedPhoneNumber(phoneNumber)) {
                throw new DaybleApplicationException(ErrorCodes.PHONE_NUMBER_VERIFICATION_REQUIRED);
            }
        } else if (SendingType.EMAIL.equals(sendingType)) {
            if (!isVerifiedEmail(email)) {
                throw new DaybleApplicationException(ErrorCodes.EMAIL_VERIFICATION_REQUIRED);
            }
        }
    }

    private boolean isVerifiedPhoneNumber(String phoneNumber) {
        Boolean isVerified = (Boolean) redisTemplate.opsForValue().get("VERIFIED_PHONE_NUMBER" + phoneNumber);
        return isVerified != null && isVerified;
    }

    private boolean isVerifiedEmail(String email) {
        Boolean isVerified = (Boolean) redisTemplate.opsForValue().get("VERIFIED_EMAIL" + email);
        return isVerified != null && isVerified;
    }
}
