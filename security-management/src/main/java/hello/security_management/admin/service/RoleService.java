package hello.security_management.admin.service;

import hello.security_management.domain.dto.RoleDto;
import hello.security_management.domain.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    Optional<Role> getRole(Long id);

    List<Role> getRoles();

    List<Role> getRolesWithoutExpression();

    void createRole(Role role);

    void deleteRole(long id);

    Role convertToEntity(RoleDto roleDto);

    RoleDto convertToRoleDto(Role role);
}
