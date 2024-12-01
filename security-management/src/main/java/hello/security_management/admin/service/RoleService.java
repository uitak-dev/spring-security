package hello.security_management.admin.service;

import hello.security_management.domain.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    Optional<Role> getRole(long id);

    List<Role> getRoles();

    List<Role> getRolesWithoutExpression();

    void createRole(Role role);

    void deleteRole(long id);
}
