
package com.springboot_javawebexamen;

import domain.Event;
import domain.Lokaal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.EventService;
import service.LokaalService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class EventBeheerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @MockitoBean
    private LokaalService lokaalService;

    @Test
    void testToonToevoegenFormGeeftLeegEventEnLokalen() throws Exception {
        when(lokaalService.getAlleLokalen()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/evenementen/toevoegen"))
                .andExpect(status().isOk())
                .andExpect(view().name("event-form"))
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("lokalen"));
    }

    @Test
    void testVerwerkToevoegenMetFouteSprekerData() throws Exception {
        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        when(lokaalService.getAlleLokalen()).thenReturn(Collections.singletonList(lokaal));

        mockMvc.perform(post("/evenementen/toevoegen").with(csrf())
                        .param("naam", "Testevent")
                        .param("datum", "2025-06-01")
                        .param("startuur", "10:00")
                        .param("lokaal.id", "1")
                        .param("sprekersInput", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("event-form"))
                .andExpect(model().attributeHasFieldErrors("event", "sprekers"));
    }

    @Test
    void testToonBewerkFormGeeftCorrecteEventData() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setNaam("Testevent");
        event.setSprekers(Collections.emptySet());
        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));
        when(lokaalService.getAlleLokalen()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/evenementen/bewerken/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("event-form"))
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("lokalen"))
                .andExpect(model().attributeExists("sprekersString"));
    }

    @Test
    void testVerwerkBewerkingMetFouteSprekers() throws Exception {
        when(lokaalService.getAlleLokalen()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/evenementen/bewerken/1").with(csrf())
                        .param("naam", "Testevent")
                        .param("datum", "2025-06-01")
                        .param("startuur", "10:00")
                        .param("lokaal.id", "1")
                        .param("sprekersInput", "Jan,Jan,Anna,Marie"))
                .andExpect(status().isOk())
                .andExpect(view().name("event-form"))
                .andExpect(model().attributeHasErrors("event"))
                .andExpect(model().attributeExists("sprekersString"));
    }

    @Test
    void testVerwijderEventRedirectsCorrect() throws Exception {
        mockMvc.perform(post("/evenementen/verwijderen/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(eventService).deleteEvent(1L);
    }

    @Test
    void testVerwerkToevoegenMetGeldigeData() throws Exception {
        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);

        when(lokaalService.getAlleLokalen()).thenReturn(List.of(lokaal));
        when(eventService.bestaatEventMetZelfdeNaamEnDatum(any())).thenReturn(false);
        when(eventService.isLokaalBezet(anyLong(), any(), any())).thenReturn(false);

        mockMvc.perform(post("/evenementen/toevoegen").with(csrf())
                        .param("naam", "JavaTalks")
                        .param("datum", "2025-06-01")
                        .param("startuur", "14:00")
                        .param("lokaal.id", "1")
                        .param("prijs", "19.99")
                        .param("beamerCode", "1234")
                        .param("beamercheck", "70")
                        .param("sprekersInput", "Alice, Bob"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(eventService).bewaarEvent(any(Event.class));
    }

    @Test
    void testVerwerkBewerkenMetGeldigeData() throws Exception {
        Event bestaandEvent = new Event();
        bestaandEvent.setId(1L);
        bestaandEvent.setNaam("JavaTalks");
        bestaandEvent.setDatum(LocalDate.of(2025, 6, 1));
        bestaandEvent.setStartuur(LocalTime.of(14, 0));
        bestaandEvent.setLokaal(new Lokaal(1L, "A123", 50));
        bestaandEvent.setPrijs(BigDecimal.valueOf(20));

        when(eventService.getEventById(1L)).thenReturn(Optional.of(bestaandEvent));
        when(lokaalService.getAlleLokalen()).thenReturn(List.of(bestaandEvent.getLokaal()));
        when(eventService.bestaatEventMetZelfdeNaamEnDatum(any())).thenReturn(false);
        when(eventService.isLokaalBezet(eq(1L), any(), any())).thenReturn(false);

        mockMvc.perform(post("/evenementen/bewerken/1")
                        .param("naam", "JavaTalks")
                        .param("datum", "2025-06-01")
                        .param("startuur", "14:00")
                        .param("lokaal.id", "1")
                        .param("prijs", "20.00")
                        .param("sprekersInput", "Alice, Bob")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(eventService).bewaarEvent(any(Event.class));
    }
}
