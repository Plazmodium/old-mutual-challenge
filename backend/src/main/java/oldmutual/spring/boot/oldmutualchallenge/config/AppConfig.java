package oldmutual.spring.boot.oldmutualchallenge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private String countriesApiUrl;
    private List<String> corsAllowedOrigins;
    private int connectTimeout = 5000;
    private int readTimeout = 5000;

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

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
