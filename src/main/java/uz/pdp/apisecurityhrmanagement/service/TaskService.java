package uz.pdp.apisecurityhrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.apisecurityhrmanagement.entity.Role;
import uz.pdp.apisecurityhrmanagement.entity.Task;
import uz.pdp.apisecurityhrmanagement.entity.User;
import uz.pdp.apisecurityhrmanagement.entity.enums.RoleName;
import uz.pdp.apisecurityhrmanagement.entity.enums.TaskStatus;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.TaskDTO;
import uz.pdp.apisecurityhrmanagement.repository.RoleRepository;
import uz.pdp.apisecurityhrmanagement.repository.TaskRepository;
import uz.pdp.apisecurityhrmanagement.repository.UserRepository;
import uz.pdp.apisecurityhrmanagement.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {

    final TaskRepository taskRepository;
    final JwtProvider jwtProvider;
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final AuthService authService;

    public TaskService(TaskRepository taskRepository, JwtProvider jwtProvider,
                       UserRepository userRepository, RoleRepository roleRepository, AuthService authService) {
        this.taskRepository = taskRepository;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authService = authService;
    }

    public ApiResponse getTasksList(HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        List<Task> taskTaker = taskRepository.findByTaskTaker(email);
        if (taskTaker.isEmpty())
            return new ApiResponse("There is a not Task for you yet", true);
        return new ApiResponse("Your tasks: ", true, taskTaker);
    }

    public ApiResponse addTask(TaskDTO taskDTO, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        if (role.equalsIgnoreCase("user")) return new ApiResponse("Your position is not allow to give a task!", false);
        Optional<User> optionalTaskTaker = userRepository.findByEmail(taskDTO.getTaskTakerEmail());
        if (!optionalTaskTaker.isPresent()) return new
                ApiResponse("Invalid Task taker Email!", false);
        Set<Role> taskTakerRoles = optionalTaskTaker.get().getRoles();
        for (Role taskTakerRole : taskTakerRoles) {
            if (role.equalsIgnoreCase("user") && taskTakerRole.equals(roleRepository.findByRoleName(RoleName.USER))) {
                return new ApiResponse("Your position is not allow to give a task to anyone!", false);
            } else if (role.equalsIgnoreCase("director") && taskTakerRole.equals(roleRepository.findByRoleName(RoleName.DIRECTOR))) {
                return new ApiResponse("Your position is not allow to give a task to Director!", false);
            } else if (role.equalsIgnoreCase("director") && taskTakerRole.equals(roleRepository.findByRoleName(RoleName.HR_MANAGER))) {
                return new ApiResponse("Your position is not allow to give a task to HR_MANAGER", false);
            }
        }
        Task task = new Task();
        task.setTaskTaker(optionalTaskTaker.get());
        task.setDeadline(taskDTO.getDeadline());
        task.setDescription(taskDTO.getDescription());
        task.setName(taskDTO.getName());
        task.setTaskGiver(userRepository.findByEmail(email).get());
        task.setStatus(TaskStatus.NEW);

        String emailBody = "You take a task from " + userRepository.findByEmail(email).get().getFirstName() +
                "Task name:" + taskDTO.getName() + "\nTask description: " + taskDTO.getDescription() + ".Task is available until: " + taskDTO.getDeadline();
        String emailSubject = "You have been assigned a task.";
        authService.emailForTask(taskDTO.getTaskTakerEmail(), emailBody, emailSubject);
        taskRepository.save(task);
        return new ApiResponse("New Task added and Task taker was informed by sending email.", true);
    }
    public ApiResponse delete(UUID id, HttpServletRequest httpServletRequest){

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        if (role.equals("ROLE_STAFF")) return new ApiResponse("c", false);
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) return new ApiResponse("Invalid Task Id", false);
        if (!optionalTask.get().getTaskGiver().getEmail().equals(email))
            return new ApiResponse("Your position is not allow to delete task.", false);

        taskRepository.deleteById(id);
        return new ApiResponse("Task deleted", false);

    }
}
