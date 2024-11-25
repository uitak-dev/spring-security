package hello.security_management.users.controller;

import hello.security_management.domain.dto.AccountDto;
import hello.security_management.domain.entity.Account;
import hello.security_management.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public String signup(AccountDto accountDto) {
        String encryptedPassword = passwordEncoder.encode(accountDto.getPassword());
        accountDto.setPassword(encryptedPassword);

        Account account = userService.convertToEntity(accountDto);
        userService.createUser(account);

        return "redirect:/";
    }
}
