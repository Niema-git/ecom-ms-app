# ecom-ms-app — Application micro-services de facturation

Application qui gère des **factures** contenant des **produits** et appartenant à des **clients**, construite avec une architecture micro-services (Spring Boot, Spring Cloud) et un client Angular.

## Architecture

```
                         ┌──────────────────────┐
  Angular (4200) ──────► │  Gateway (8888)      │
                         │  Spring Cloud Gateway│
                         └──────────┬───────────┘
                                    │ routes dynamiques (lb://)
          ┌─────────────────────────┼─────────────────────────┐
          ▼                         ▼                         ▼
 customer-service (8081)   inventory-service (8082)   billing-service (8083)
                                                         │  OpenFeign
                                                         └──► customer + inventory

 discovery-service (8761) : annuaire Eureka — tous les services s'y enregistrent
 config-service (9999)    : configuration centralisée lue depuis config-repo (GitHub)
```

| Service | Port | Rôle |
|---|---|---|
| customer-service | 8081 | Gestion des clients |
| inventory-service | 8082 | Gestion des produits |
| billing-service | 8083 | Gestion des factures (OpenFeign + circuit breaker) |
| gateway-service | 8888 | Point d'entrée unique (Spring Cloud Gateway) |
| discovery-service | 8761 | Annuaire Eureka |
| config-service | 9999 | Configuration centralisée (Spring Cloud Config) |
| ecom-web-app | 4200 | Client Angular |

**Technologies :** Spring Boot 4, Spring Cloud 2025, Spring Data JPA / REST, H2, Eureka, Spring Cloud Gateway, OpenFeign, Resilience4j, Spring Cloud Config, Angular 22.

## Lancement

1. `discovery-service`
2. `config-service`
3. `customer-service`, `inventory-service`
4. `billing-service`
5. `gateway-service`
6. Client Angular : `cd ecom-web-app && npm install && npx ng serve`

---

## 1. Customer-service

Entité `Customer` (id, name, email), `CustomerRepository` exposé en REST avec Spring Data REST, base H2.

`GET http://localhost:8081/customers`

![customers](captures/01-customers.png)

Table CUSTOMER dans la console H2 :

![h2 customers](captures/02-h2-customers.png)

## 2. Inventory-service

Entité `Product` (id, name, price, quantity), `ProductRepository` exposé en REST.

`GET http://localhost:8082/products`

![products](captures/03-products.png)

![h2 products](captures/04-h2-products.png)

## 3. Gateway (Spring Cloud Gateway)

Gateway réactive (WebFlux / Netty) sur le port 8888.

![gateway health](captures/05-gateway-health.png)

## 4. Configuration statique des routes

Routes déclarées dans `application.yml` vers des adresses fixes (`http://localhost:8081`, `http://localhost:8082`).

`GET http://localhost:8888/customers`

![static customers](captures/06-static-route-customers.png)

`GET http://localhost:8888/products`

![static products](captures/07-static-route-products.png)

**Limite :** les adresses sont écrites en dur ; un changement de port ou plusieurs instances imposent de modifier la configuration.

## 5. Annuaire Eureka (Discovery Service)

Serveur Eureka (`@EnableEurekaServer`) sur le port 8761.

![eureka vide](captures/08-eureka-vide.png)

Après l'ajout du client Eureka dans les services, ils s'enregistrent automatiquement :

![eureka services](captures/09-eureka-services.png)

Les routes utilisent alors le nom du service (`lb://CUSTOMER-SERVICE`) au lieu d'une adresse fixe :

![gateway eureka](captures/10-gateway-eureka-customers.png)

## 6. Configuration dynamique des routes

Bean `DiscoveryClientRouteDefinitionLocator` : la gateway crée une route pour chaque service enregistré dans Eureka. URL : `http://localhost:8888/NOM-DU-SERVICE/...`

`GET http://localhost:8888/CUSTOMER-SERVICE/customers`

![dynamic customers](captures/11-dynamic-route-customers.png)

`GET http://localhost:8888/INVENTORY-SERVICE/products`

![dynamic products](captures/12-dynamic-route-products.png)

## 7. Billing-service avec OpenFeign

Entités `Bill` et `ProductItem` ; une facture ne stocke que les identifiants du client et des produits.

`GET http://localhost:8888/BILLING-SERVICE/bills`

![bills](captures/13-bills.png)

![h2 product items](captures/14-h2-product-items.png)

Les clients OpenFeign `CustomerRestClient` et `ProductRestClient` récupèrent le client et les produits auprès des autres micro-services (via Eureka) :

`GET http://localhost:8888/BILLING-SERVICE/api/bills/1`

![bill feign](captures/15-bill-feign.png)

### Circuit breaker (Resilience4j)

Chaque client Feign a un *fallback*. Si customer-service est arrêté, la facture reste disponible avec des valeurs par défaut au lieu d'une erreur 500 :

![circuit breaker](captures/16-circuit-breaker.png)

## 8. Service de configuration (Spring Cloud Config)

Config Server (`@EnableConfigServer`) qui lit les fichiers du dossier `config-repo` de ce dépôt GitHub.

`GET http://localhost:9999/customer-service/default`

![config default](captures/17-config-default.png)

`GET http://localhost:9999/customer-service/dev`

![config dev](captures/18-config-dev.png)

customer-service récupère sa configuration au démarrage (`spring.config.import=optional:configserver:http://localhost:9999`) :

`GET http://localhost:8081/testConfig1` (paramètres globaux, `@Value`)

![test config 1](captures/19-test-config1.png)

`GET http://localhost:8081/testConfig2` (paramètres du service, `@ConfigurationProperties`)

![test config 2](captures/20-test-config2.png)

**Rafraîchissement à chaud :** après modification de `p1` sur GitHub puis `POST /actuator/refresh`, la nouvelle valeur est prise en compte sans redémarrer le service (`@RefreshScope`) :

![refresh](captures/21-refresh-config.png)

## 9. Client Angular

Application Angular qui consomme les micro-services à travers la gateway (CORS autorisé sur la gateway pour `http://localhost:4200`).

Liste des produits :

![angular products](captures/22-angular-products.png)

Liste des clients :

![angular customers](captures/23-angular-customers.png)

Factures d'un client :

![angular bills](captures/24-angular-bills.png)

Détail d'une facture :

![angular bill details](captures/25-angular-bill-details.png)
