// File: vn.iotstar.repository.UserRepository.java
package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Users;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    // Phương thức tìm kiếm User theo username (Spring Data JPA sẽ tự động triển khai)
    Optional<Users> findByUsername(String username);

}