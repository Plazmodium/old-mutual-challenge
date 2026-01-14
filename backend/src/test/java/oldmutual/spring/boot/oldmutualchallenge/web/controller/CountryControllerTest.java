package oldmutual.spring.boot.oldmutualchallenge.web.controller;

import oldmutual.spring.boot.oldmutualchallenge.services.CountryService;
import oldmutual.spring.boot.oldmutualchallenge.controllers.CountryController;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
@org.springframework.context.annotation.Import(oldmutual.spring.boot.oldmutualchallenge.config.CorsConfig.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountryService countryService;

    @Test
    void getAllCountries_shouldReturnOkAndList() throws Exception {
        // Given
        Country country = Country.builder()
                .commonName("South Africa")
                .flagPng("https://flagcdn.com/w320/za.png")
                .capital("Pretoria")
                .build();
        when(countryService.getAllCountries()).thenReturn(List.of(country));

        // When & Then
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].commonName").value("South Africa"))
                .andExpect(jsonPath("$[0].flagPng").value("https://flagcdn.com/w320/za.png"));
    }

    @Test
    void getCountryByName_shouldReturnOkAndDetails_whenFound() throws Exception {
        // Given
        Country country = Country.builder()
                .commonName("South Africa")
                .officialName("Republic of South Africa")
                .flagPng("https://flagcdn.com/w320/za.png")
                .capital("Pretoria")
                .build();
        when(countryService.getCountryByName("South Africa")).thenReturn(List.of(country));

        // When & Then
        mockMvc.perform(get("/countries/South Africa"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].commonName").value("South Africa"))
                .andExpect(jsonPath("$[0].officialName").value("Republic of South Africa"))
                .andExpect(jsonPath("$[0].capital").value("Pretoria"))
                .andExpect(jsonPath("$[0].flagPng").value("https://flagcdn.com/w320/za.png"));
    }

    @Test
    void getCountryByName_shouldReturnOkAndEmptyList_whenNotExists() throws Exception {
        // Given
        when(countryService.getCountryByName("Atlantis")).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/countries/Atlantis"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void options_shouldReturnCorsHeaders() throws Exception {
        mockMvc.perform(options("/countries")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"))
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("GET")));
    }
}
