package nl.lexfelix.simplecalculator.calculationserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("nl.lexfelix.simplecalculator.calculationserver.repository")
@EnableTransactionManagement
public class DatabaseConfig {
}
