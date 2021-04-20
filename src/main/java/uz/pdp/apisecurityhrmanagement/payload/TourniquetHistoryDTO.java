package uz.pdp.apisecurityhrmanagement.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class TourniquetHistoryDTO {

    Integer tourniquetId;
    boolean goingIn;
    UUID userId;
}
