package uz.pdp.apisecurityhrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.apisecurityhrmanagement.entity.Tourniquet;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.TourniquetDTO;
import uz.pdp.apisecurityhrmanagement.repository.TourniquetRepository;
import uz.pdp.apisecurityhrmanagement.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class TourniquetService {

    final TourniquetRepository tourniquetRepository;
    final JwtProvider jwtProvider;

    public TourniquetService(TourniquetRepository tourniquetRepository, JwtProvider jwtProvider) {
        this.tourniquetRepository = tourniquetRepository;
        this.jwtProvider = jwtProvider;
    }


    public ApiResponse add(TourniquetDTO dto, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);

        if (!role.equals("DIRECTOR")) return new ApiResponse("You can not add new tourniquet", false);
        Tourniquet tourniquet = new Tourniquet();
        tourniquet.setLocation(dto.getLocation());
        tourniquetRepository.save(tourniquet);
        return new ApiResponse("New tourniquet added", true);
    }

    public ApiResponse edit(Integer id, TourniquetDTO tourniquetDTO, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);

        if (!role.equals("DIRECTOR")) return new ApiResponse("You can not edit Tourniquet", false);
        Optional<Tourniquet> optional = tourniquetRepository.findById(id);
        if (!optional.isPresent()) return new ApiResponse("Invalid Tourniquet ID!", false);
        Tourniquet tourniquet = optional.get();
        tourniquet.setLocation(tourniquetDTO.getLocation());
        tourniquetRepository.save(tourniquet);
        return new ApiResponse("Tourniquet edited", true);
    }

    public ApiResponse delete(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);

        if (!role.equals("ROLE_DIRECTOR")) return new ApiResponse("You can not delete turniket", false);
        Optional<Tourniquet> optionalTourniquet = tourniquetRepository.findById(id);
        if (!optionalTourniquet.isPresent()) return new ApiResponse("Invalid Tourniquet ID!", false);
        tourniquetRepository.deleteById(id);
        return new ApiResponse("Tourniquet deleted", true);
    }

    public ApiResponse getAll(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleNameFromToken(token);

        if (!role.equals("DIRECTOR")) return new ApiResponse("You can not check Tourniquet list", false);
        return new ApiResponse("Tourniquets List", true, tourniquetRepository.findAll());
    }
}