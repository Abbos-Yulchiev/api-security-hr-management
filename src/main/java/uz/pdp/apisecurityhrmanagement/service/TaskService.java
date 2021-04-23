package uz.pdp.apisecurityhrmanagement.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public ApiResponse getTasksList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Task> taskTaker = taskRepository.findByTaskTaker(user.getEmail());
        if (taskTaker.isEmpty())
            return new ApiResponse("There is a not Task for you yet", true);
        return new ApiResponse("Your tasks: ", true, taskTaker);
    }

    public ApiResponse addTask(TaskDTO taskDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String role = String.valueOf(user.getRoles());

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
        task.setTaskGiver(user);
        task.setStatus(TaskStatus.NEW);

        String emailBody = "You take a task from " + user.getFirstName() +
                "Task name:" + taskDTO.getName() + "\nTask description: " + taskDTO.getDescription() + ".Task is available until: " + taskDTO.getDeadline();
        String emailSubject = "You have been assigned a task.";
        authService.emailForTask(taskDTO.getTaskTakerEmail(), emailBody, emailSubject);
        taskRepository.save(task);
        return new ApiResponse("New Task added and Task taker was informed by sending email.", true);
    }

    public ApiResponse delete(Integer id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String role = String.valueOf(user.getRoles());

        if (role.equals("ROLE_STAFF")) return new ApiResponse("c", false);
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) return new ApiResponse("Invalid Task Id", false);
        if (!optionalTask.get().getTaskGiver().getEmail().equals(user.getEmail()))
            return new ApiResponse("Your position is not allow to delete task.", false);

        taskRepository.deleteById(id);
        return new ApiResponse("Task deleted", false);

    }

    public ApiResponse completeTask(Integer id, Integer taskStatus) {

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Such task id not found!", false);

        TaskStatus status = TaskStatus.values()[taskStatus];
        optionalTask.get().setStatus(status);

        taskRepository.save(optionalTask.get());
        return new ApiResponse("Task status updated!", true);

    }

    public ApiResponse checkEmployeeTask(UUID userId, TaskStatus status) {

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent())
            return new ApiResponse("Invalid user Id!", false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            User user = (User) authentication.getPrincipal();
            Set<Role> roles = user.getRoles();

            boolean checkRoleStatus = false;
            for (Role role : roles) {
                if (role.getRoleName().name().equals("DIRECTOR") || role.getRoleName().name().equals("HR_MANAGER")) {
                    checkRoleStatus = true;
                    break;
                }
            }
            if (!checkRoleStatus)
                return new ApiResponse("You don't have access for this operation!", false);
            List<Task> taskList = taskRepository.findAllByStatusAndTaskTaker(status, user);
            if (taskList.size() == 0)
                return new ApiResponse("There is not any task for this data!", false);
            return new ApiResponse("Success!", true, taskList);
        }
        return new ApiResponse("Authorization empty!", false);
    }
}
