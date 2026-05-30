# Missing items and next steps

## Missing or open items
- Legacy packages still exist (for example, com.example.demo.agency.entity and com.example.demo.user.enums). Decide whether to remove or fully migrate them to models.
- Database schema must include new columns for `inscription_date`, `email_verified_at`, `licence_number`, `approved_at`, `approved_by_admin_id`, and `rejection_reason`.
- Data that still uses the `ADMIN` role must be migrated to `SUPER_ADMIN` to match the models enum.
- Some legacy entities still use UUID IDs. If they are still referenced, align them to `String` IDs or remove them.

## Next steps
1. Decide on a single source of truth for entities/enums and remove unused packages.
2. Add or update migrations to include the new columns in users and agency managers tables.
3. Run the tests and exercise the login, registration, and admin flows.
4. Add integration tests for JWT auth and role based access if needed.
