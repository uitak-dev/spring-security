package hello.security_management.admin.service;

import hello.security_management.domain.entity.Resources;
import java.util.List;
import java.util.Optional;

public interface ResourcesService {

    Optional<Resources> getResources(long id);

    List<Resources> getResources();

    void createResources(Resources Resources);

    void deleteResources(long id);
}
