package uz.pdp.apisecurityhrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.PasswordDTO;
import uz.pdp.apisecurityhrmanagement.payload.RegisterDTO;
import uz.pdp.apisecurityhrmanagement.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Getting all users
     */
    @GetMapping
    public ResponseEntity<?> getUserList(HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = userService.getUserList(httpServletRequest);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public HttpEntity<?> addEmployee(@RequestBody RegisterDTO registerDTO, HttpServletRequest httpServletRequest) {

        ApiResponse apiResponse = userService.addEmployee(registerDTO, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping(value = "/{username}")
    public HttpEntity<?> editEmployee(@PathVariable String username, @RequestBody PasswordDTO passwordDTO, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = userService.editEmployeeByManagerAndHR(username, passwordDTO, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @DeleteMapping(value = "/{username}")
    public HttpEntity<?> deleteUser(@PathVariable String username, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = userService.deleteUser(username, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

}
