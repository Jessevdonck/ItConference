package com.springboot_javawebexamen;

import domain.Lokaal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.LokaalService;

@Controller
@RequestMapping("/lokaal")
public class LokaalController {

    @Autowired
    private LokaalService lokaalService;

    @GetMapping("/toevoegen")
    public String toonToevoegenForm(Model model) {
        model.addAttribute("lokaal", new Lokaal());
        return "lokaal-form";
    }

    @PostMapping("/toevoegen")
    public String verwerkToevoegen(@Valid @ModelAttribute Lokaal lokaal,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {

        if (lokaalService.bestaatLokaalMetNaam(lokaal.getNaam())) {
            result.rejectValue("naam", "error.lokaal", "Naam van het lokaal bestaat al.");
        }

        if (result.hasErrors()) {
            return "lokaal-form";
        }

        lokaalService.bewaarLokaal(lokaal);
        redirectAttributes.addFlashAttribute("message",
                "Lokaal met capaciteit van " + lokaal.getCapaciteit() + " werd toegevoegd.");
        return "redirect:/admin";
    }

    @GetMapping("/bewerken/{id}")
    public String toonBewerkForm(@PathVariable Long id, Model model) {
        Lokaal lokaal = lokaalService.getLokaalById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lokaal niet gevonden met id " + id));

        model.addAttribute("lokaal", lokaal);
        return "lokaal-form";
    }

    @PostMapping("/bewerken/{id}")
    public String verwerkBewerking(@PathVariable Long id,
                                   @Valid @ModelAttribute Lokaal lokaal,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "lokaal-form";
        }

        lokaal.setId(id);
        lokaalService.bewaarLokaal(lokaal);
        redirectAttributes.addFlashAttribute("message", "Lokaal " + lokaal.getNaam() + " werd bijgewerkt.");
        return "redirect:/admin";
    }

    @PostMapping("/verwijderen/{id}")
    public String verwijderLokaal(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            lokaalService.verwijderLokaal(id);
            redirectAttributes.addFlashAttribute("message", "Lokaal werd verwijderd.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}
