package uz.pdp.apisecurityhrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apisecurityhrmanagement.entity.Role;
import uz.pdp.apisecurityhrmanagement.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);
}
