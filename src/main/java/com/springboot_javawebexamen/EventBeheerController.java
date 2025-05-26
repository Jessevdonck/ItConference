package com.springboot_javawebexamen;

import domain.Event;
import domain.Spreker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.EventService;
import service.LokaalService;
import validation.EventValidator;
import validation.SprekersValidator;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/evenementen")
public class EventBeheerController {

    @Autowired
    private EventService eventService;

    @Autowired
    private LokaalService lokaalService;

    @Autowired
    private EventValidator eventValidator;

    @Autowired
    private SprekersValidator sprekersValidator;

    @Autowired
    private Validator validator;

    @InitBinder("event")
    protected void initBinder(WebDataBinder binder){
        binder.addValidators(sprekersValidator, eventValidator);
    }

    @GetMapping("/toevoegen")
    public String toonToevoegenForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("lokalen", lokaalService.getAlleLokalen());
        return "event-form";
    }

    @PostMapping("/toevoegen")
    public String verwerkToevoegen(@ModelAttribute Event event,
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

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        for (ConstraintViolation<Event> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            result.rejectValue(propertyPath, null, message);
        }

        sprekersValidator.validate(event, result);
        eventValidator.validate(event, result);

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
                                   @ModelAttribute Event event,
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

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        for (ConstraintViolation<Event> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            result.rejectValue(propertyPath, null, message);
        }

        sprekersValidator.validate(event, result);
        eventValidator.validate(event, result);

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
