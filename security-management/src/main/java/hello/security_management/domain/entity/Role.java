package hello.security_management.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    private String roleName;
    private String roleDesc;
    private String isExpression;

//    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
//    private Set<AccountRole> accountRoleSet = new HashSet<>();

//    @OneToMany(mappedBy = "resources", cascade = CascadeType.ALL)
//    @OrderBy("orderNum desc")
//    private Set<ResourcesRole> resourcesSet = new LinkedHashSet<>();

    @Builder
    public Role(Long id, String roleName, String roleDesc, String isExpression) {
        this.id = id;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.isExpression = isExpression;
    }


}