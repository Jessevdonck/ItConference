package com.springboot_javawebexamen;

import domain.Favoriet;
import domain.Gebruiker;
import domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.FavorietService;
import service.EventService;
import service.GebruikerService;

@Controller
@RequestMapping("/favorieten")
public class FavorietController {

    @Autowired
    private FavorietService favorietService;

    @Autowired
    private EventService eventService;

    @Autowired
    private GebruikerService gebruikerService;

    @GetMapping
    public String toonFavorieten(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Gebruiker gebruiker = gebruikerService.getUserByUsername(userDetails.getUsername());
        model.addAttribute("favorieten", favorietService.getFavorietenVoorGebruiker(gebruiker));
        return "favorieten";
    }

    @PostMapping("/toevoegen/{eventId}")
    public String voegToe(@PathVariable Long eventId,
                          @AuthenticationPrincipal UserDetails userDetails) {
        Gebruiker gebruiker = gebruikerService.getUserByUsername(userDetails.getUsername());
        Event event = eventService.getEventById(eventId).orElseThrow();
        favorietService.voegFavorietToe(gebruiker, event);
        return "redirect:/event/" + eventId;
    }

    @PostMapping("/verwijderen/{eventId}")
    public String verwijder(@PathVariable Long eventId,
                            @AuthenticationPrincipal UserDetails userDetails) {
        Gebruiker gebruiker = gebruikerService.getUserByUsername(userDetails.getUsername());
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event niet gevonden"));
        favorietService.verwijderFavoriet(gebruiker, event);
        return "redirect:/event/" + eventId;
    }
}
