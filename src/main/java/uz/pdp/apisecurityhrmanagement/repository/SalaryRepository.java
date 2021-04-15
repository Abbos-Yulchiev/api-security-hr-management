package uz.pdp.apisecurityhrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.apisecurityhrmanagement.entity.SalaryHistory;
import uz.pdp.apisecurityhrmanagement.entity.User;

import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<SalaryHistory, UUID> {

    @Query(value = "select amount from SalaryHistory where users.id:?", nativeQuery = true)
    List<SalaryHistory> findByUser(UUID id);
}
