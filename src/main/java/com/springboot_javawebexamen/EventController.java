package com.springboot_javawebexamen;

import domain.Event;
import domain.Gebruiker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import service.EventService;
import service.FavorietService;
import service.GebruikerService;
import utils.GebruikerRol;

import java.security.Principal;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private GebruikerService gebruikerService;
    @Autowired
    FavorietService favorietService;

    @GetMapping("/event/{id}")
    public String toonEvent(@PathVariable Long id, Model model, Principal principal) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event niet gevonden"));

        model.addAttribute("event", event);

        if (principal != null) {
            Gebruiker gebruiker = gebruikerService.getUserByUsername(principal.getName());

            boolean alFavoriet = favorietService.bestaatFavoriet(gebruiker, event);
            boolean limietBereikt = favorietService.aantalFavorieten(gebruiker) >= 5;

            model.addAttribute("magToevoegenAanFavorieten", !alFavoriet && !limietBereikt);
        }

        return "event-detail";
    }


}
