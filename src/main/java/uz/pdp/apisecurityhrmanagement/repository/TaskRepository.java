package uz.pdp.apisecurityhrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.apisecurityhrmanagement.entity.Task;
import uz.pdp.apisecurityhrmanagement.entity.User;
import uz.pdp.apisecurityhrmanagement.entity.enums.TaskStatus;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "select * from Task where users.email :?", nativeQuery = true)
    List<Task> findByTaskTaker(String email);

    List<Task> findAllByStatusAndTaskTaker(TaskStatus status, User taskTaker);

}
