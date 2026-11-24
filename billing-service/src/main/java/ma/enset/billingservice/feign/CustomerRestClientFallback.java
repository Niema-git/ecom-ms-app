package ma.enset.billingservice.feign;

import ma.enset.billingservice.model.Customer;
import org.springframework.stereotype.Component;

// Réponse par défaut quand customer-service est indisponible
@Component
public class CustomerRestClientFallback implements CustomerRestClient {
    @Override
    public Customer getCustomerById(Long id) {
        return new Customer(id, "Non disponible", "Non disponible");
    }
}
