package oldmutual.spring.boot.oldmutualchallenge.services;

import oldmutual.spring.boot.oldmutualchallenge.exceptions.CountryNotFoundException;
import oldmutual.spring.boot.oldmutualchallenge.exceptions.ExternalApiException;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import oldmutual.spring.boot.oldmutualchallenge.models.ICountryApiClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Sort;

@Slf4j
@Service
public class CountryService implements ICountryService {

    private final ICountryApiClient countryApiClient;
    private final CountryMapper countryMapper;

    public CountryService(ICountryApiClient countryApiClient, CountryMapper countryMapper) {
        this.countryApiClient = countryApiClient;
        this.countryMapper = countryMapper;
    }

    @Override
    @Retryable(retryFor = {ResourceAccessException.class, HttpServerErrorException.class}, 
               maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    @CircuitBreaker(name = "countryService")
    public List<Country> getAllCountries() {
        log.info("Fetching all countries from external API");
        long startTime = System.currentTimeMillis();
        try {
            List<Country> result = countryApiClient.getAllCountries().stream()
                    .map(countryMapper::toModel)
                    .collect(Collectors.toList());
            log.info("Successfully fetched {} countries from external API in {}ms", result.size(), System.currentTimeMillis() - startTime);
            return result;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error calling external API: {} - Status: {}", e.getMessage(), e.getStatusCode());
            throw new ExternalApiException("Error calling external API: " + e.getMessage(), e);
        } catch (ResourceAccessException e) {
            log.error("Network error or timeout calling external API: {}", e.getMessage());
            throw new ExternalApiException("Network error or timeout calling external API", e);
        }
    }

    @Override
    public Page<Country> getAllCountries(Pageable pageable, String region) {
        List<Country> countries = getAllCountries();
        
        Stream<Country> stream = countries.stream();

        if (region != null && !region.isBlank()) {
            stream = stream.filter(c -> region.equalsIgnoreCase(c.getRegion()));
        }

        if (pageable.getSort().isSorted()) {
            stream = stream.sorted(getCountryComparator(pageable.getSort()));
        }

        List<Country> filteredAndSorted = stream.collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredAndSorted.size());

        if (start > filteredAndSorted.size()) {
            return new PageImpl<>(List.of(), pageable, filteredAndSorted.size());
        }

        return new PageImpl<>(filteredAndSorted.subList(start, end), pageable, filteredAndSorted.size());
    }

    private Comparator<Country> getCountryComparator(Sort sort) {
        Comparator<Country> comparator = null;
        for (Sort.Order order : sort) {
            Comparator<Country> currentComparator = switch (order.getProperty()) {
                case "commonName" -> Comparator.comparing(Country::getCommonName, Comparator.nullsLast(Comparator.naturalOrder()));
                case "officialName" -> Comparator.comparing(Country::getOfficialName, Comparator.nullsLast(Comparator.naturalOrder()));
                case "cca2" -> Comparator.comparing(Country::getCca2, Comparator.nullsLast(Comparator.naturalOrder()));
                case "cca3" -> Comparator.comparing(Country::getCca3, Comparator.nullsLast(Comparator.naturalOrder()));
                case "capital" -> Comparator.comparing(Country::getCapital, Comparator.nullsLast(Comparator.naturalOrder()));
                case "region" -> Comparator.comparing(Country::getRegion, Comparator.nullsLast(Comparator.naturalOrder()));
                case "subregion" -> Comparator.comparing(Country::getSubregion, Comparator.nullsLast(Comparator.naturalOrder()));
                case "population" -> Comparator.comparing(Country::getPopulation, Comparator.nullsLast(Comparator.naturalOrder()));
                default -> null;
            };

            if (currentComparator != null) {
                if (order.isDescending()) {
                    currentComparator = currentComparator.reversed();
                }
                comparator = (comparator == null) ? currentComparator : comparator.thenComparing(currentComparator);
            }
        }
        return (comparator != null) ? comparator : (c1, c2) -> 0;
    }

    @Override
    @Retryable(retryFor = {ResourceAccessException.class, HttpServerErrorException.class}, 
               maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    @CircuitBreaker(name = "countryService")
    public List<Country> getCountryByName(String name) {
        log.info("Fetching country by name: {} from external API", name);
        long startTime = System.currentTimeMillis();
        try {
            List<Country> result = countryApiClient.getCountryByName(name).stream()
                    .map(countryMapper::toModel)
                    .collect(Collectors.toList());
            log.info("Successfully fetched country by name: {} in {}ms", name, System.currentTimeMillis() - startTime);
            return result;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Country not found with name: {} in {}ms", name, System.currentTimeMillis() - startTime);
            throw new CountryNotFoundException("Country not found with name: " + name);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error calling external API for country: {} - Status: {}", name, e.getStatusCode());
            throw new ExternalApiException("Error calling external API: " + e.getMessage(), e);
        } catch (ResourceAccessException e) {
            log.error("Network error or timeout calling external API for country: {}", name);
            throw new ExternalApiException("Network error or timeout calling external API", e);
        }
    }
}
