package uz.pdp.apisecurityhrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.apisecurityhrmanagement.entity.TourniquetHistory;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TourniquetHistoryRepository extends JpaRepository<TourniquetHistory, UUID> {

    @Query(value = "select t.id from tournique_history t join users u on t.user_id = e.id \n" +
            "where e.id =:userId and \n" +
            "t.time between :from and :to", nativeQuery = true)
    List<UUID> InAndOut(UUID userId, Timestamp from, Timestamp to);
}
