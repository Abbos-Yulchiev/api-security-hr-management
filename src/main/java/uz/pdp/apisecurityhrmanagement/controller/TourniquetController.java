package uz.pdp.apisecurityhrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.TourniquetDTO;
import uz.pdp.apisecurityhrmanagement.service.TourniquetService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tourniquet")
public class TourniquetController {

    final TourniquetService tourniquetService;

    public TourniquetController(TourniquetService tourniquetService) {
        this.tourniquetService = tourniquetService;
    }


    @PostMapping
    public HttpEntity<?> add(@RequestBody TourniquetDTO tourniquetDTO, HttpServletRequest httpServletRequest) {

        ApiResponse apiResponse = tourniquetService.add(tourniquetDTO, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@RequestBody TourniquetDTO tourniquetDTO, HttpServletRequest httpServletRequest, @PathVariable Integer id) {

        ApiResponse apiResponse = tourniquetService.edit(id, tourniquetDTO, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        ApiResponse response = tourniquetService.delete(id, httpServletRequest);
        return ResponseEntity.status(response.isSuccess() ? 202 : 409).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(tourniquetService.getAll(httpServletRequest));
    }
}
