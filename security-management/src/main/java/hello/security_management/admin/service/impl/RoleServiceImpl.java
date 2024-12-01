package hello.security_management.admin.service.impl;

import hello.security_management.admin.repository.RoleRepository;
import hello.security_management.admin.service.RoleService;
import hello.security_management.domain.dto.RoleDto;
import hello.security_management.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public Optional<Role> getRole(long id) {
        return roleRepository.findById(id);
    }

    @Transactional
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public List<Role> getRolesWithoutExpression() {
        return roleRepository.findAllRolesWithoutExpression();
    }

    @Transactional
    public void createRole(Role role){
        roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }

    public Role convertToEntity(RoleDto roleDto) {
        Role role = Role.builder()
                .id(roleDto.getId())
                .roleName(roleDto.getRoleName())
                .roleDesc(roleDto.getRoleDesc())
                .isExpression(roleDto.getIsExpression())
                .build();

        return role;
    }
}
