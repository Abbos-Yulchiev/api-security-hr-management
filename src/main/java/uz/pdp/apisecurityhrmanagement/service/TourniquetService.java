package uz.pdp.apisecurityhrmanagement.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.apisecurityhrmanagement.entity.Tourniquet;
import uz.pdp.apisecurityhrmanagement.entity.User;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.TourniquetDTO;
import uz.pdp.apisecurityhrmanagement.repository.TourniquetRepository;
import uz.pdp.apisecurityhrmanagement.security.JwtProvider;

import java.util.Optional;

@Service
public class TourniquetService {

    final TourniquetRepository tourniquetRepository;
    final JwtProvider jwtProvider;

    public TourniquetService(TourniquetRepository tourniquetRepository, JwtProvider jwtProvider) {
        this.tourniquetRepository = tourniquetRepository;
        this.jwtProvider = jwtProvider;
    }


    public ApiResponse add(TourniquetDTO tourniquetDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String role = String.valueOf(user.getRoles());

        if (!role.equals("DIRECTOR")) return new ApiResponse("You can not add new tourniquet", false);
        Tourniquet tourniquet = new Tourniquet();
        tourniquet.setLocation(tourniquetDTO.getLocation());
        tourniquetRepository.save(tourniquet);
        return new ApiResponse("New tourniquet added", true);
    }

    public ApiResponse edit(Integer id, TourniquetDTO tourniquetDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String role = String.valueOf(user.getRoles());

        if (!role.equals("DIRECTOR")) return new ApiResponse("You can not edit Tourniquet", false);
        Optional<Tourniquet> optional = tourniquetRepository.findById(id);
        if (!optional.isPresent()) return new ApiResponse("Invalid Tourniquet ID!", false);
        Tourniquet tourniquet = optional.get();
        tourniquet.setLocation(tourniquetDTO.getLocation());
        tourniquetRepository.save(tourniquet);
        return new ApiResponse("Tourniquet edited", true);
    }

    public ApiResponse delete(Integer id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String role = String.valueOf(user.getRoles());

        if (!role.equals("ROLE_DIRECTOR")) return new ApiResponse("You can not delete Tourniquet", false);
        Optional<Tourniquet> optionalTourniquet = tourniquetRepository.findById(id);
        if (!optionalTourniquet.isPresent()) return new ApiResponse("Invalid Tourniquet ID!", false);
        tourniquetRepository.deleteById(id);
        return new ApiResponse("Tourniquet deleted", true);
    }

    public ApiResponse getAll() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String role = String.valueOf(user.getRoles());

        if (!role.equals("DIRECTOR")) return new ApiResponse("You can not check Tourniquet list", false);
        return new ApiResponse("Tourniquets List", true, tourniquetRepository.findAll());
    }
}
