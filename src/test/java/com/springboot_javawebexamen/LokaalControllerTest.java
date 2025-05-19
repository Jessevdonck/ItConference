package com.springboot_javawebexamen;

import domain.Lokaal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.LokaalService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LokaalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LokaalService lokaalService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testToonToevoegenFormGeeftLeegModel() throws Exception {
        mockMvc.perform(get("/lokaal/toevoegen"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lokaal"))
                .andExpect(view().name("lokaal-form"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testVerwerkToevoegenMetGeldigeData() throws Exception {
        when(lokaalService.bestaatLokaalMetNaam("A101")).thenReturn(false);

        mockMvc.perform(post("/lokaal/toevoegen")
                        .param("naam", "A101")
                        .param("capaciteit", "50")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(lokaalService).bewaarLokaal(any(Lokaal.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testVerwerkToevoegenMetBestaandeNaam() throws Exception {
        when(lokaalService.bestaatLokaalMetNaam("Zaal A")).thenReturn(true);

        mockMvc.perform(post("/lokaal/toevoegen")
                        .param("naam", "Zaal A")
                        .param("capaciteit", "50")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("lokaal", "naam"))
                .andExpect(view().name("lokaal-form"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testToonBewerkFormGeeftLokaalTerug() throws Exception {
        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        lokaal.setNaam("Zaal B");
        lokaal.setCapaciteit(100);

        when(lokaalService.getLokaalById(1L)).thenReturn(Optional.of(lokaal));

        mockMvc.perform(get("/lokaal/bewerken/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("lokaal", lokaal))
                .andExpect(view().name("lokaal-form"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testToonBewerkFormVoorOnbestaandLokaalGeeftFout() throws Exception {
        when(lokaalService.getLokaalById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/lokaal/bewerken/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error?errorCode=400&errorMessage=Lokaal+niet+gevonden+met+id+999"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testVerwerkBewerkingMetValidatiefout() throws Exception {
        mockMvc.perform(post("/lokaal/bewerken/1")
                        .param("naam", "")  // leeg veld => fout
                        .param("capaciteit", "50")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("lokaal-form"))
                .andExpect(model().attributeHasFieldErrors("lokaal", "naam"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testVerwijderLokaalSuccesvol() throws Exception {
        mockMvc.perform(post("/lokaal/verwijderen/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(lokaalService).verwijderLokaal(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testVerwijderLokaalMetExceptionGeeftErrorInFlash() throws Exception {
        doThrow(new IllegalStateException("Niet toegestaan")).when(lokaalService).verwijderLokaal(1L);

        mockMvc.perform(post("/lokaal/verwijderen/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attribute("error", "Niet toegestaan"));
    }
}
