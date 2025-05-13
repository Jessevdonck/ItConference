package domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "event", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"naam", "datumTijd"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String naam;

    private String beschrijving;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Spreker> sprekers;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lokaal_id")
    private Lokaal lokaal;

    @Column(nullable = false)
    private LocalDateTime datumTijd;

    @Column(nullable = false, length = 4)
    private String beamerCode;

    @Column(nullable = false, length = 2)
    private String beamercheck;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal prijs;
}
