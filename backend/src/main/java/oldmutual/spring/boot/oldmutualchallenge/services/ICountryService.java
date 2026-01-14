package oldmutual.spring.boot.oldmutualchallenge.services;

import oldmutual.spring.boot.oldmutualchallenge.models.Country;

public interface ICountryService {
    Iterable<Country> getAllCountries();
    Iterable<Country> getCountryByName(String name);
}
