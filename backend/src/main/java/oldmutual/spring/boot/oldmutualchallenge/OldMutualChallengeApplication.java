package oldmutual.spring.boot.oldmutualchallenge;

import oldmutual.spring.boot.oldmutualchallenge.config.AppConfig;
import oldmutual.spring.boot.oldmutualchallenge.models.ICountryApiClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class OldMutualChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OldMutualChallengeApplication.class, args);
    }

    @Bean
    public ICountryApiClient countryApiClient(AppConfig appConfig) {
        RestClient restClient = RestClient.builder()
                .baseUrl(appConfig.getCountriesApiUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(ICountryApiClient.class);
    }

}
