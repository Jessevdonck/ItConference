package com.springboot_javawebexamen;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Event;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.EventService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    void testGetEventsByDateReturnsExpectedEvents() throws Exception {
        LocalDate datum = LocalDate.of(2025, 5, 20);
        Event event = Event.builder()
                .id(1L)
                .naam("Test Event")
                .datum(datum)
                .startuur(LocalTime.of(10, 0))
                .build();

        when(eventService.getEventsOpDatum(datum)).thenReturn(List.of(event));

        mockMvc.perform(get("/api/events")
                        .param("datum", datum.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].naam").value("Test Event"))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testGetEventsByDateReturnsEmptyList() throws Exception {
        LocalDate datum = LocalDate.of(2025, 1, 1);
        when(eventService.getEventsOpDatum(datum)).thenReturn(List.of());

        mockMvc.perform(get("/api/events")
                        .param("datum", datum.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetEventsByDateWithMissingParamReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isBadRequest());
    }
}
