package com.springboot_javawebexamen;

import domain.Event;
import domain.Lokaal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.EventService;
import service.LokaalService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/evenementen")
public class EventBeheerController {

    @Autowired
    private EventService eventService;

    @Autowired
    private LokaalService lokaalService;

    @GetMapping("/toevoegen")
    public String toonToevoegenForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("lokalen", lokaalService.getAlleLokalen());
        return "event-form";
    }

    @PostMapping("/toevoegen")
    public String verwerkToevoegen(@Valid @ModelAttribute Event event,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        if (result.hasErrors()) {
            model.addAttribute("lokalen", lokaalService.getAlleLokalen());
            return "event-form";
        }

        eventService.bewaarEvent(event);
        redirectAttributes.addFlashAttribute("message", "Evenement werd toegevoegd.");
        return "redirect:/admin";
    }


    @GetMapping("/bewerken/{id}")
    public String toonBewerkForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event niet gevonden met id " + id));

        model.addAttribute("event", event);
        model.addAttribute("lokalen", lokaalService.getAlleLokalen());
        return "event-form";
    }

    @PostMapping("/bewerken/{id}")
    public String verwerkBewerking(@PathVariable Long id,
                                   @Valid @ModelAttribute Event event,
                                   BindingResult result,
                                   @RequestParam String datum,
                                   @RequestParam String tijd,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        try {
            event.setDatum(LocalDate.parse(datum));
            event.setStartuur(LocalTime.parse(tijd));
        } catch (DateTimeParseException e) {
            result.rejectValue("datum", "invalid", "Ongeldige datum of tijd.");
        }

        if (result.hasErrors()) {
            model.addAttribute("lokalen", lokaalService.getAlleLokalen());
            return "event-form";
        }

        event.setId(id);
        eventService.bewaarEvent(event);
        redirectAttributes.addFlashAttribute("message", "Evenement werd bijgewerkt.");
        return "redirect:/admin";
    }

    @PostMapping("/verwijderen/{id}")
    public String verwijderEvent(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        eventService.deleteEvent(id);
        redirectAttributes.addFlashAttribute("message", "Evenement werd verwijderd.");
        return "redirect:/admin";
    }
}
