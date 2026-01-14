package oldmutual.spring.boot.oldmutualchallenge.models;

import oldmutual.spring.boot.oldmutualchallenge.dtos.CountryDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface ICountryApiClient {
    @GetExchange("/all?fields=name,cca2,cca3,flags,capital,region,subregion,population")
    List<CountryDto> getAllCountries();
    @GetExchange("/name/{name}")
    List<CountryDto> getCountryByName(@PathVariable String name);
}
