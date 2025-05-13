package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "lokaal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lokaal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z]{1}\\d{3}$", message = "{lokaal.naam.formaat}")
    private String naam;

    @Column(nullable = false, unique = true)
    @Min(value = 1, message = "{lokaal.capaciteit.min}")
    @Max(value = 50, message = "{lokaal.capaciteit.max}")
    private int capaciteit;
}
