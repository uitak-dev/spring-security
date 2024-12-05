package hello.security_management.security.manager;

import hello.security_management.admin.repository.ResourcesRepository;
import hello.security_management.admin.repository.RoleRepository;
import hello.security_management.security.mapper.MapBasedUrlRoleMapper;
import hello.security_management.security.mapper.PersistentUrlRoleMapper;
import hello.security_management.security.service.DynamicAuthorizationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomDynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final HandlerMappingIntrospector handlerMappingIntrospector;
    private final ResourcesRepository resourcesRepository;
    private final RoleRepository roleRepository;

    private List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;

    private static final AuthorizationDecision ACCESS = new AuthorizationDecision(true);

    private DynamicAuthorizationService dynamicAuthorizationService;

    @PostConstruct
    public void mapping() {
        dynamicAuthorizationService =
                new DynamicAuthorizationService(new PersistentUrlRoleMapper(resourcesRepository, roleRepository));

        setMapping();
    }

    private void setMapping() {
        mappings = dynamicAuthorizationService.getUrlRoleMappings()
                .entrySet()
                .stream()
                .map(entry ->
                        new RequestMatcherEntry<>(
                                new MvcRequestMatcher(handlerMappingIntrospector, entry.getKey()),
                                customAuthorizationManager(entry.getValue())
                        )
                )
                .collect(Collectors.toList());
    }

    private AuthorizationManager<RequestAuthorizationContext> customAuthorizationManager(String role) {

        // "ROLE_" 로 시작하는 경우, AuthorityAuthorizationManager 를 통해 권한 심사.
        // 그 외의 경우 표현식 기반의 인가 매니저를 사용해서 권한 심사를 진행한다.
        if (role != null) {
            if (role.startsWith("ROLE")) {
                return AuthorityAuthorizationManager.hasAuthority(role);
            }
            else {
                return new WebExpressionAuthorizationManager(role);
            }
        }

        return null;
    }

    @Override
    @Deprecated
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext request) {

        for (RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : mappings) {
            RequestMatcher matcher = mapping.getRequestMatcher();
            RequestMatcher.MatchResult matchResult = matcher.matcher(request.getRequest());
            if (matchResult.isMatch()) {
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();
                return manager.check(authentication,
                        new RequestAuthorizationContext(request.getRequest(), matchResult.getVariables()));
            }
        }

        return ACCESS;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    public synchronized void reload() {
        mappings.clear();
        setMapping();
    }
}
