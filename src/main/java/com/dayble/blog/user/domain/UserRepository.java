package com.dayble.blog.user.domain;

import com.dayble.blog.user.domain.enums.Interest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByKakaoEmail(String kakaoEmail);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.interest = :interest")
    List<User> findAllByInterest(@Param("interest") Interest interest);

    User findByEmail(String email);

    @Query("SELECT u.email FROM User u WHERE u.id = :userId")
    Optional<String> findEmailById(@Param("userId") Long userId);

    User findByPhoneNumber(String phoneNumber);
}
