package com.springboot_javawebexamen;

import domain.Event;
import domain.Lokaal;
import domain.Spreker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.EventService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDashboardToontEventsCorrect() throws Exception {
        Lokaal lokaal = Lokaal.builder()
                .id(1L)
                .naam("Zaal A")
                .capaciteit(100)
                .build();

        Set<Spreker> sprekers = new HashSet<>();
        sprekers.add(Spreker.builder().naam("Jan").build());

        Event event = Event.builder()
                .id(1L)
                .naam("Spring Boot Conf")
                .lokaal(lokaal)
                .datum(LocalDate.now())
                .startuur(LocalTime.of(10, 0))
                .sprekers(sprekers)
                .build();

        List<Event> events = List.of(event);

        Mockito.when(eventService.getAllEventsSorted()).thenReturn(events);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("events", events));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testDashboardMetGeenEvents() throws Exception {
        Mockito.when(eventService.getAllEventsSorted()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("events", Collections.emptyList()));
    }
}
