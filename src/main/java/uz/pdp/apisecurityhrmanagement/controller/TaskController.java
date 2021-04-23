package uz.pdp.apisecurityhrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apisecurityhrmanagement.entity.enums.TaskStatus;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.TaskDTO;
import uz.pdp.apisecurityhrmanagement.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/task")
public class TaskController {

    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public HttpEntity<?> addTask(TaskDTO taskDTO) {
        ApiResponse apiResponse = taskService.addTask(taskDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping(value = "/{taskId}")
    public HttpEntity<?> deleteTask(@PathVariable Integer taskId) {
        ApiResponse apiResponse = taskService.delete(taskId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> completeTask(@PathVariable Integer id, @RequestParam Integer taskStatus) {
        ApiResponse response = taskService.completeTask(id, taskStatus);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping
    public HttpEntity<?> checkEmployeeTask(@RequestParam UUID employeeId, @RequestParam TaskStatus taskStatus) {
        ApiResponse response = taskService.checkEmployeeTask(employeeId, taskStatus);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

}
