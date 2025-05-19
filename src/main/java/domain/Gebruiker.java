package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "{gebruiker.gebruikersnaam.leeg}")
    private String gebruikersnaam;

    @Column(nullable = false)
    @NotBlank(message = "{gebruiker.wachtwoord.leeg}")
    private String wachtwoord;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    @NotNull(message = "{gebruiker.rol.leeg}")
    private GebruikerRol rol;

    @OneToMany(mappedBy = "gebruiker", cascade = CascadeType.ALL)
    private List<Favoriet> favorieten;
}
