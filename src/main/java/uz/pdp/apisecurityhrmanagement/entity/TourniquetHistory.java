package uz.pdp.apisecurityhrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.apisecurityhrmanagement.entity.enums.TourniquetType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
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

    @Enumerated(EnumType.STRING)
    private TourniquetType type;

    @CreationTimestamp
    private Timestamp time;

    @NotNull
    private LocalDateTime enterDateTime;   // Entrance time to work

    private LocalDateTime exitDateTime;    // Exit time from work

    @ManyToOne
    private User user;                  // Tourniketga userni saqalsh uchun
}
