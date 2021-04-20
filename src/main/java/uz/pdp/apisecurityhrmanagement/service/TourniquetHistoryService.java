package uz.pdp.apisecurityhrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.apisecurityhrmanagement.entity.Tourniquet;
import uz.pdp.apisecurityhrmanagement.entity.TourniquetHistory;
import uz.pdp.apisecurityhrmanagement.entity.User;
import uz.pdp.apisecurityhrmanagement.payload.ApiResponse;
import uz.pdp.apisecurityhrmanagement.payload.TourniquetHistoryDTO;
import uz.pdp.apisecurityhrmanagement.repository.TourniquetHistoryRepository;
import uz.pdp.apisecurityhrmanagement.repository.TourniquetRepository;
import uz.pdp.apisecurityhrmanagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TourniquetHistoryService {

    final TourniquetHistoryRepository tourniquetHistoryRepository;
    final TourniquetRepository tourniquetRepository;
    final UserRepository userRepository;

    public TourniquetHistoryService(TourniquetHistoryRepository tourniquetHistoryRepository,
                                    TourniquetRepository tourniquetRepository,
                                    UserRepository userRepository) {
        this.tourniquetHistoryRepository = tourniquetHistoryRepository;
        this.tourniquetRepository = tourniquetRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse addHistory(TourniquetHistoryDTO historyDTO) {

        Optional<User> optionalUser = userRepository.findById(historyDTO.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Invalid User Id!", false);

        TourniquetHistory tourniquetHistory = new TourniquetHistory();
        Optional<Tourniquet> optionalTourniquet = tourniquetRepository.findById(historyDTO.getTourniquetId());
        if (!optionalTourniquet.isPresent()) return new ApiResponse("Invalid Tourniquet ID!", false);

        tourniquetHistory.setTourniquet(optionalTourniquet.get());
        tourniquetHistory.setUser(optionalUser.get());

        if (historyDTO.isGoingIn()) {
            if (!optionalTourniquet.get().isStatus())
                return new ApiResponse("You using the tourniquet illegally!", false);
            tourniquetHistory.setEnterDateTime(LocalDateTime.now());
            optionalTourniquet.get().setStatus(true);
        } else {
            if (optionalTourniquet.get().isStatus())
                return new ApiResponse("You using the tourniquet illegally!", false);
            tourniquetHistory.setExitDateTime(LocalDateTime.now());
            optionalTourniquet.get().setStatus(false);
        }
        tourniquetHistoryRepository.save(tourniquetHistory);
        return new ApiResponse("Input and output saved", true);
    }
}
