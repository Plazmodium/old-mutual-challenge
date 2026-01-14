package oldmutual.spring.boot.oldmutualchallenge.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information about a country")
public class Country {

    @Schema(description = "Common name of the country", example = "South Africa")
    private String commonName;

    @Schema(description = "Official name of the country", example = "Republic of South Africa")
    private String officialName;

    @Schema(description = "Two-letter country code (ISO 3166-1 alpha-2)", example = "ZA")
    private String cca2;

    @Schema(description = "Three-letter country code (ISO 3166-1 alpha-3)", example = "ZAF")
    private String cca3;

    @Schema(description = "URL to the PNG version of the flag", example = "https://flagcdn.com/w320/za.png")
    private String flagPng;

    @Schema(description = "URL to the SVG version of the flag", example = "https://flagcdn.com/za.svg")
    private String flagSvg;

    @Schema(description = "Primary capital city", example = "Pretoria")
    private String capital;

    @Schema(description = "The region the country belongs to", example = "Africa")
    private String region;

    @Schema(description = "The subregion the country belongs to", example = "Southern Africa")
    private String subregion;

    @Schema(description = "Total population count", example = "59308690")
    private Long population;
}