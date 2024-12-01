package hello.security_management.admin.repository;

import hello.security_management.domain.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

    Resources findByResourceNameAndHttpMethod(String resourceName, String httpMethod);

    @Query("select r from Resources r join fetch r.resourcesRoleSet where r.resourceType = 'url' order by r.orderNum desc")
    List<Resources> findAllResources();
}
