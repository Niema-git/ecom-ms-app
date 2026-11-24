# ecom-ms-app

Application de gestion des factures (clients, produits, factures) basée sur une architecture micro-services avec Spring Boot et Spring Cloud.

## Services

| Service | Port | Rôle |
|---|---|---|
| customer-service | 8081 | Gestion des clients |
| inventory-service | 8082 | Gestion des produits |
| gateway-service | 8888 | Point d'entrée unique (Spring Cloud Gateway) |
