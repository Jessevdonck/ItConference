package com.springboot_javawebexamen;

import domain.Event;
import domain.Lokaal;
import domain.Spreker;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
                                   @RequestParam(required = false) String sprekersInput,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        Set<Spreker> sprekers = Arrays.stream((sprekersInput != null ? sprekersInput : "")
                        .split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(naam -> Spreker.builder().naam(naam).event(event).build())
                .collect(Collectors.toSet());

        event.setSprekers(sprekers);

        if (sprekers.isEmpty()) {
            result.rejectValue("sprekers", "event.spreker.verplicht", "Je moet minstens 1 spreker opgeven.");
        }

        if (sprekers.size() > 3) {
            result.rejectValue("sprekers", "event.sprekers.teveel", "Je mag maximaal 3 sprekers opgeven.");
        }

        var namen = sprekers.stream()
                .map(Spreker::getNaam)
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        var uniekeNamen = new java.util.HashSet<>(namen);
        if (uniekeNamen.size() < namen.size()) {
            result.rejectValue("sprekers", "event.sprekers.dubbel", "Sprekers moeten uniek zijn");
        }

        if(eventService.bestaatEventMetZelfdeNaamEnDatum(event)){
            result.rejectValue("naam", "event.naam.datum.bestaat", "Dit evenement gaat al door vandaag");
        }

        if(eventService.isLokaalBezet(event.getLokaal().getId(), event.getDatum(), event.getStartuur())){
            result.rejectValue("startuur", "event.bestaat", "Er bestaat al een evenement op dit tijdstip");
        }

        if (result.hasErrors()) {
            model.addAttribute("lokalen", lokaalService.getAlleLokalen());
            model.addAttribute("sprekersString", sprekersInput);
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

        String sprekersString = event.getSprekers().stream()
                .map(Spreker::getNaam)
                .map(String::trim)
                .collect(Collectors.joining(", "));

        model.addAttribute("event", event);
        model.addAttribute("lokalen", lokaalService.getAlleLokalen());
        model.addAttribute("sprekersString", sprekersString);
        return "event-form";
    }

    @PostMapping("/bewerken/{id}")
    public String verwerkBewerking(@PathVariable Long id,
                                   @Valid @ModelAttribute Event event,
                                   BindingResult result,
                                   @RequestParam(required = false) String sprekersInput,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        Set<Spreker> sprekers = Arrays.stream((sprekersInput != null ? sprekersInput : "")
                        .split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(naam -> Spreker.builder().naam(naam).event(event).build())
                .collect(Collectors.toSet());

        event.setSprekers(sprekers);

        if (sprekers.isEmpty()) {
            result.rejectValue("sprekers", "event.spreker.verplicht", "Je moet minstens 1 spreker opgeven.");
        }

        if (sprekers.size() > 3) {
            result.rejectValue("sprekers", "event.sprekers.teveel", "Je mag maximaal 3 sprekers opgeven.");
        }

        // check op dubbele namen
        Set<String> uniekeNamen = new HashSet<>();
        for (Spreker spreker : sprekers) {
            if (!uniekeNamen.add(spreker.getNaam().toLowerCase())) {
                result.rejectValue("sprekers", "event.sprekers.dubbel", "Sprekers moeten unieke namen hebben.");
                break;
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("lokalen", lokaalService.getAlleLokalen());
            model.addAttribute("sprekersString", sprekersInput);
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
