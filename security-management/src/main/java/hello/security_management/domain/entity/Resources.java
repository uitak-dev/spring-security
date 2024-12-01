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

}