package oldmutual.spring.boot.oldmutualchallenge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private String countriesApiUrl;
    private List<String> corsAllowedOrigins;

    public String getCountriesApiUrl() {
        return countriesApiUrl;
    }

    public void setCountriesApiUrl(String countriesApiUrl) {
        this.countriesApiUrl = countriesApiUrl;
    }

    public List<String> getCorsAllowedOrigins() {
        return corsAllowedOrigins;
    }

    public void setCorsAllowedOrigins(List<String> corsAllowedOrigins) {
        this.corsAllowedOrigins = corsAllowedOrigins;
    }
}
