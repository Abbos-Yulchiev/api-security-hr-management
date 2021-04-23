package uz.pdp.apisecurityhrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TourniquetHistory {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Tourniquet tourniquet;      //TurniketHistory o'zida  tourniqetni saqlaydi

    @NotNull
    private LocalDateTime presentTime;   // Entrance and Exit time to work

    private boolean inOut;                //  For check In or out

    @ManyToOne
    private User user;                  // Tourniketga userni saqalsh uchun
}
