package oldmutual.spring.boot.oldmutualchallenge;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import oldmutual.spring.boot.oldmutualchallenge.models.ICountryApiClient;

@SpringBootTest
class OldMutualChallengeApplicationTests {

    @MockitoBean
    private ICountryApiClient countryApiClient;

    @Test
    void contextLoads() {
    }
}
