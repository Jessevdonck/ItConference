package domain;

import jakarta.persistence.*;
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
    private String naam;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;
}
