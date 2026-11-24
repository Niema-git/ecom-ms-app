package ma.enset.inventoryservice;

import ma.enset.inventoryservice.entities.Product;
import ma.enset.inventoryservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository, RepositoryRestConfiguration restConfiguration) {
        restConfiguration.exposeIdsFor(Product.class);
        return args -> {
            productRepository.save(Product.builder().name("Ordinateur").price(7500).quantity(12).build());
            productRepository.save(Product.builder().name("Imprimante").price(1800).quantity(5).build());
            productRepository.save(Product.builder().name("Smartphone").price(3200).quantity(20).build());
            productRepository.findAll().forEach(System.out::println);
        };
    }
}
