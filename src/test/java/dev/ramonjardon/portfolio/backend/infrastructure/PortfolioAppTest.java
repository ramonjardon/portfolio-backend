package dev.ramonjardon.portfolio.backend.infrastructure;

import dev.ramonjardon.portfolio.backend.infrastructure.config.ContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(ContainerConfiguration.class)
class PortfolioAppTest{

    @Test
    void contextLoads() {
        // Todo listo y libre de advertencias de código deprecado
    }
}
