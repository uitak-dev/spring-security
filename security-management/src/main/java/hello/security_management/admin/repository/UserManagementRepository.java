package hello.security_management.admin.repository;

import hello.security_management.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserManagementRepository extends JpaRepository<Account, Long> {
}
