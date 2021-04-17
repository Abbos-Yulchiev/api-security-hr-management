package uz.pdp.apisecurityhrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.apisecurityhrmanagement.entity.enums.TourniquetType;

import javax.persistence.*;
import java.sql.Timestamp;
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
    private Tourniquet tourniquet;

    @Enumerated(EnumType.STRING)
    private TourniquetType type;

    @CreationTimestamp
    private Timestamp time;

    @ManyToOne
    private User user;
}
