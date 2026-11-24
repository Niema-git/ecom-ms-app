package ma.enset.customerservice;

import ma.enset.customerservice.config.CustomerConfigParams;
import ma.enset.customerservice.entities.Customer;
import ma.enset.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(CustomerConfigParams.class)
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository, RepositoryRestConfiguration restConfiguration) {
        restConfiguration.exposeIdsFor(Customer.class);
        return args -> {
            customerRepository.save(Customer.builder().name("Niema").email("niema@gmail.com").build());
            customerRepository.save(Customer.builder().name("Yassine").email("yassine@gmail.com").build());
            customerRepository.save(Customer.builder().name("Salma").email("salma@gmail.com").build());
            customerRepository.findAll().forEach(System.out::println);
        };
    }
}
