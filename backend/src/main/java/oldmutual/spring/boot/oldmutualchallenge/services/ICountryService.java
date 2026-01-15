package oldmutual.spring.boot.oldmutualchallenge.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;

public interface ICountryService {
    List<Country> getAllCountries();
    Page<Country> getAllCountries(Pageable pageable, String region);
    List<Country> getCountryByName(String name);
}
