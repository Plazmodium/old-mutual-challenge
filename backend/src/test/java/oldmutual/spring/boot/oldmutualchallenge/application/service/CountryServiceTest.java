package oldmutual.spring.boot.oldmutualchallenge.application.service;

import oldmutual.spring.boot.oldmutualchallenge.dtos.CountryDto;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import oldmutual.spring.boot.oldmutualchallenge.models.ICountryApiClient;
import oldmutual.spring.boot.oldmutualchallenge.services.CountryMapper;
import oldmutual.spring.boot.oldmutualchallenge.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CountryServiceTest {

    private ICountryApiClient countryApiClient;
    private CountryService countryService;
    private CountryMapper countryMapper;

    @BeforeEach
    void setUp() {
        countryApiClient = Mockito.mock(ICountryApiClient.class);
        countryMapper = Mappers.getMapper(CountryMapper.class);
        countryService = new CountryService(countryApiClient, countryMapper);
    }

    @Test
    void getAllCountries_shouldReturnListOfCountries() {
        // Given
        CountryDto dto = new CountryDto(
                new CountryDto.NameDto("Test", "Official Test", Map.of()),
                "TT",
                "TST",
                new CountryDto.FlagsDto("png", "svg", "alt"),
                List.of("Capital"),
                "Region",
                "Subregion",
                1000L
        );
        when(countryApiClient.getAllCountries()).thenReturn(List.of(dto));

        // When
        Iterable<Country> result = countryService.getAllCountries();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getCommonName()).isEqualTo("Test");
    }

    @Test
    void getCountryByName_shouldReturnCountry_whenFound() {
        // Given
        CountryDto dto = new CountryDto(
                new CountryDto.NameDto("Test", "Official Test", Map.of()),
                "TT",
                "TST",
                new CountryDto.FlagsDto("png", "svg", "alt"),
                List.of("Capital"),
                "Region",
                "Subregion",
                1000L
        );
        when(countryApiClient.getCountryByName("Test")).thenReturn(List.of(dto));

        // When
        Iterable<Country> result = countryService.getCountryByName("Test");

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.iterator().next().getCommonName()).isEqualTo("Test");
    }
}
