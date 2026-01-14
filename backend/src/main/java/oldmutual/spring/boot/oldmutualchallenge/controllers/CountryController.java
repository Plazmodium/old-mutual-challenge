package oldmutual.spring.boot.oldmutualchallenge.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import oldmutual.spring.boot.oldmutualchallenge.services.CountryService;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
@Tag(name = "Country API", description = "Endpoints for retrieving country information")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all countries", responses = {
            @ApiResponse(description = "A list of countries", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Country.class)))
    })
    public ResponseEntity<Iterable<Country>> getAllCountries() {
        Iterable<Country> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Retrieve details about a specific country", responses = {
            @ApiResponse(description = "Details about the country", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Country.class)))
    })
    public ResponseEntity<Iterable<Country>> getCountryByName(
            @Parameter(description = "The name of the country", required = true)
            @PathVariable String name) {
        Iterable<Country> country = countryService.getCountryByName(name);
        return ResponseEntity.ok(country);
    }
}
