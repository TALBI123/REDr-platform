# Guide des endpoints (tests avec curl)

## Base URL
- http://localhost:8080

## Comptes de demo (DemoApplication)
- SUPER_ADMIN: email `admin@demo.com`, password `admin123`
- AGENCY_MANAGER: email `manager@demo.com`, password `manager123`
- CLIENT: email `client@demo.com`, password `client123`

## Workflow d'authentification (cookie JWT)
1. Login via `/auth/login`.
2. Recuperer le cookie `ACCESS_TOKEN`.
3. Reutiliser ce cookie pour les endpoints proteges.

Exemple login avec cookie:
```bash
curl -i -c cookies.txt \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@demo.com","password":"admin123"}' \
  http://localhost:8080/auth/login
```
Ensuite appeler un endpoint protege:
```bash
curl -i -b cookies.txt http://localhost:8080/test
```

---

## /auth/login (POST)
**But**: Authentifier un utilisateur et recevoir un cookie JWT.

**Body** (LoginRequest):
```json
{
  "email": "client@demo.com",
  "password": "client123"
}
```

**curl**:
```bash
curl -i -c cookies.txt \
  -H "Content-Type: application/json" \
  -d '{"email":"client@demo.com","password":"client123"}' \
  http://localhost:8080/auth/login
```

**Workflow**:
- Valide les champs.
- Verifie l'etat du compte (lock).
- Authentifie via Spring Security.
- Genere le JWT et le place dans `ACCESS_TOKEN`.

**A tester**:
- 200 OK avec cookie.
- 401 si mot de passe faux.
- 423 (ou 401) si compte locked.

---

## /auth/logout (POST)
**But**: Supprimer le cookie JWT.

**curl**:
```bash
curl -i -b cookies.txt http://localhost:8080/auth/logout
```

**A tester**:
- Cookie `ACCESS_TOKEN` vide.

---

## /auth/register/client (POST)
**But**: Creer un client.

**Body** (RegisterRequest):
```json
{
  "email": "client.new@example.com",
  "password": "password123",
  "firstName": "New",
  "lastName": "Client",
  "phone": "0600000000",
  "licenceNumber": "LIC-123",
  "passportNumber": "PASS-456",
  "location": "Rabat"
}
```

**curl**:
```bash
curl -i \
  -H "Content-Type: application/json" \
  -d '{"email":"client.new@example.com","password":"password123","firstName":"New","lastName":"Client","phone":"0600000000","licenceNumber":"LIC-123","passportNumber":"PASS-456","location":"Rabat"}' \
  http://localhost:8080/auth/register/client
```

**Workflow**:
- Cree un client avec `PENDING`.
- Envoie un email de verification.

**A tester**:
- 201 Created.
- Le compte ne peut pas se connecter avant verification email.

---

## /auth/register/agency (POST)
**But**: Creer une agence + un manager.

**Body** (RegisterAgencyManagerRequest):
```json
{
  "email": "manager.new@example.com",
  "password": "password123",
  "firstName": "New",
  "lastName": "Manager",
  "phone": "0611111111",
  "licenceNumber": "LIC-MAN-001",
  "nationalId": "NID-001",
  "agencyName": "New Agency",
  "agencyCity": "Casa",
  "agencyPhone": "0522000000",
  "agencyEmail": "agency.new@example.com",
  "agencyAddress": "10 Avenue",
  "agencyDescription": "Agence test"
}
```

**curl**:
```bash
curl -i \
  -H "Content-Type: application/json" \
  -d '{"email":"manager.new@example.com","password":"password123","firstName":"New","lastName":"Manager","phone":"0611111111","licenceNumber":"LIC-MAN-001","nationalId":"NID-001","agencyName":"New Agency","agencyCity":"Casa","agencyPhone":"0522000000","agencyEmail":"agency.new@example.com","agencyAddress":"10 Avenue","agencyDescription":"Agence test"}' \
  http://localhost:8080/auth/register/agency
```

**Workflow**:
- Cree une agence avec status `PENDING`.
- Cree un manager avec accountStatus `PENDING`.
- Envoie un email de verification.
- Le manager doit verifier email, puis etre approuve par SUPER_ADMIN.

**A tester**:
- 201 Created.
- Le manager ne peut pas se connecter avant verification + approbation.

---

## /verification/verify-email (GET)
**But**: Verifier un email avec un token.

**Query**:
- `token`: token recu par email

**curl**:
```bash
curl -i "http://localhost:8080/verification/verify-email?token=TOKEN_ICI"
```

**Workflow**:
- Verifie token (existant, pas expire, pas utilise).
- Marque email comme verifie.
- Active le compte si role CLIENT.

**A tester**:
- 200 OK si token valide.
- 400 si token expire ou deja utilise.

---

## /verification/resend (POST)
**But**: Renvoyer un email de verification.

**Body** (ResendVerificationRequest):
```json
{
  "email": "client.new@example.com"
}
```

**curl**:
```bash
curl -i \
  -H "Content-Type: application/json" \
  -d '{"email":"client.new@example.com"}' \
  http://localhost:8080/verification/resend
```

**A tester**:
- 200 OK et email renvoye.

---

## /agencies (GET)
**Auth**: requis (cookie JWT)

**curl**:
```bash
curl -i -b cookies.txt http://localhost:8080/agencies
```

**Workflow**:
- Retourne une reponse simple (placeholder).

**A tester**:
- 200 OK si authentifie.
- 401 si non authentifie.

---

## /agencies/{agencyId} (GET)
**Auth**: SUPER_ADMIN ou AGENCY_MANAGER

**curl**:
```bash
curl -i -b cookies.txt http://localhost:8080/agencies/AGENCY_ID
```

**Workflow**:
- Verifie role.
- Si manager, verifie que l'agence correspond a son agence.

**A tester**:
- 200 OK si SUPER_ADMIN.
- 403 si manager tente une autre agence.

---

## /bookings/my (GET)
**Auth**: CLIENT ou SUPER_ADMIN

**curl**:
```bash
curl -i -b cookies.txt http://localhost:8080/bookings/my
```

**Workflow**:
- Utilise l'ID du user courant.

**A tester**:
- 200 OK avec client.
- 403 si role AGENCY_MANAGER.

---

## /admin/users (GET)
**Auth**: SUPER_ADMIN

**curl**:
```bash
curl -i -b cookies.txt http://localhost:8080/admin/users
```

**A tester**:
- 200 OK avec SUPER_ADMIN.
- 403 avec autres roles.

---

## /admin/agencies (GET)
**Auth**: SUPER_ADMIN

**curl**:
```bash
curl -i -b cookies.txt http://localhost:8080/admin/agencies
```

---

## /admin/agencies/pending (GET)
**Auth**: SUPER_ADMIN

**curl**:
```bash
curl -i -b cookies.txt http://localhost:8080/admin/agencies/pending
```

**Workflow**:
- Retourne les agences en `PENDING`.

**A tester**:
- Apres `/auth/register/agency`, l'agence doit apparaitre ici.

---

## /admin/agencies/{agencyId}/approve (POST)
**Auth**: SUPER_ADMIN

**Body** (ApproveAgencyRequest):
```json
{
  "comment": "OK"
}
```

**curl**:
```bash
curl -i -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{"comment":"OK"}' \
  http://localhost:8080/admin/agencies/AGENCY_ID/approve
```

**Workflow**:
- Met l'agence en APPROVED.
- Active les managers verifies (emailVerifiedAt != null).

**A tester**:
- Manager peut se connecter apres verification + approbation.

---

## /admin/agencies/{agencyId}/reject (POST)
**Auth**: SUPER_ADMIN

**Body** (RejectAgencyRequest):
```json
{
  "reason": "Non conforme"
}
```

**curl**:
```bash
curl -i -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{"reason":"Non conforme"}' \
  http://localhost:8080/admin/agencies/AGENCY_ID/reject
```

---

## /admin/agencies/{agencyId}/suspend (POST)
**Auth**: SUPER_ADMIN

**Body** (RejectAgencyRequest):
```json
{
  "reason": "Incident"
}
```

**curl**:
```bash
curl -i -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{"reason":"Incident"}' \
  http://localhost:8080/admin/agencies/AGENCY_ID/suspend
```

---

## /test (GET)
**Auth**: requis

**curl**:
```bash
curl -i -b cookies.txt http://localhost:8080/test
```

**A tester**:
- Retourne "Authenticated as: <email>".

---

## Checklist de validation
- Login pour SUPER_ADMIN, AGENCY_MANAGER, CLIENT.
- Register client + verification email -> login OK.
- Register agency manager -> verification email -> approbation -> login OK.
- Roles bloquent correctement les endpoints admin.
- Acces agence: manager seulement sur sa propre agence.
- Token expire/refuse correctement si cookie invalide.
