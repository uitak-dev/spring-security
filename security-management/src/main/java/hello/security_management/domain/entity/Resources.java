package hello.security_management.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = { AuditingEntityListener.class })
public class Resources implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "resources_id")
    private Long id;

    private String resourceName;
    private String httpMethod;
    private int orderNum;
    private String resourceType;

    @OneToMany(mappedBy = "resources", cascade = CascadeType.ALL)
    private Set<ResourcesRole> resourcesRoleSet = new HashSet<>();

    @Builder
    public Resources(Long id, String resourceName, String httpMethod, int orderNum, String resourceType) {
        this.id = id;
        this.resourceName = resourceName;
        this.httpMethod = httpMethod;
        this.orderNum = orderNum;
        this.resourceType = resourceType;
    }

    // 연관 관계 편의 메서드
    public void addRole(Role role) {
        resourcesRoleSet.add(new ResourcesRole(role, this));
    }

    public Set<Role> getRoleSet() {
        Set<Role> ret = new HashSet<>();
        for (ResourcesRole resourcesRole : resourcesRoleSet) {
            ret.add(resourcesRole.getRole());
        }
        return ret;
    }
}