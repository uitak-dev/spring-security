package hello.security_management.security.service;

import hello.security_management.domain.dto.AccountContext;
import hello.security_management.domain.dto.AccountDto;
import hello.security_management.domain.entity.Account;
import hello.security_management.domain.entity.AccountRole;
import hello.security_management.users.repository.UserRepository;
import hello.security_management.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
@Transactional
public class FormUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = userRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("No user found with username" + username);
        }

//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRoles()));
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (AccountRole accountRole : account.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(accountRole.getRole().getRoleName()));
        }

        AccountDto accountDto = userService.convertToAccountDto(account);
        return new AccountContext(accountDto, authorities);
    }
}
