package oldmutual.spring.boot.oldmutualchallenge.application.service;

import oldmutual.spring.boot.oldmutualchallenge.dtos.CountryDto;
import oldmutual.spring.boot.oldmutualchallenge.exceptions.CountryNotFoundException;
import oldmutual.spring.boot.oldmutualchallenge.exceptions.ExternalApiException;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import oldmutual.spring.boot.oldmutualchallenge.models.ICountryApiClient;
import oldmutual.spring.boot.oldmutualchallenge.services.CountryMapper;
import oldmutual.spring.boot.oldmutualchallenge.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        List<Country> result = countryService.getAllCountries();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCommonName()).isEqualTo("Test");
    }

    @Test
    void getAllCountries_paginated_shouldReturnPageOfCountries() {
        // Given
        CountryDto dto1 = new CountryDto(
                new CountryDto.NameDto("A", "A", Map.of()), "AA", "AAA",
                new CountryDto.FlagsDto("png", "svg", "alt"), List.of("Capital"), "Region", "Subregion", 1000L
        );
        CountryDto dto2 = new CountryDto(
                new CountryDto.NameDto("B", "B", Map.of()), "BB", "BBB",
                new CountryDto.FlagsDto("png", "svg", "alt"), List.of("Capital"), "Region", "Subregion", 1000L
        );
        when(countryApiClient.getAllCountries()).thenReturn(List.of(dto1, dto2));

        // When
        Page<Country> result = countryService.getAllCountries(PageRequest.of(0, 1), null);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getCommonName()).isEqualTo("A");
    }

    @Test
    void getAllCountries_paginatedWithRegionFilter_shouldReturnFilteredPage() {
        // Given
        CountryDto dto1 = new CountryDto(
                new CountryDto.NameDto("South Africa", "South Africa", Map.of()), "ZA", "ZAF",
                new CountryDto.FlagsDto("png", "svg", "alt"), List.of("Pretoria"), "Africa", "Southern Africa", 1000L
        );
        CountryDto dto2 = new CountryDto(
                new CountryDto.NameDto("France", "France", Map.of()), "FR", "FRA",
                new CountryDto.FlagsDto("png", "svg", "alt"), List.of("Paris"), "Europe", "Western Europe", 1000L
        );
        when(countryApiClient.getAllCountries()).thenReturn(List.of(dto1, dto2));

        // When
        Page<Country> result = countryService.getAllCountries(PageRequest.of(0, 10), "Africa");

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getCommonName()).isEqualTo("South Africa");
    }

    @Test
    void getAllCountries_sortedByPopulation_shouldReturnSortedPage() {
        // Given
        CountryDto dto1 = new CountryDto(
                new CountryDto.NameDto("Small", "Small", Map.of()), "SM", "SML",
                new CountryDto.FlagsDto("png", "svg", "alt"), List.of("Capital"), "Region", "Subregion", 100L
        );
        CountryDto dto2 = new CountryDto(
                new CountryDto.NameDto("Large", "Large", Map.of()), "LG", "LRG",
                new CountryDto.FlagsDto("png", "svg", "alt"), List.of("Capital"), "Region", "Subregion", 1000L
        );
        when(countryApiClient.getAllCountries()).thenReturn(List.of(dto1, dto2));

        // When (Ascending)
        Page<Country> resultAsc = countryService.getAllCountries(PageRequest.of(0, 10, Sort.by("population").ascending()), null);
        // Then
        assertThat(resultAsc.getContent().get(0).getCommonName()).isEqualTo("Small");
        assertThat(resultAsc.getContent().get(1).getCommonName()).isEqualTo("Large");

        // When (Descending)
        Page<Country> resultDesc = countryService.getAllCountries(PageRequest.of(0, 10, Sort.by("population").descending()), null);
        // Then
        assertThat(resultDesc.getContent().get(0).getCommonName()).isEqualTo("Large");
        assertThat(resultDesc.getContent().get(1).getCommonName()).isEqualTo("Small");
    }

    @Test
    void getAllCountries_sortedByCommonName_shouldReturnSortedPage() {
        // Given
        CountryDto dto1 = new CountryDto(
                new CountryDto.NameDto("Zambia", "Zambia", Map.of()), "ZM", "ZMB",
                null, null, null, null, null
        );
        CountryDto dto2 = new CountryDto(
                new CountryDto.NameDto("Albania", "Albania", Map.of()), "AL", "ALB",
                null, null, null, null, null
        );
        when(countryApiClient.getAllCountries()).thenReturn(List.of(dto1, dto2));

        // When (Ascending)
        Page<Country> resultAsc = countryService.getAllCountries(PageRequest.of(0, 10, Sort.by("commonName").ascending()), null);
        // Then
        assertThat(resultAsc.getContent().get(0).getCommonName()).isEqualTo("Albania");
        assertThat(resultAsc.getContent().get(1).getCommonName()).isEqualTo("Zambia");
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
        List<Country> result = countryService.getCountryByName("Test");

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCommonName()).isEqualTo("Test");
    }

    @Test
    void getCountryByName_shouldThrowCountryNotFoundException_whenNotFound() {
        // Given
        when(countryApiClient.getCountryByName("Unknown")).thenThrow(HttpClientErrorException.NotFound.class);

        // When & Then
        assertThatThrownBy(() -> countryService.getCountryByName("Unknown"))
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessageContaining("Country not found with name: Unknown");
    }

    @Test
    void getAllCountries_shouldThrowExternalApiException_whenApiFails() {
        // Given
        when(countryApiClient.getAllCountries()).thenThrow(new ResourceAccessException("Timeout"));

        // When & Then
        assertThatThrownBy(() -> countryService.getAllCountries())
                .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining("Network error or timeout calling external API");
    }
}
