package com.springboot_javawebexamen;

import domain.Event;
import domain.Gebruiker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.EventService;
import service.FavorietService;
import service.GebruikerService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FavorietControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavorietService favorietService;

    @MockitoBean
    private GebruikerService gebruikerService;

    @MockitoBean
    private EventService eventService;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testToonFavorieten() throws Exception {
        Gebruiker gebruiker = new Gebruiker();
        when(gebruikerService.getUserByUsername("user")).thenReturn(gebruiker);
        when(favorietService.getFavorietenVoorGebruiker(gebruiker)).thenReturn(List.of());

        mockMvc.perform(get("/favorieten"))
                .andExpect(status().isOk())
                .andExpect(view().name("favorieten"))
                .andExpect(model().attributeExists("favorieten"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testVoegFavorietToeAlsErNogPlaatsIs() throws Exception {
        Gebruiker gebruiker = new Gebruiker();
        Event event = new Event();
        when(gebruikerService.getUserByUsername("user")).thenReturn(gebruiker);
        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));
        when(favorietService.aantalFavorieten(gebruiker)).thenReturn(2);

        mockMvc.perform(post("/favorieten/toevoegen/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/event/1"));

        verify(favorietService).voegFavorietToe(gebruiker, event);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testVoegFavorietToeAlsMaxBereikt() throws Exception {
        Gebruiker gebruiker = new Gebruiker();
        Event event = new Event();
        when(gebruikerService.getUserByUsername("user")).thenReturn(gebruiker);
        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));
        when(favorietService.aantalFavorieten(gebruiker)).thenReturn(3);

        mockMvc.perform(post("/favorieten/toevoegen/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("foutmelding", "Je kunt maximaal 3 favorieten toevoegen."))
                .andExpect(redirectedUrl("/event/1"));

        verify(favorietService, never()).voegFavorietToe(any(), any());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testVerwijderFavoriet() throws Exception {
        Gebruiker gebruiker = new Gebruiker();
        Event event = new Event();
        when(gebruikerService.getUserByUsername("user")).thenReturn(gebruiker);
        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));

        mockMvc.perform(post("/favorieten/verwijderen/1").with(csrf()))  // CSRF toegevoegd
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/event/1"));

        verify(favorietService).verwijderFavoriet(gebruiker, event);
    }
}
