package oldmutual.spring.boot.oldmutualchallenge;

import oldmutual.spring.boot.oldmutualchallenge.config.AppConfig;
import oldmutual.spring.boot.oldmutualchallenge.models.ICountryApiClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableRetry
public class OldMutualChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OldMutualChallengeApplication.class, args);
    }

    @Bean
    public ICountryApiClient countryApiClient(AppConfig appConfig) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(appConfig.getConnectTimeout());
        factory.setReadTimeout(appConfig.getReadTimeout());

        RestClient restClient = RestClient.builder()
                .baseUrl(appConfig.getCountriesApiUrl())
                .requestFactory(factory)
                .build();

        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return proxyFactory.createClient(ICountryApiClient.class);
    }

}
