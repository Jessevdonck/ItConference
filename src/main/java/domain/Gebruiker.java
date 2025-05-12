package domain;

import jakarta.persistence.*;
import lombok.*;
import utils.GebruikerRol;
import java.util.List;

@Entity
@Table(name = "gebruiker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "gebruikersnaam")
@ToString(of = "gebruikersnaam")
@Builder
public class Gebruiker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String gebruikersnaam;

    @Column(nullable = false)
    private String wachtwoord;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private GebruikerRol rol;

    @OneToMany(mappedBy = "gebruiker", cascade = CascadeType.ALL)
    private List<Favoriet> favorieten;
}
