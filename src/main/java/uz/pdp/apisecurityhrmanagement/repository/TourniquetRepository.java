package uz.pdp.apisecurityhrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apisecurityhrmanagement.entity.Tourniquet;

import java.util.UUID;

public interface TourniquetRepository extends JpaRepository<Tourniquet, Integer> {
}
