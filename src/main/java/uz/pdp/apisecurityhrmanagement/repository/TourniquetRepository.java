package uz.pdp.apisecurityhrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.apisecurityhrmanagement.entity.Task;
import uz.pdp.apisecurityhrmanagement.entity.Tourniquet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TourniquetRepository extends JpaRepository<Tourniquet, Integer> {

    Optional<Task> findAllByTaskTakerEmail(String taskTaker_email);

    @Query(value = "select t.id from task t join user e on t.task_taker_id = e.id  " +
            "where t.status='DONE' and e.id=:userId", nativeQuery = true)
    List<UUID> completedTasks(UUID userId);

    @Query(value = "select count(id) from task where task_taker_id =:userId",
            nativeQuery = true)
    Integer userAllTasks(UUID userId);

    @Query(value = "select count(id) from task where task_taker_id =:userId " +
            "and deadline>completed_date", nativeQuery = true)
    Integer userCompletedTaskOnTime(UUID userId);
}
