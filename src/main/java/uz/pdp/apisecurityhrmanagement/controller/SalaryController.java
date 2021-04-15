package uz.pdp.apisecurityhrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.SalaryDTO;
import uz.pdp.apisecurityhrmanagement.service.SalaryService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping("/{userId}")
    public HttpEntity<?> getUserSalary(@PathVariable UUID userId, HttpServletRequest httpServletRequest) {

        ApiResponse apiResponse = salaryService.getUserSalary(userId, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping
    public HttpEntity<?> addSalary(@RequestBody SalaryDTO salaryDTO, HttpServletRequest httpServletRequest) {

        ApiResponse apiResponse = salaryService.addSalary(salaryDTO, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{userId}")
    public HttpEntity<?> editSalaryByTask(@PathVariable UUID userId, @RequestBody SalaryDTO salaryDTO, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = salaryService.editSalaryByTask(userId, salaryDTO, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @DeleteMapping(value = "/{userId}")
    public HttpEntity<?> deleteUser(@PathVariable UUID userId, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = salaryService.deleteUserSalary(userId, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
