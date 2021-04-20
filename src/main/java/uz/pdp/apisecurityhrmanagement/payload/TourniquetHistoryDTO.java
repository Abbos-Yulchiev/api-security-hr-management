package uz.pdp.apisecurityhrmanagement.payload;

import lombok.Data;

@Data
public class TourniquetHistoryDTO {

    Integer tourniquetId;
    boolean goingIn;
    String user;
}
