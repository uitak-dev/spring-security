package hello.security_management.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private int age;
    private String roles;

    @Builder
    public Account(String username, String password, int age, String roles) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.roles = roles;
    }
}
