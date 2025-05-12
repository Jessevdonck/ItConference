package domain;

import jakarta.persistence.*;
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
    private String naam;

    @Column(nullable = false, unique = true)
    private int capaciteit;
}
