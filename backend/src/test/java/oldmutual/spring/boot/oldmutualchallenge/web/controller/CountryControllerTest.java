package oldmutual.spring.boot.oldmutualchallenge.web.controller;

import oldmutual.spring.boot.oldmutualchallenge.exceptions.CountryNotFoundException;
import oldmutual.spring.boot.oldmutualchallenge.exceptions.ExternalApiException;
import oldmutual.spring.boot.oldmutualchallenge.exceptions.GlobalExceptionHandler;
import oldmutual.spring.boot.oldmutualchallenge.services.CountryService;
import oldmutual.spring.boot.oldmutualchallenge.controllers.CountryController;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
@Import({oldmutual.spring.boot.oldmutualchallenge.config.CorsConfig.class, oldmutual.spring.boot.oldmutualchallenge.config.AppConfig.class, GlobalExceptionHandler.class})
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountryService countryService;

    @Test
    void getAllCountries_shouldReturnOkAndPage() throws Exception {
        // Given
        Country country = Country.builder()
                .commonName("South Africa")
                .flagPng("https://flagcdn.com/w320/za.png")
                .capital("Pretoria")
                .build();
        when(countryService.getAllCountries(any(Pageable.class), any()))
                .thenReturn(new PageImpl<>(List.of(country), PageRequest.of(0, 10), 1));

        // When & Then
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].commonName").value("South Africa"))
                .andExpect(jsonPath("$.content[0].flagPng").value("https://flagcdn.com/w320/za.png"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getAllCountries_withParams_shouldReturnOkAndPage() throws Exception {
        // Given
        Country country = Country.builder().commonName("Nigeria").region("Africa").population(200000000L).build();
        when(countryService.getAllCountries(any(Pageable.class), any()))
                .thenReturn(new PageImpl<>(List.of(country), PageRequest.of(0, 10), 1));

        // When & Then
        mockMvc.perform(get("/countries")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "population,desc")
                        .param("region", "Africa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].commonName").value("Nigeria"));
    }

    @Test
    void getAllCountries_shouldReturnEmptyPage_whenNoResults() throws Exception {
        // Given
        when(countryService.getAllCountries(any(Pageable.class), any()))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        // When & Then
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void getAllCountries_all_shouldReturnOkAndList() throws Exception {
        // Given
        Country country = Country.builder()
                .commonName("South Africa")
                .build();
        when(countryService.getAllCountries()).thenReturn(List.of(country));

        // When & Then
        mockMvc.perform(get("/countries/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].commonName").value("South Africa"));
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
    void getCountryByName_shouldReturnNotFound_whenNotExists() throws Exception {
        // Given
        when(countryService.getCountryByName("Atlantis"))
                .thenThrow(new CountryNotFoundException("Country not found with name: Atlantis"));

        // When & Then
        mockMvc.perform(get("/countries/Atlantis"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Country not found with name: Atlantis"));
    }

    @Test
    void getAllCountries_shouldReturnBadGateway_whenExternalApiFails() throws Exception {
        // Given
        when(countryService.getAllCountries(any(Pageable.class), any()))
                .thenThrow(new ExternalApiException("Error calling external API"));

        // When & Then
        mockMvc.perform(get("/countries"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.error").value("Bad Gateway"));
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

    @Test
    void getCountryByName_shouldReturnBadRequest_whenNameIsTooLong() throws Exception {
        String longName = "a".repeat(101);
        mockMvc.perform(get("/countries/" + longName))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(containsString("getCountryByName.name: size must be between 1 and 100")));
    }

    @Test
    void getCountryByName_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        mockMvc.perform(get("/countries/ "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(containsString("must not be blank")));
    }
}
