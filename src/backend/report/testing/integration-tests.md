# Integration tests des endpoints

## Objectif
Valider chaque endpoint liste dans [report/testing/endpoints.md](report/testing/endpoints.md) avec `MockMvc`, en verifiant:
- les codes HTTP attendus
- les roles et l'authentification
- les appels aux services mocks

## Fichier de tests
- [src/test/java/com/example/demo/EndpointIntegrationTests.java](src/test/java/com/example/demo/EndpointIntegrationTests.java)

## Annotations utilisees
- `@SpringBootTest`: charge tout le contexte Spring Boot.
- `@AutoConfigureMockMvc`: active `MockMvc` avec les filtres de securite.
- `@MockBean`: remplace les services par des mocks Mockito.
- `@BeforeEach`: prepare les donnees de test (users, agencies).
- `@Test`: chaque scenario de test.

## Fonctions utilitaires
- `authFor(AppUser user)`: construit un `Authentication` avec `CustomUserDetails` et les authorities.
- `setUp()`: cree les objets `Admin`, `AgencyManager`, `Client` et les agences.

## Endpoints couverts
### Auth
- `/auth/login` (POST)
  - verifie le status 200
  - verifie que `Set-Cookie` contient `ACCESS_TOKEN`
- `/auth/logout` (POST)
  - verifie le status 200
  - verifie que le cookie est efface
- `/auth/register/client` (POST)
  - verifie le status 201
- `/auth/register/agency` (POST)
  - verifie le status 201

### Verification
- `/verification/verify-email` (GET)
  - verifie le status 200
  - verifie que `EmailVerificationService.verifyEmail` est appele
- `/verification/resend` (POST)
  - verifie le status 200
  - verifie que `EmailVerificationService.sendVerificationEmail` est appele

### Agencies
- `/agencies` (GET)
  - 401 sans auth
  - 200 avec un client authentifie
- `/agencies/{agencyId}` (GET)
  - 200 avec SUPER_ADMIN
  - 200 avec AGENCY_MANAGER de la meme agence
  - 403 avec AGENCY_MANAGER d'une autre agence
  - 403 avec CLIENT

### Bookings
- `/bookings/my` (GET)
  - 200 avec CLIENT
  - 200 avec SUPER_ADMIN
  - 403 avec AGENCY_MANAGER

### Admin
- `/admin/users` (GET)
  - 200 avec SUPER_ADMIN
  - 403 avec CLIENT
- `/admin/agencies` (GET)
  - 200 avec SUPER_ADMIN
- `/admin/agencies/pending` (GET)
  - 200 avec SUPER_ADMIN
- `/admin/agencies/{agencyId}/approve` (POST)
  - 200 avec SUPER_ADMIN
- `/admin/agencies/{agencyId}/reject` (POST)
  - 200 avec SUPER_ADMIN, raison >= 20 caracteres
- `/admin/agencies/{agencyId}/suspend` (POST)
  - 200 avec SUPER_ADMIN, raison >= 20 caracteres

### Test
- `/test` (GET)
  - 200 avec auth, verifie le message "Authenticated as:"

## Mocks utilises
- `AuthService`
- `RegistrationService`
- `EmailVerificationService`
- `AgencyAdminService`

Les mocks evitent d'appeler la base de donnees et permettent de tester les endpoints et la securite.

## Execution
```bash
./gradlew.bat test
```

## Notes
- `RejectAgencyRequest` impose une longueur minimale de 20 caracteres.
- Les roles utilises sont `SUPER_ADMIN`, `AGENCY_MANAGER`, `CLIENT`.
- Les tests utilisent `CustomUserDetails` pour simuler l'authentification.
