package com.springboot_javawebexamen;

import domain.Lokaal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.EventService;

@RestController
@RequestMapping("/api/lokaal")
public class LokaalRestController {

    @Autowired
    private EventService eventService;

    @GetMapping("/{id}/capaciteit")
    public int getLokaalCapaciteit(@PathVariable Long id) {
        Lokaal lokaal = eventService.getLokaalById(id);
        return lokaal.getCapaciteit();
    }
}
