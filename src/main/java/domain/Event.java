package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Pattern(regexp = "^[A-Za-z].*", message = "{event.naam.startletter}")
    private String naam;

    private String beschrijving;

    @Column(nullable = false)
    @DecimalMin(value = "9.99", message = "{event.prijs.min}")
    @DecimalMax(value = "100.00", message = "{event.prijs.max}")
    private BigDecimal prijs;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(max = 3, message = "{event.sprekers.max3}")
    private Set<Spreker> sprekers;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lokaal_id")
    @NotNull(message = "{event.lokaal.verplicht}")
    private Lokaal lokaal;

    @Column(nullable = false)
    @NotNull(message = "{event.datumTijd.verplicht}")
    private LocalDateTime datumTijd;

    @Column(nullable = false, length = 4)
    @Pattern(regexp = "\\d{4}", message = "{event.beamerCode.format}")
    private String beamerCode;

    @Column(nullable = false, length = 2)
    @Pattern(regexp = "\\d{2}", message = "{event.beamercheck.format}")
    private String beamercheck;
}
