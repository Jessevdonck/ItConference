package com.springboot_javawebexamen;

import domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.EventService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public String showDashboard(Model model, Principal principal) {
        List<Event> events = eventService.getAllEventsSorted();
        model.addAttribute("events", events);
        if(principal != null){
            model.addAttribute("username", principal.getName());
        }


        return "dashboard";
    }
}
