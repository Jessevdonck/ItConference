package domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import validation.ValidBeamerCheck;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "event", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"naam", "datum", "startuur"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidBeamerCheck
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z].*", message = "{event.naam.startletter}")
    @NotNull(message = "{event.naam.startletter}")
    private String naam;

    private String beschrijving;

    @Column(nullable = false)
    @DecimalMin(value = "9.99", message = "{event.prijs.min}")
    @DecimalMax(value = "100.00", message = "{event.prijs.max}")
    @NotNull(message="{event.prijs.verplicht}")
    private BigDecimal prijs;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Size(max = 3, message = "{event.sprekers.max3}")
    private Set<Spreker> sprekers;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lokaal_id")
    @NotNull(message = "{event.lokaal.verplicht}")
    private Lokaal lokaal;

    @Column(nullable = false)
    @NotNull(message = "{event.datum.verplicht}")
    private LocalDate datum;

    @Column(nullable = false)
    @NotNull(message = "{event.lokaal.verplicht}")
    private LocalTime startuur;

    @Column(nullable = false, length = 4)
    @Pattern(regexp = "\\d{4}", message = "{event.beamerCode.format}")
    private String beamerCode;

    @Column(nullable = false, length = 2)
    @Pattern(regexp = "\\d{2}", message = "{event.beamercheck.format}")
    private String beamercheck;
}
