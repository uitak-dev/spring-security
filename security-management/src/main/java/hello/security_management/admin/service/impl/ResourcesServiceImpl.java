package hello.security_management.admin.service.impl;

import hello.security_management.admin.repository.ResourcesRepository;
import hello.security_management.admin.repository.RoleRepository;
import hello.security_management.admin.service.ResourcesService;
import hello.security_management.domain.dto.ResourcesDto;
import hello.security_management.domain.entity.Resources;
import hello.security_management.domain.entity.ResourcesRole;
import hello.security_management.domain.entity.Role;
import hello.security_management.security.manager.CustomDynamicAuthorizationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourcesServiceImpl implements ResourcesService {

    private final ResourcesRepository resourcesRepository;
    private final RoleRepository roleRepository;

    private final CustomDynamicAuthorizationManager authorizationManager;

    @Transactional
    public Optional<Resources> getResources(Long id) {
        return resourcesRepository.findById(id);
    }

    @Transactional
    public List<Resources> getResources() {
        return resourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Transactional
    public void createResources(Resources resources){
        resourcesRepository.save(resources);
        authorizationManager.reload();
    }

    @Transactional
    public void deleteResources(long id) {
        resourcesRepository.deleteById(id);
        authorizationManager.reload();
    }

    public Resources convertToEntity(ResourcesDto resourcesDto) {
        Resources resources = Resources.builder()
                .id(resourcesDto.getId())
                .resourceName(resourcesDto.getResourceName())
                .resourceType(resourcesDto.getResourceType())
                .orderNum(resourcesDto.getOrderNum())
                .httpMethod(resourcesDto.getHttpMethod())
                .build();

        for (Role role : resourcesDto.getRoleSet()) {
            Role findRole = roleRepository.findByRoleName(role.getRoleName());
            resources.addRole(findRole);
        }

        return resources;
    }

    public ResourcesDto convertToResourcesDto(Resources resources) {
        ResourcesDto resourcesDto = ResourcesDto.builder()
                .id(resources.getId())
                .resourceName(resources.getResourceName())
                .resourceType(resources.getResourceType())
                .orderNum(resources.getOrderNum())
                .httpMethod(resources.getHttpMethod())
                .build();

        Set<Role> roleSet = new HashSet<>();
        for (ResourcesRole resourcesRole : resources.getResourcesRoleSet()) {
            roleSet.add(resourcesRole.getRole());
        }
        resourcesDto.setRoleSet(roleSet);

        return resourcesDto;
    }
}