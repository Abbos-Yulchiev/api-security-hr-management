package uz.pdp.apisecurityhrmanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.apisecurityhrmanagement.entity.User;
import uz.pdp.apisecurityhrmanagement.entity.enums.RoleName;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.PasswordDTO;
import uz.pdp.apisecurityhrmanagement.payload.RegisterDTO;
import uz.pdp.apisecurityhrmanagement.repository.RoleRepository;
import uz.pdp.apisecurityhrmanagement.repository.UserRepository;
import uz.pdp.apisecurityhrmanagement.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    final UserRepository userRepository;
    final JwtProvider jwtProvider;
    final PasswordEncoder passwordEncoder;
    final RoleRepository roleRepository;
    final AuthService authService;

    public UserService(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authService = authService;
    }


    public ApiResponse addEmployee(RegisterDTO registerDTO, HttpServletRequest httpServletRequest) {

        boolean existsByEmail = userRepository.existsByEmail(registerDTO.getEmail());
        if (existsByEmail)
            return new ApiResponse("This email already exist!", false);
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);
        String emailFromToken = jwtProvider.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(emailFromToken);
        if (!optionalUser.isPresent())
            return new ApiResponse("Invalid user name", false);
        String position = optionalUser.get().getPosition();
        if (role.equals("USER") || (role.equals("DIRECTOR")) && !position.equals("HR_MANAGER"))
            return new ApiResponse("You cannot add an employee!", false);

        User user = new User();
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPosition(registerDTO.getRoleName());

        if (role.equals("HR_MANAGER"))
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.HR_MANAGER)));

        if (role.equals("DIRECTOR"))
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.DIRECTOR)));

        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);
        authService.sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("User successfully registered. Verify your email.", true);
    }

    public ApiResponse editEmployeeByManagerAndHR(String username, PasswordDTO passwordDTO, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);

        String role = jwtProvider.getRoleNameFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);
        if (!role.equals("USER"))
            return new ApiResponse("Director or HR-Manager can not  edit user inform in this  way! Only user can.", false);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("Invalid email address!", false);
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
        userRepository.save(user);
        return new ApiResponse("User password edited.", true);
    }

    public ApiResponse getUserList(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("Invalid email Address!", false);
        String position = optionalUser.get().getPosition();
        if (role.equals("USER") || (role.equals("DIRECTOR") && !position.equals("HR_MANAGER")))
            return new ApiResponse("Your position do not allow get USer information!", false);
        List<User> userList = userRepository.findAll();
        return new ApiResponse("User List", true, userList);
    }

    public ApiResponse getUser(UUID userId) {
        return null;
    }

    public ApiResponse deleteUser(String username, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("Invalid User name!", false);

        String position = optionalUser.get().getPosition();
        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can not delete employee information", false);
        userRepository.deleteById(optionalUser.get().getId());
        return new ApiResponse("User deleted!", true);
    }
}
