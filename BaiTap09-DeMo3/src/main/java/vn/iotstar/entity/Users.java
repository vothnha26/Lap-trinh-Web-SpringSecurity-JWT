// File: vn.iotstar.entity.Users.java
package vn.iotstar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Column(length = 255, nullable = false, unique = true)
    private String username;

    // Mật khẩu đã được mã hóa BCrypt
    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 255)
    private String email;

    private Boolean enabled = true;

    // Mối quan hệ Many-to-Many với Role
    // Đây là bên sở hữu (owner) của mối quan hệ, tạo ra bảng trung gian (joinTable)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "user_id"), // Khóa ngoại trỏ đến bảng users
            inverseJoinColumns = @JoinColumn(name = "role_id") // Khóa ngoại trỏ đến bảng roles
    )
    private Set<Role> roles;

    // Getter/Setter/Constructor được tạo bởi Lombok
}