package oldmutual.spring.boot.oldmutualchallenge;

import oldmutual.spring.boot.oldmutualchallenge.dtos.CountryDto;
import oldmutual.spring.boot.oldmutualchallenge.models.ICountryApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICountryApiClient countryApiClient;

    @Test
    void getAllCountries_Integration_shouldReturnPaginatedData() throws Exception {
        // Given
        CountryDto dto1 = new CountryDto(new CountryDto.NameDto("South Africa", "SA", Map.of()), "ZA", "ZAF", null, null, "Africa", null, 60000000L);
        CountryDto dto2 = new CountryDto(new CountryDto.NameDto("France", "FR", Map.of()), "FR", "FRA", null, null, "Europe", null, 67000000L);
        
        when(countryApiClient.getAllCountries()).thenReturn(List.of(dto1, dto2));

        // When & Then
        mockMvc.perform(get("/countries")
                        .param("region", "Africa"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].commonName").value("South Africa"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getCountryByName_Integration_shouldReturnCountryDetails() throws Exception {
        // Given
        CountryDto dto = new CountryDto(new CountryDto.NameDto("South Africa", "SA", Map.of()), "ZA", "ZAF", null, List.of("Pretoria"), "Africa", null, 60000000L);
        when(countryApiClient.getCountryByName("South Africa")).thenReturn(List.of(dto));

        // When & Then
        mockMvc.perform(get("/countries/South Africa"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].commonName").value("South Africa"))
                .andExpect(jsonPath("$[0].capital").value("Pretoria"));
    }
}
