package com.springboot_javawebexamen;

import domain.Lokaal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.EventService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LokaalRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    void testGetLokaalCapaciteitGeeftCorrecteWaardeTerug() throws Exception {
        Lokaal lokaal = new Lokaal();
        lokaal.setId(1L);
        lokaal.setNaam("A.123");
        lokaal.setCapaciteit(100);

        when(eventService.getLokaalById(1L)).thenReturn(lokaal);

        mockMvc.perform(get("/api/lokaal/1/capaciteit"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    void testGetLokaalCapaciteitVoorOnbestaandIdGeeftRedirect() throws Exception {
        when(eventService.getLokaalById(999L)).thenThrow(new IllegalArgumentException("Lokaal niet gevonden"));

        mockMvc.perform(get("/api/lokaal/999/capaciteit"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/error?errorCode=400&errorMessage=Lokaal+niet+gevonden"));
    }
}
