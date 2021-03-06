package uz.pdp.apisecurityhrmanagement.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.apisecurityhrmanagement.entity.SalaryHistory;
import uz.pdp.apisecurityhrmanagement.entity.User;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.SalaryDTO;
import uz.pdp.apisecurityhrmanagement.repository.SalaryRepository;
import uz.pdp.apisecurityhrmanagement.repository.UserRepository;
import uz.pdp.apisecurityhrmanagement.security.JwtProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryService {

    final JwtProvider jwtProvider;
    final UserRepository userRepository;
    final SalaryRepository salaryRepository;
    final AuthenticationManager authenticationManager;

    public SalaryService(JwtProvider jwtProvider, UserRepository userRepository,
                         SalaryRepository salaryRepository, AuthenticationManager authenticationManager) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.salaryRepository = salaryRepository;
        this.authenticationManager = authenticationManager;
    }

    public ApiResponse addSalary(SalaryDTO salaryDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String role = String.valueOf(user.getRoles());
        String position = user.getPosition();

        if ((role.equalsIgnoreCase("user")) || ((role.equalsIgnoreCase("director") && !position.equals("hr_manager"))))
            return new ApiResponse("You position is not allow to do this operation!", false);
        Optional<User> optionalUser = userRepository.findById(salaryDTO.getUserId());
        if (!optionalUser.isPresent()) return new ApiResponse("Invalid Username ID!", false);

        SalaryHistory salary = new SalaryHistory();
        salary.setUser(optionalUser.get());
        salary.setAmount(salaryDTO.getAmount());
        salary.setPaid(salaryDTO.isPaid());
        salary.setMonth(salaryDTO.getMonth());
        salary.setYear(salaryDTO.getYear());
        salaryRepository.save(salary);
        return new ApiResponse("New salary added", true);
    }

    public ApiResponse editSalaryByTask(UUID userId, SalaryDTO salaryDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String role = String.valueOf(user.getRoles());
        String position = user.getPosition();

        if ((role.equalsIgnoreCase("user")) || ((role.equalsIgnoreCase("director") && !position.equals("hr_manager"))))
            return new ApiResponse("You position is not allow to do this operation!", false);
        Optional<User> optionalUser = userRepository.findById(salaryDTO.getUserId());
        if (!optionalUser.isPresent()) return new ApiResponse("Invalid Username ID!", false);
        Optional<SalaryHistory> optionalSalary = salaryRepository.findById(userId);
        if (!optionalSalary.isPresent()) return new ApiResponse("Invalid Salary Id!", false);

        SalaryHistory salary = optionalSalary.get();
        salary.setUser(optionalUser.get());
        salary.setAmount(salaryDTO.getAmount());
        salary.setPaid(salaryDTO.isPaid());
        salary.setMonth(salaryDTO.getMonth());
        salary.setYear(salaryDTO.getYear());
        salaryRepository.save(salary);
        return new ApiResponse("Salary edited", true);
    }

    public ApiResponse getUserSalary(UUID userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String role = String.valueOf(user.getRoles());
        String position = user.getPosition();

        if ((role.equalsIgnoreCase("user")) || ((role.equalsIgnoreCase("director") && !position.equals("hr_manager"))))
            return new ApiResponse("You position is not allow to do this operation!", false);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) return new ApiResponse("Invalid Username ID!", false);
        List<SalaryHistory> historyList = salaryRepository.findByUser(userId);
        return new ApiResponse("User Salary history:", true, historyList);
    }
}
