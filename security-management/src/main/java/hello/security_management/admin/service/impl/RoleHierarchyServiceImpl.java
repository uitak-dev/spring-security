package hello.security_management.admin.service.impl;

import hello.security_management.admin.repository.RoleHierarchyRepository;
import hello.security_management.admin.service.RoleHierarchyService;
import hello.security_management.domain.entity.RoleHierarchy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    private RoleHierarchyRepository roleHierarchyRepository;

    @Autowired
    private void setRoleHierarchyServiceImpl(RoleHierarchyRepository roleHierarchyRepository) {
        this.roleHierarchyRepository = roleHierarchyRepository;
    }

    @Override
    @Transactional
    public String findAllHierarchy() {
        List<RoleHierarchy> rolesHierarchy = roleHierarchyRepository.findAll();

        StringBuilder hierarchyRole = new StringBuilder();
        Iterator<RoleHierarchy> itr = rolesHierarchy.iterator();

        while (itr.hasNext()) {
            RoleHierarchy roleHierarchy = itr.next();
            if (roleHierarchy.getParent() != null) {
                hierarchyRole.append(roleHierarchy.getParent().getRoleName());
                hierarchyRole.append(" > ");
                hierarchyRole.append(roleHierarchy.getRoleName());
                hierarchyRole.append("\n");
            }
        }

        return hierarchyRole.toString();
    }
}
