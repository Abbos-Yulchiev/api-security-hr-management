package uz.pdp.apisecurityhrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apisecurityhrmanagement.entity.TourniquetHistory;

import java.util.UUID;

public interface TourniquetHistoryRepository extends JpaRepository<TourniquetHistory, UUID> {
}
