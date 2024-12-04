package hello.security_management.users.service;

import hello.security_management.admin.repository.RoleRepository;
import hello.security_management.domain.dto.AccountDto;
import hello.security_management.domain.entity.Account;
import hello.security_management.domain.entity.AccountRole;
import hello.security_management.domain.entity.Role;
import hello.security_management.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void createUser(Account account) {
        Role role = roleRepository.findByRoleName("ROLE_USER");
        account.addRole(role);

        userRepository.save(account);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Account convertToEntity(AccountDto accountDto) {
        Account account = Account.builder()
                .id(accountDto.getId())
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .age(accountDto.getAge())
                .build();

        for (String role : accountDto.getRoles()) {
            Role findRole = roleRepository.findByRoleName(role);
            account.addRole(findRole);
        }

        return account;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AccountDto convertToAccountDto(Account account) {
        AccountDto accountDto = AccountDto.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .age(account.getAge())
                .build();

        List<String> roles = new ArrayList<>();
        for (AccountRole accountRole : account.getRoles()) {
            String roleName = accountRole.getRole().getRoleName();
            roles.add(roleName);
        }

        accountDto.setRoles(roles);
        return accountDto;
    }
}
