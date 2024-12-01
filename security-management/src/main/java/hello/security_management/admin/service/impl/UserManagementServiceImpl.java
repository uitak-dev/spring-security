package hello.security_management.admin.service.impl;

import hello.security_management.admin.repository.RoleRepository;
import hello.security_management.admin.repository.UserManagementRepository;
import hello.security_management.admin.service.UserManagementService;
import hello.security_management.domain.dto.AccountDto;
import hello.security_management.domain.entity.Account;
import hello.security_management.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("userManagementService")
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void modifyUser(AccountDto accountDto){
//        ModelMapper modelMapper = new ModelMapper();
//        Account account = modelMapper.map(accountDto, Account.class);

        Account account = userService.convertToEntity(accountDto);

//        if(accountDto.getRoles() != null){
//            Set<Role> roles = new HashSet<>();
//            accountDto.getRoles().forEach(role -> {
//                Role r = roleRepository.findByRoleName(role);
//                roles.add(r);
//            });
//            account.setUserRoles(roles);
//        }

        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        userManagementRepository.save(account);
    }

    @Transactional
    public AccountDto getUser(Long id) {
//        Account account = userManagementRepository.findById(id).orElse(new Account());

        Account account = userManagementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

//        ModelMapper modelMapper = new ModelMapper();
//        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        AccountDto accountDto = userService.convertToAccountDto(account);

//        List<String> roles = account.getUserRoles()
//                .stream()
//                .map(Role::getRoleName)
//                .collect(Collectors.toList());
//
//        accountDto.setRoles(roles);

        return accountDto;
    }

    @Transactional
    public List<Account> getUsers() {
        return userManagementRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userManagementRepository.deleteById(id);
    }

}