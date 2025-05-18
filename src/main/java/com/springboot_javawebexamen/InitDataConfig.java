package com.springboot_javawebexamen;

import domain.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
public class InitDataConfig implements CommandLineRunner {

    @Autowired
    private GebruikerRepository gebruikerRepository;

    @Autowired
    private LokaalRepository lokaalRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SprekerRepository sprekerRepository;

    @Autowired
    private FavorietRepository favorietRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {

        // ---------- Gebruikers ----------
        var admin = Gebruiker.builder()
                .gebruikersnaam("admin")
                .wachtwoord(encoder.encode("admin123"))
                .rol(utils.GebruikerRol.ADMIN)
                .build();

        var user = Gebruiker.builder()
                .gebruikersnaam("user")
                .wachtwoord(encoder.encode("user123"))
                .rol(utils.GebruikerRol.USER)
                .build();

        gebruikerRepository.saveAll(List.of(admin, user));

        // ---------- Lokalen ----------
        var lokaal1 = Lokaal.builder().naam("A101").capaciteit(30).build();
        var lokaal2 = Lokaal.builder().naam("B202").capaciteit(50).build();
        var lokaal3 = Lokaal.builder().naam("C303").capaciteit(20).build();

        lokaalRepository.saveAll(List.of(lokaal1, lokaal2, lokaal3));

        // ---------- Events + Sprekers ----------
        var event1 = Event.builder()
                .naam("Spring Boot Intro")
                .beschrijving("Een inleiding tot Spring Boot")
                .datum(LocalDate.of(2025, 6, 1))
                .startuur(LocalTime.of(10, 0))
                .beamerCode("1234")
                .beamercheck("70")
                .prijs(new BigDecimal("19.99"))
                .lokaal(lokaal1)
                .build();

        var spreker1a = Spreker.builder().naam("Alice Developer").event(event1).build();
        var spreker1b = Spreker.builder().naam("Bob Coder").event(event1).build();
        event1.setSprekers(Set.of(spreker1a, spreker1b));

        var event2 = Event.builder()
                .naam("Advanced Java")
                .beschrijving("Diepere inzichten in Java 21")
                .datum(LocalDate.of(2025, 6, 1))
                .startuur(LocalTime.of(13, 0))
                .beamerCode("2025")
                .beamercheck("85")
                .prijs(new BigDecimal("24.99"))
                .lokaal(lokaal2)
                .build();

        var spreker2a = Spreker.builder().naam("Charlie Architect").event(event2).build();
        event2.setSprekers(Set.of(spreker2a));

        var event3 = Event.builder()
                .naam("Reactive Programming")
                .beschrijving("Hands-on met WebFlux")
                .datum(LocalDate.of(2025, 6, 2))
                .startuur(LocalTime.of(10, 0))
                .beamerCode("0999")
                .beamercheck("29")
                .prijs(new BigDecimal("29.99"))
                .lokaal(lokaal3)
                .build();

        var spreker3a = Spreker.builder().naam("Dora Functional").event(event3).build();
        var spreker3b = Spreker.builder().naam("Eve Reactive").event(event3).build();
        event3.setSprekers(Set.of(spreker3a, spreker3b));

        eventRepository.saveAll(List.of(event1, event2, event3));
        sprekerRepository.saveAll(List.of(spreker1a, spreker1b, spreker2a, spreker3a, spreker3b));

        // ---------- Favoriet ----------
        var favoriet = new Favoriet();
        favoriet.setGebruiker(user);
        favoriet.setEvent(event1);
        favorietRepository.save(favoriet);
    }
}
