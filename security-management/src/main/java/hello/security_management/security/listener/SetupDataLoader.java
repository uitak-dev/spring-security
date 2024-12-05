package hello.security_management.security.listener;

import hello.security_management.admin.repository.ResourcesRepository;
import hello.security_management.admin.repository.RoleRepository;
import hello.security_management.domain.entity.Account;
import hello.security_management.domain.entity.Resources;
import hello.security_management.domain.entity.Role;
import hello.security_management.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        setupData();
        alreadySetup = true;
    }

    private void setupData() {
        Set<Role> roles = new HashSet<>();

        Role userRole = createRoleIfNotFound("ROLE_USER", "사용자", "N");
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저", "N");
        Role dbaRole = createRoleIfNotFound("ROLE_DBA", "DB 관리자", "N");
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자", "N");
        Role expressionRole = createRoleIfNotFound("hasRole('DBA') or hasRole('ADMIN')", "총괄 관리자 및 DB 관리자", "Y");

        roles = Set.of(userRole);
        createUserIfNotFound("user", "user@user.com", "pass", roles);

        roles = Set.of(userRole, managerRole);
        createUserIfNotFound("manager", "manager@manager.com", "pass", roles);

        roles = Set.of(userRole, dbaRole);
        createUserIfNotFound("dba", "dba@dba.com", "pass", roles);

        roles = Set.of(userRole, managerRole, dbaRole, adminRole);
        createUserIfNotFound("admin", "admin@admin.com", "pass", roles);

        //
        createResourcesIfNotFound("/user", "url", 0, userRole);
        createResourcesIfNotFound("/manager", "url", 1, managerRole);
        createResourcesIfNotFound("/admin/**", "url", 2, adminRole);
        createResourcesIfNotFound("/db", "url", 3, expressionRole);
    }

    public Role createRoleIfNotFound(String roleName, String roleDesc, String isExpression) {
        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .isExpression(isExpression)
                    .build();
        }
        return roleRepository.save(role);
    }

    public void createUserIfNotFound(final String userName, final String email, final String password, Set<Role> roleSet) {
        Account account = userRepository.findByUsername(userName);

        if (account == null) {
            account = Account.builder()
                    .username(userName)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .build();

            for (Role role : roleSet) {
                account.addRole(role);
            }
        }
        userRepository.save(account);
    }

    public void createResourcesIfNotFound(String name, String type, int orderNum, Role role) {
        Resources resources = resourcesRepository.findByResourceName(name);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(name)
                    .resourceType(type)
                    .orderNum(orderNum)
                    .build();

            Role findRole = createRoleIfNotFound(role.getRoleName(), role.getRoleDesc(), role.getIsExpression());
            resources.addRole(findRole);
        }

        resourcesRepository.save(resources);
    }
}
