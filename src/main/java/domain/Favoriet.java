package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "favorieten", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"gebruiker_id", "event_id"}) //Combinatie gebruiker_id en event_id mag maar 1x voorkomen
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favoriet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gebruiker_id")
    @NotNull(message = "{favoriet.gebruiker.verplicht}")
    private Gebruiker gebruiker;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    @NotNull(message = "{favoriet.event.verplicht}")
    private Event event;
}
