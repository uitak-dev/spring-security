package hello.security_management.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"account", "role"})
public class AccountRole {

    @Id
    @GeneratedValue
    @Column(name = "account_role_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public AccountRole(Account account, Role role) {
        this.account = account;
        this.role = role;
    }
}
