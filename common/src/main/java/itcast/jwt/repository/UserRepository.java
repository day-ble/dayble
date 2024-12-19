package itcast.jwt.repository;

import java.util.List;
import java.util.Optional;

import itcast.domain.user.enums.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import itcast.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByKakaoEmail(String kakaoEmail);
    List<User> findAllByInterest(Interest interest);
    User findByEmail(String userEmail);
    boolean existsByPhoneNumber(String phoneNumber);
}
