// File: vn.iotstar.entity.Role.java
package vn.iotstar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Users> getUsers() {
		return users;
	}

	public void setUsers(Set<Users> users) {
		this.users = users;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên vai trò (Ví dụ: ROLE_ADMIN, ROLE_USER)
    @Column(length = 255, nullable = false, unique = true)
    private String name;

    // Mối quan hệ Many-to-Many với Users.
    // MappedBy chỉ ra rằng User là bên sở hữu (owner) của mối quan hệ này.
    @ManyToMany(mappedBy = "roles")
    private Set<Users> users;

    // Getter/Setter/Constructor được tạo bởi Lombok
}