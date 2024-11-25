package hello.security_management.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AccountDto {

    private Long id;
    private String username;
    private String password;
    private int age;
    private String roles;
}
