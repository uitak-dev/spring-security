package hello.security_management.security.mapper;

import hello.security_management.admin.repository.ResourcesRepository;
import hello.security_management.domain.entity.Resources;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PersistentUrlRoleMapper implements UrlRoleMapper {

    private final LinkedHashMap<String, String> urlRoleMappings = new LinkedHashMap<>();
    private final ResourcesRepository resourcesRepository;

    @Override
    public Map<String, String> getUrlRoleMappings() {
        List<Resources> resourcesList = resourcesRepository.findAllResources();
        resourcesList.forEach(resources -> {
            resources.getRoleSet().forEach(role -> {
                urlRoleMappings.put(resources.getResourceName(), role.getRoleName());
            });
        });

        return urlRoleMappings;
    }
}
