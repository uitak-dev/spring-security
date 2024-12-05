package hello.security_management.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Getter
@Builder
public class RoleHierarchy {

    @Id
    @GeneratedValue
    @Column(name = "role_hierarchy_id")
    private Long id;

    private String roleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "role_hierarchy_id", insertable = false, updatable = false)
    private RoleHierarchy parent;

    @OneToMany(mappedBy = "parent")
    private Set<RoleHierarchy> children = new HashSet<>();
}
