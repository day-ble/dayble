package itcast.repository;

import itcast.domain.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByEmail(String email);
}
