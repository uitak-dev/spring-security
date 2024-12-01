package hello.security_management.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private int age;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<AccountRole> roles = new HashSet<>();

    @Builder
    public Account(Long id, String username, String password, int age, Set<AccountRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.roles = roles;
    }

    // 연관 관계 편의 메서드
    public void addRole(Role role) {
        roles.add(new AccountRole(this, role));
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
