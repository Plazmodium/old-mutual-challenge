package oldmutual.spring.boot.oldmutualchallenge.services;

import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import oldmutual.spring.boot.oldmutualchallenge.models.ICountryApiClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
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
        return countryApiClient.getAllCountries().stream()
                .map(countryMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Country> getCountryByName(String name) {
        try {

            return countryApiClient.getCountryByName(name).stream()
                    .map(countryMapper::toModel)
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException.NotFound e) {
            return Collections.emptyList();
        }

    }
}
