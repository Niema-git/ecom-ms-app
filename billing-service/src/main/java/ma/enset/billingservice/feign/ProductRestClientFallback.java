package ma.enset.billingservice.feign;

import ma.enset.billingservice.model.Product;
import org.springframework.stereotype.Component;

// Réponse par défaut quand inventory-service est indisponible
@Component
public class ProductRestClientFallback implements ProductRestClient {
    @Override
    public Product getProductById(Long id) {
        return new Product(id, "Non disponible", 0, 0);
    }
}
