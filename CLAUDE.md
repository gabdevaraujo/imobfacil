# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ImobfacilApplicationTests

# Build (package as WAR)
./mvnw clean package

# Clean build artifacts
./mvnw clean
```

The app runs on `http://localhost:8080/api`. H2 console is at `http://localhost:8080/api/h2-console` (username: `sa`, password: blank, JDBC URL: `jdbc:h2:mem:imobfacildb`).

## Architecture

Spring Boot 4 REST API following MVC + layered architecture:

```
Controller → Service → Repository → JPA/Hibernate → H2 (dev) / PostgreSQL (prod)
```

**Package layout** (`br.com.gva.imobfacil`):
- `model/` — JPA entities (`@Entity`)
- `dto/` — API request/response shapes (separate from entities)
- `repository/` — Spring Data JPA interfaces
- `service/` — Business logic; controllers call services, not repositories directly
- `controller/` — REST endpoints (`@RestController`)
- `config/` — Cross-cutting config (CORS)

**Central entity: `Imovel` (Property)**
- 1:1 → `Endereco` (address with coordinates)
- N:1 → `Corretor` (real estate agent)
- 1:N → `Imagem` (photos with ordering)
- N:N → `Caracteristica` (features like pool, AC)
- 1:N → `MensagemContato` (contact/lead messages)

**Enums:** `TipoNegocio` (VENDA, ALUGUEL, ALUGUEL_TEMPORADA), `StatusImovel` (DISPONIVEL, ALUGADO, VENDIDO, INDISPONIVEL, EM_CONSTRUCAO)

## Key Notes

- Use Lombok `@RequiredArgsConstructor` for constructor injection (already established pattern)
- `ImovelRepository` has custom `@Query` methods for complex property filtering (by price, bedrooms, location)
- `ddl-auto: create-drop` — schema is recreated on every startup in dev; no migrations needed for H2
- JWT dependency (JJWT 0.12.3) is in `pom.xml` but not yet integrated — authentication is the next major feature per the backlog
- CORS currently allows all origins (`*`) — restrict before production deployment
- Controllers return `ResponseEntity` wrapping DTOs, not entities directly
