package hello.security_management.security.configs;

import hello.security_management.admin.service.RoleHierarchyService;
import hello.security_management.admin.service.impl.RoleHierarchyServiceImpl;
import hello.security_management.domain.entity.RoleHierarchy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy(RoleHierarchyService roleHierarchyService) {

        String allHierarchy = roleHierarchyService.findAllHierarchy();
        return RoleHierarchyImpl.fromHierarchy(allHierarchy);
    }
}
