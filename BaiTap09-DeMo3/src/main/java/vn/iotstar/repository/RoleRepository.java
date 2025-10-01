// File: vn.iotstar.repository.RoleRepository.java
package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Phương thức tìm kiếm Role theo tên (ví dụ: "ROLE_ADMIN")
    Optional<Role> findByName(String name);
}