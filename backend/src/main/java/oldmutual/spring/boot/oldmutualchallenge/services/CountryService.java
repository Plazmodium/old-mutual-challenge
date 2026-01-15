package oldmutual.spring.boot.oldmutualchallenge.services;

import oldmutual.spring.boot.oldmutualchallenge.exceptions.CountryNotFoundException;
import oldmutual.spring.boot.oldmutualchallenge.exceptions.ExternalApiException;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import oldmutual.spring.boot.oldmutualchallenge.models.ICountryApiClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.stream.Collectors;

@Service
public class CountryService implements ICountryService {

    private final ICountryApiClient countryApiClient;
    private final CountryMapper countryMapper;

    public CountryService(ICountryApiClient countryApiClient, CountryMapper countryMapper) {
        this.countryApiClient = countryApiClient;
        this.countryMapper = countryMapper;
    }

    @Override
    public Iterable<Country> getAllCountries() {
        try {
            return countryApiClient.getAllCountries().stream()
                    .map(countryMapper::toModel)
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalApiException("Error calling external API: " + e.getMessage(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("Network error or timeout calling external API", e);
        }
    }

    @Override
    public Iterable<Country> getCountryByName(String name) {
        try {
            return countryApiClient.getCountryByName(name).stream()
                    .map(countryMapper::toModel)
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException.NotFound e) {
            throw new CountryNotFoundException("Country not found with name: " + name);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalApiException("Error calling external API: " + e.getMessage(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("Network error or timeout calling external API", e);
        }
    }
}
