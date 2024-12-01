package hello.security_management.admin.service;

import hello.security_management.domain.dto.AccountDto;
import hello.security_management.domain.entity.Account;

import java.util.List;

public interface UserManagementService {

    void modifyUser(AccountDto accountDto);

    List<Account> getUsers();
    AccountDto getUser(Long id);

    void deleteUser(Long idx);

}
