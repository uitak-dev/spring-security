package hello.security_management.security.mapper;

import hello.security_management.admin.repository.ResourcesRepository;
import hello.security_management.admin.repository.RoleRepository;
import hello.security_management.domain.entity.Resources;
import hello.security_management.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PersistentUrlRoleMapper implements UrlRoleMapper {

    private final LinkedHashMap<String, String> urlRoleMappings = new LinkedHashMap<>();
    private final ResourcesRepository resourcesRepository;
    private final RoleRepository roleRepository;

    public PersistentUrlRoleMapper(ResourcesRepository resourcesRepository, RoleRepository roleRepository) {
        this.resourcesRepository = resourcesRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public Map<String, String> getUrlRoleMappings() {
        urlRoleMappings.clear();

        List<Resources> resourcesList = resourcesRepository.findAllResources();
        resourcesList.forEach(resources -> {
            resources.getResourcesRoleSet().forEach(resourcesRole -> {
                Role role = roleRepository.findById(resourcesRole.getRole().getId())
                        .orElseThrow(() -> new IllegalArgumentException("role doesn't exist"));

                urlRoleMappings.put(resources.getResourceName(), role.getRoleName());
            });
        });

        return urlRoleMappings;
    }
}
