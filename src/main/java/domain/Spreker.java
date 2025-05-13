package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = "naam")
@Table(name = "spreker")
public class Spreker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "{spreker.naam.leeg}")
    private String naam;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    @NotNull(message = "{spreker.event.verplicht}")
    private Event event;
}
