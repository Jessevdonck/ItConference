package com.springboot_javawebexamen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import service.LokaalService;
import service.EventService;

@Controller
public class AdminController {

    @Autowired
    private LokaalService lokaalService;
    @Autowired
    private EventService eventService;

    @GetMapping("/admin")
    public String toonAdminDashboard(Model model) {
        model.addAttribute("lokalen", lokaalService.getAlleLokalen());
        model.addAttribute("events", eventService.getAllEventsSorted());
        return "admin-dashboard";
    }
}
