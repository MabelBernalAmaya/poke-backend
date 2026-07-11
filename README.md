# Poke Backend — API REST Pokédex (DOSW)

Este es el backend de mi proyecto de Pokédex para el curso de DOSW. Es una API REST que maneja el catálogo de Pokémon, el login de usuarios (con JWT y también con Google), los favoritos de cada usuario y un "Team Builder" para armar equipos. El frontend es aparte, un prototipo hecho en Figma/Loveable, no está en este repo.

**Producción:** `https://poke-backend-production-0b04.up.railway.app`
**Swagger UI (ya autenticado con Google, sin copiar tokens a mano):** `https://poke-backend-production-0b04.up.railway.app/auth-swagger.html`

## Tabla de contenido

- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Estructura de paquetes](#estructura-de-paquetes)
- [Diagramas](#diagramas)
- [Seguridad](#seguridad)
- [Persistencia](#persistencia)
- [Cómo ejecutar en local](#cómo-ejecutar-en-local)
- [Variables de entorno](#variables-de-entorno)
- [Testing y CI/CD](#testing-y-cicd)
- [Despliegue](#despliegue)
- [Documentación relacionada](#documentación-relacionada)

## Tecnologías

Estas son las tecnologías principales que usé:

| Categoría | Tecnología |
|---|---|
| Lenguaje / runtime | Java 21 |
| Framework | Spring Boot 3.3.4 |
| Base de datos relacional | PostgreSQL + Spring Data JPA |
| Base de datos no relacional | MongoDB (para registrar las vistas de Pokémon) |
| Migraciones de base de datos | Flyway |
| Seguridad | Spring Security, JWT (jjwt), OAuth2 con Google |
| Documentación de la API | springdoc-openapi / Swagger UI |
| Mapeo entre objetos | MapStruct |
| Cobertura de pruebas | JaCoCo (pido mínimo 70% de líneas cubiertas) |
| CI/CD | GitHub Actions |
| Despliegue | Railway |

## Arquitectura

Usé arquitectura hexagonal (puertos y adaptadores) y, dentro de la capa que recibe las peticiones HTTP, apliqué también el patrón MVC. La idea de hexagonal es que el dominio (o sea, las reglas de negocio) no dependa de Spring ni de JPA ni de nada de eso. El core no sabe que existe una base de datos: solo declara qué necesita (eso es un puerto), y quien se encarga de conectarse a la base de datos real es un adaptador aparte.

El proyecto quedó dividido en tres capas principales:

- **`core`**: es el dominio. Acá están los modelos de negocio (`Pokemon`, `PokemonStats`), los servicios con la lógica (`PokemonService`, `AuthService`, `FavoritoService`, `TeamService`) y los puertos, que son interfaces que dicen qué necesita el dominio sin decir cómo se hace (`PokemonPersistencePort`). En este paquete no hay nada de JPA ni de Mongo importado directamente.

- **`persistence`**: son los adaptadores de salida. Acá implementé los puertos usando JPA (`PokemonPersistenceAdapter`), que convierte entre las entidades de la base de datos (`PokemonEntity`, `UserEntity`, etc.) y los modelos del dominio, usando mappers. También está la entidad de MongoDB (`PokemonViewDocument`).

- **`controller`**: son los adaptadores de entrada, la parte que expone la API REST. Cada recurso tiene una interfaz (`PokemonApi`, `AuthApi`, `FavoritoApi`, `TeamApi`) que define el contrato con las anotaciones de Swagger, y una clase que la implementa (`PokemonController`, etc.). Acá es donde entra el MVC: el *Controller* recibe la petición, llama al *Service* (que sería el *Model*) y devuelve un DTO en JSON. No hay *Vista* como tal porque esto es una API y no una app con HTML — la "vista" la pone el prototipo de Figma, que es quien consume esta API.

Algo que vale la pena aclarar si el profe pregunta por qué no todo está igual de "hexagonal": el módulo de **Pokémon** es el único que tiene el patrón completo de puerto + adaptador, porque tiene más lógica (filtros, comparaciones, ordenamientos) y valía la pena separarla de JPA. Los módulos de **Favoritos**, **Equipos (Teams)** y **Auth** son CRUD bastante simples de una sola tabla, así que ahí los servicios usan directamente el repositorio de Spring Data JPA, sin un puerto intermedio — meter una interfaz de más ahí habría sido sobre-ingeniería sin ganar nada real. De todos modos, en los tres casos se respeta la separación Controller → Service → Repository.

Aparte de estas tres capas está `security`, con el filtro que valida el JWT en cada petición (`JwtAuthFilter`), el manejador que genera el token cuando alguien entra con Google (`OAuth2SuccessHandler`) y la configuración general de Spring Security (`SecurityConfig`), donde se define qué rutas son públicas, cuáles necesitan un rol específico y cómo funciona CORS.

## Estructura de paquetes

```
com.pokepedia.pokecore
├── core
│   ├── model        (Pokemon, PokemonStats — dominio puro)
│   ├── port          (PokemonPersistencePort — lo que el dominio necesita de afuera)
│   ├── service        (lógica de negocio: Pokemon, Auth, Favorito, Team, PasswordReset)
│   └── exception       (excepciones de negocio)
├── persistence
│   ├── entity/relational  (JPA: PokemonEntity, UserEntity, EquipoEntity, ...)
│   ├── entity/document   (MongoDB: PokemonViewDocument)
│   ├── repository       (Spring Data JPA / MongoRepository)
│   ├── mapper          (Entity ↔ modelo de dominio)
│   └── adapter        (implementación de los puertos, ej. PokemonPersistenceAdapter)
├── controller
│   ├── api           (interfaces del contrato REST + anotaciones de OpenAPI)
│   ├── impl          (implementaciones — los "Controllers" en sentido MVC)
│   ├── dto/request y dto/response (el "Model" que viaja por HTTP)
│   ├── mapper         (Domain ↔ DTO)
│   └── handler         (manejo global de excepciones)
├── security          (JWT, OAuth2, filtros, configuración de Spring Security)
└── config           (OpenApiConfig — configuración de Swagger)
```

## Diagramas

Estos son los diagramas exportados como imagen (armados en Lucidchart y ):

### Diagrama de clases

Clases principales del módulo Pokémon (Controller → Service → Puerto → Adaptador → Entidad) y las relaciones del modelo de datos con usuarios, equipos y favoritos.

![Diagrama de clases](https://github.com/MabelBernalAmaya/poke-backend/blob/main/docs/diagramas/diagrama-clases.png)

Acá se ve la cadena completa del módulo Pokémon: `PokemonController` usa `PokemonService`, `PokemonServiceImpl` implementa esa interfaz (realización, flecha con triángulo hueco) y a su vez usa el puerto `PokemonPersistencePort`, que `PokemonPersistenceAdapter` implementa para hablar con `PokemonEntity` en la base de datos. También quedan las clases de `Pokemon` y `PokemonStats` (el modelo de dominio) y cómo se relacionan con `UserEntity`, `EquipoEntity` y `FavoritoEntity`, que son las que soportan equipos y favoritos.

### Diagrama de componentes general

Vista de alto nivel: cómo se conectan el cliente, las capas del backend (Controller, Core, Persistencia, Seguridad) y la infraestructura externa (PostgreSQL, MongoDB, Google OAuth2).

![Diagrama de componentes general](https://github.com/MabelBernalAmaya/poke-backend/blob/main/docs/diagramas/componentes-general.png)

Este es el panorama completo desde afuera: el cliente (Swagger o el prototipo del frontend) le pega al backend, que por dentro está dividido en las capas que mencioné en la sección de arquitectura (Controller, Core, Persistencia y Security). De ahí para afuera, el backend se conecta a la infraestructura externa: PostgreSQL para los datos relacionales, MongoDB para las vistas de Pokémon, y Google OAuth2 para el login. La idea de este diagrama es mostrar que el backend es el único que le habla directamente a esos tres servicios externos.

### Diagrama de componentes específico

Detalle interno de los 4 módulos funcionales (Pokémon, Auth, Favoritos, Teams) y la única dependencia entre ellos: Teams necesita los datos de Pokémon para exportar un equipo a texto.

![Diagrama de componentes específico](https://github.com/MabelBernalAmaya/poke-backend/blob/main/docs/diagramas/componentes-especifico.png)

Acá ya entro al detalle de adentro del backend, módulo por módulo: Pokémon, Auth, Favoritos y Teams, cada uno con su Controller, su Service y su capa de persistencia. Casi todos son independientes entre sí, menos Teams, que sí necesita pedirle datos a Pokémon para poder armar el equipo y exportarlo a texto (por eso es la única flecha que cruza entre módulos). Esto refuerza lo que expliqué en arquitectura: solo Pokémon tiene puerto/adaptador completo, los demás usan su repositorio de JPA directo.

### Diagramas de casos de uso

Uno por cada tipo de usuario, para que se entienda más claro qué puede hacer cada uno.

**Visitante**

![Caso de uso - Visitante](https://github.com/MabelBernalAmaya/poke-backend/blob/main/docs/diagramas/Diagrama%20visitante.png)

El visitante es cualquiera que entra sin loguearse. Solo puede hacer operaciones de lectura sobre el catálogo: ver el listado de Pokémon, buscar por nombre o id, filtrar (simple y avanzado) y comparar dos Pokémon. No tiene acceso a favoritos, equipos ni nada que necesite una cuenta.

**Entrenador autenticado**

![Caso de uso - Entrenador autenticado](https://github.com/MabelBernalAmaya/poke-backend/blob/main/docs/diagramas/diagrama%20entrenador.png)

El entrenador es el usuario ya logueado (con email/password o con Google), rol `TRAINER` por defecto. Además de todo lo que puede hacer el visitante, puede agregar y quitar favoritos, y crear, editar, eliminar y exportar sus propios equipos en el Team Builder. Lo que no puede es tocar el catálogo de Pokémon (crear/editar/eliminar un Pokémon), porque esos endpoints están protegidos para ADMIN.

**Administrador**

![Caso de uso - Administrador](https://github.com/MabelBernalAmaya/poke-backend/blob/main/docs/diagramas/diagrama%20admin.png)

El administrador tiene todo lo del entrenador, más los casos de uso exclusivos de gestión del catálogo: crear, editar y eliminar Pokémon. Es el único rol que puede modificar los datos que ven todos los demás usuarios.

> El diagrama C4 se documenta en el otro repositorio (el del prototipo / manual de identidad), junto con el resto de artefactos de diseño.

## Seguridad

- **Registro/login con email y contraseña:** `POST /v1/auth/register` y `POST /v1/auth/login` devuelven un JWT.
- **Login con Google (OAuth2):** al elegir la cuenta de Google, el backend crea (o busca) el usuario, genera un JWT y redirige automáticamente a `/auth-swagger.html#token=...`, una página que carga Swagger UI y se autentica sola con ese token. No hay que copiar nada a mano.
- **Roles:** `TRAINER` (el que se asigna por defecto) y `ADMIN`. Los endpoints para crear, editar o borrar Pokémon están protegidos con `@PreAuthorize("hasRole('ADMIN')")`.
- **JWT:** se valida en cada petición con `JwtAuthFilter`, antes del filtro normal de autenticación de Spring Security.
- **CORS:** los orígenes permitidos se configuran por variable de entorno (`app.cors.allowed-origins`), para que el prototipo del frontend pueda consumir la API desde otro dominio.

## Persistencia

- **PostgreSQL** guarda el catálogo (Pokémon, tipos, regiones, estadísticas), los usuarios, los favoritos y los equipos. El esquema se versiona con **Flyway** (`src/main/resources/db/migration`).
- **MongoDB** registra las vistas de cada Pokémon (`pokemon_views`), como ejemplo de persistencia no relacional para algo de analítica simple.

## Cómo ejecutar en local

```bash
# 1. Levantar Postgres y MongoDB
docker-compose up -d

# 2. Configurar las variables de entorno (ver sección de abajo)

# 3. Ejecutar la aplicación
./mvnw spring-boot:run
```

Con la app corriendo:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health check: `http://localhost:8080/actuator/health`

## Variables de entorno

| Variable | Para qué es | Valor por defecto (local) |
|---|---|---|
| `PORT` | Puerto del servidor | `8080` |
| `SPRING_DATASOURCE_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://localhost:5432/pokedb` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de PostgreSQL | `pokeuser` |
| `SPRING_DATASOURCE_PASSWORD` | Password de PostgreSQL | `pokepassword` |
| `SPRING_DATA_MONGODB_URI` | URI de MongoDB | `mongodb://localhost:27017/pokelogs` |
| `JWT_SECRET` | Clave para firmar los JWT | *(no tiene default, hay que ponerla)* |
| `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` | Credenciales de OAuth2 desde Google Cloud Console | *(no tiene default, hay que ponerla)* |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos, separados por coma | `http://localhost:3000` |

## Testing y CI/CD

GitHub Actions (`.github/workflows/ci.yml`) corre en cada push o PR contra `develop` y `main`:

1. Levanta Postgres 15 y Mongo 6 como contenedores de servicio.
2. Compila y corre las pruebas con `mvn clean verify`.
3. Revisa la cobertura con **JaCoCo** (mínimo 70% de líneas).
4. Sube el reporte de JaCoCo como artefacto descargable.

## Despliegue

Desplegado en **Railway**: `https://poke-backend-production-0b04.up.railway.app`

Ahí corren tres servicios: la app de Spring Boot, un plugin de PostgreSQL y un plugin de MongoDB, conectados entre sí con variables de servicio de Railway. El healthcheck usa `/actuator/health`.

## Documentación relacionada

- Prototipo de frontend (Figma / Loveable) y diagrama C4: repositorio de identidad de producto _(agregar link)_.
- Documentación interactiva de la API: Swagger UI en `/swagger-ui.html` (o `/auth-swagger.html` para entrar ya autenticado con Google).
