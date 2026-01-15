package oldmutual.spring.boot.oldmutualchallenge.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import oldmutual.spring.boot.oldmutualchallenge.services.CountryService;
import oldmutual.spring.boot.oldmutualchallenge.models.Country;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
@Validated
@Tag(name = "Country API", description = "Endpoints for retrieving country information")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all countries with pagination and optional filtering", parameters = {
            @Parameter(name = "page", in = ParameterIn.QUERY, description = "Zero-based page index (0..N)", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", in = ParameterIn.QUERY, description = "The size of the page to be returned", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(name = "sort", in = ParameterIn.QUERY, description = "Sorting criteria - use format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.", array = @ArraySchema(schema = @Schema(type = "string")))
    }, responses = {
            @ApiResponse(description = "A paginated list of countries", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Country.class)))
    })
    public ResponseEntity<Page<Country>> getAllCountries(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(description = "Filter by region") @RequestParam(required = false) String region) {
        Page<Country> countries = countryService.getAllCountries(pageable, region);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/all")
    @Operation(summary = "Retrieve all countries", responses = {
            @ApiResponse(description = "A list of countries", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Country.class)))
    })
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Retrieve details about a specific country", responses = {
            @ApiResponse(description = "Details about the country", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Country.class)))
    })
    public ResponseEntity<List<Country>> getCountryByName(
            @Parameter(description = "The name of the country", required = true)
            @PathVariable @NotBlank @Size(min = 1, max = 100) String name) {
        List<Country> country = countryService.getCountryByName(name);
        return ResponseEntity.ok(country);
    }
}
