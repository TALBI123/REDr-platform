# Spring Data JPA filtering and paging - detailed guide

Document version: 2026-05-31
Project: mdl-project backend
Scope: Agency search, Specification, Predicate, Pageable, and SQL generation
Audience: developers who need deep context and an end to end mental model
Goal: explain what happens from HTTP query params to final SQL and results

Table of contents
1. Scope and reading guide
2. Problem statement and why Specification exists
3. Key classes in this project
4. Pageable and Page explained
5. Specification explained
6. Predicate explained
7. Criteria API vocabulary
8. Where Specification lives in the codebase
9. Spring Data flow with JpaSpecificationExecutor
10. SQL generation pipeline (short)
11. SQL generation pipeline (detailed trace)
12. How filters map to SQL
13. Sorting and pagination details
14. Count query details
15. Performance and indexing notes
16. Validation and error behavior
17. Common pitfalls and fixes
18. Testing strategy
19. FAQ
20. Summary checklist

1. Scope and reading guide
This document is intentionally long and explicit.
Each topic is separated into short lines to make the flow easy to scan.
You can read only the sections you need.
If you only need quick usage, read sections 4, 5, and 9.
If you need to explain it to someone, read sections 2, 6, and 11.

2. Problem statement and why Specification exists
Spring Data repository methods are great for simple queries.
But optional filters create method explosion.
Example: findByNameAndCityAndStatusAndRatingRange.
Every optional field doubles the number of combinations.
This is the core problem that Specification solves.
Specification lets you build one query dynamically.
Only the filters that are present become predicates.
You avoid string concatenation and manual SQL.
You keep logic type safe and testable.
You reuse the same Specification in multiple services.
You can compose Specifications with and or.
You can keep filtering logic separate from controller logic.
This is why Specification was introduced in Spring Data JPA.

3. Key classes in this project
AgencySpecifications is the class that builds the Specification.
AgencySearchCriteria is the DTO that holds optional filters.
AgencyRepository extends JpaSpecificationExecutor to accept Specification.
AgencyService calls repository.findAll(spec, pageable).
AgencyPublicController and AgencyAdminController pass criteria and pageable.
AgencyManagerController uses CRUD for the manager scope.

4. Pageable and Page explained
Pageable is an interface that carries paging and sorting parameters.
Spring MVC builds Pageable from query params by default.
Common query params are page, size, and sort.
page is zero based.
size is the number of items per page.
sort is optional and can be repeated.
Example: sort=name,asc and sort=city,desc.
Pageable can be built manually with PageRequest.of.
Pageable can be configured with @PageableDefault on controller params.
You can cap page size using application properties if needed.
Page is a wrapper that includes content and metadata.
Page contains total elements and total pages.
Page contains the Pageable used for the query.
Page is different from Slice.
Slice does not compute total elements.
Slice avoids the count query.
Slice is useful when you do not need total pages.
List is just content and has no paging metadata.
If you always need totals, use Page.
If you need speed and do not care about totals, use Slice.
If you do not need paging, use List.

5. Specification explained
Specification is a functional interface in Spring Data JPA.
Package: org.springframework.data.jpa.domain.Specification.
It has one method: toPredicate.
The method receives Root, CriteriaQuery, and CriteriaBuilder.
You return a Predicate that represents the where clause.
Specification can be chained with and and or.
Specification.where is a helper to start a chain.
You can build small reusable specs and compose them.
Specification keeps query logic in Java, not in strings.
Specification is recognized by JpaSpecificationExecutor.
When you call repository.findAll(spec), the spec is used.
When you call repository.findAll(spec, pageable), it also applies paging.

6. Predicate explained
Predicate is a JPA Criteria API interface.
It represents a boolean expression used in queries.
Examples: name like, status equals, rating between.
CriteriaBuilder creates Predicate instances.
Predicates can be combined with builder.and and builder.or.
Predicates are type safe because they use typed paths.
Predicate is chosen because it is the standard JPA interface.
This keeps Spring Data compatible with any JPA provider.

7. Criteria API vocabulary
CriteriaBuilder builds criteria objects and predicates.
CriteriaQuery represents the full select query.
Root represents the main entity in the query.
Path is a typed access to a field on Root or Join.
Expression is a typed value in the query tree.
Predicate is a boolean expression.
Order represents sorting expressions.
ParameterExpression represents a parameter value in a query.
These objects form a tree that the provider converts to SQL.

8. Where Specification lives in the codebase
This project places Specifications under agency.repository.
This is a common practice because it is data access logic.
Another acceptable practice is agency.query or agency.spec.
Both are valid if your team keeps it consistent.
Specification should not be inside controller or DTO classes.
Specification should not contain web specific code.
Specification should only build query predicates.

9. Spring Data flow with JpaSpecificationExecutor
AgencyRepository extends JpaSpecificationExecutor.
JpaSpecificationExecutor is implemented by SimpleJpaRepository.
SimpleJpaRepository uses the EntityManager and Criteria API.
findAll(spec, pageable) builds a CriteriaQuery for data.
It also builds a CriteriaQuery for count.
The spec is applied to both queries.
Sort from Pageable is applied to the data query only.
Pagination is applied to the data query only.
Count query returns total elements for Page.

10. SQL generation pipeline (short)
Controller receives query params.
Spring binds params into AgencySearchCriteria and Pageable.
Service calls repository.findAll(spec, pageable).
Repository builds CriteriaQuery and applies spec to it.
Hibernate translates CriteriaQuery to SQL.
Database executes SQL and returns rows.
Rows are mapped to Agency entities.
Page is created with content and metadata.

11. SQL generation pipeline (detailed trace)
This trace is deliberately verbose.
It shows each step from HTTP to SQL.

Trace step 001 - HTTP request arrives at the controller.
Trace step 002 - Spring MVC parses query parameters.
Trace step 003 - Spring creates an AgencySearchCriteria instance.
Trace step 004 - Spring sets name if name param is present.
Trace step 005 - Spring sets city if city param is present.
Trace step 006 - Spring sets email if email param is present.
Trace step 007 - Spring sets phone if phone param is present.
Trace step 008 - Spring sets status if status param is present.
Trace step 009 - Spring sets minRating if minRating param is present.
Trace step 010 - Spring sets maxRating if maxRating param is present.
Trace step 011 - Spring sets approvalFrom if approvalFrom param is present.
Trace step 012 - Spring sets approvalTo if approvalTo param is present.
Trace step 013 - Spring sets inscriptionFrom if inscriptionFrom param is present.
Trace step 014 - Spring sets inscriptionTo if inscriptionTo param is present.
Trace step 015 - Spring creates a Pageable from page, size, and sort.
Trace step 016 - Controller calls AgencyService.searchAgencies.
Trace step 017 - Service calls AgencySpecifications.from(criteria).
Trace step 018 - Specification returns a lambda for toPredicate.
Trace step 019 - Service calls repository.findAll(spec, pageable).
Trace step 020 - SimpleJpaRepository creates a CriteriaBuilder.
Trace step 021 - SimpleJpaRepository creates a CriteriaQuery for Agency.
Trace step 022 - SimpleJpaRepository creates Root<Agency> root.
Trace step 023 - SimpleJpaRepository calls spec.toPredicate(root, query, builder).
Trace step 024 - Specification checks if criteria is null.
Trace step 025 - Specification creates a list of Predicate.
Trace step 026 - If name is set, it adds a like predicate on lower(name).
Trace step 027 - If city is set, it adds a like predicate on lower(city).
Trace step 028 - If email is set, it adds a like predicate on lower(email).
Trace step 029 - If phone is set, it adds a like predicate on lower(phone).
Trace step 030 - If status is set, it adds an equals predicate.
Trace step 031 - If minRating is set, it adds a >= predicate.
Trace step 032 - If maxRating is set, it adds a <= predicate.
Trace step 033 - If approvalFrom is set, it adds a >= date predicate.
Trace step 034 - If approvalTo is set, it adds a <= date predicate.
Trace step 035 - If inscriptionFrom is set, it adds a >= date predicate.
Trace step 036 - If inscriptionTo is set, it adds a <= date predicate.
Trace step 037 - Specification returns builder.and(allPredicates).
Trace step 038 - Repository applies predicate to CriteriaQuery.where.
Trace step 039 - Repository applies Sort from Pageable to the query.
Trace step 040 - Repository builds a TypedQuery from CriteriaQuery.
Trace step 041 - Repository applies firstResult based on page and size.
Trace step 042 - Repository applies maxResults equal to size.
Trace step 043 - Repository executes the data query.
Trace step 044 - JPA provider creates an internal query tree.
Trace step 045 - Hibernate converts CriteriaQuery to SQM model.
Trace step 046 - Hibernate converts SQM to SQL AST.
Trace step 047 - Hibernate produces SQL with parameters.
Trace step 048 - JDBC PreparedStatement is created.
Trace step 049 - Query parameters are bound to the PreparedStatement.
Trace step 050 - Database executes the SQL.
Trace step 051 - Database returns rows for the data query.
Trace step 052 - Hibernate maps rows to Agency entities.
Trace step 053 - Repository now needs total count for Page.
Trace step 054 - Repository builds a CriteriaQuery<Long> for count.
Trace step 055 - Repository uses the same Specification predicate.
Trace step 056 - Repository removes any sort for count query.
Trace step 057 - Repository executes the count query.
Trace step 058 - Hibernate builds SQL for count query.
Trace step 059 - JDBC executes count SQL.
Trace step 060 - Repository gets the total count value.
Trace step 061 - Repository creates a PageImpl with content and total.
Trace step 062 - Controller returns Page<AgencyPublicDTO> or Page<AgencyAdminDTO>.
Trace step 063 - JSON response is written.

Trace step 064 - Optional step when no criteria provided.
Trace step 065 - Specification returns builder.conjunction.
Trace step 066 - Conjunction means no filtering.
Trace step 067 - Query becomes select all with only sort and pagination.
Trace step 068 - Count query becomes select count(*) from agencies.

Trace step 069 - Optional step when only status is provided.
Trace step 070 - Query includes only where status = ?.
Trace step 071 - The rest of the predicate list is empty.
Trace step 072 - builder.and(singlePredicate) is the same predicate.

Trace step 073 - Optional step when name and city are provided.
Trace step 074 - Query includes lower(name) like ? and lower(city) like ?.
Trace step 075 - Both predicates are joined by AND.

Trace step 076 - Optional step when minRating and maxRating are provided.
Trace step 077 - Query includes rating >= ? and rating <= ?.

Trace step 078 - Optional step when only minRating is provided.
Trace step 079 - Query includes rating >= ? only.

Trace step 080 - Optional step when only maxRating is provided.
Trace step 081 - Query includes rating <= ? only.

Trace step 082 - Optional step when approval date range is used.
Trace step 083 - Query includes approval_date between ? and ?.
Trace step 084 - In code it is two predicates, not between.

Trace step 085 - Optional step when inscription date range is used.
Trace step 086 - Query includes inscription_date between ? and ?.
Trace step 087 - In code it is two predicates, not between.

Trace step 088 - Optional step when sort is provided.
Trace step 089 - Query adds order by clause from Pageable.
Trace step 090 - Sort can be multiple fields.

Trace step 091 - Optional step when sort is not provided.
Trace step 092 - Query has no order by.
Trace step 093 - Without order, paging can be unstable.

Trace step 094 - Optional step when page size is large.
Trace step 095 - Database may spend more time in data query.
Trace step 096 - Count query cost remains the same.

Trace step 097 - Optional step when there are no results.
Trace step 098 - Data query returns empty list.
Trace step 099 - Count query returns zero.
Trace step 100 - Page still reports pageable metadata.

Trace step 101 - Optional step when invalid criteria is provided.
Trace step 102 - Conversion errors can happen before reaching service.
Trace step 103 - Example: invalid date format for approvalFrom.
Trace step 104 - Spring returns 400 Bad Request for invalid conversion.

Trace step 105 - Optional step when status value is not in enum.
Trace step 106 - Spring fails to bind the enum.
Trace step 107 - Response is 400 Bad Request with error message.

Trace step 108 - Optional step when accessing public endpoints.
Trace step 109 - SecurityConfig allows GET /public/agencies/**.
Trace step 110 - Other endpoints require authentication.

Trace step 111 - Optional step when accessing manager endpoints.
Trace step 112 - SecurityUtils reads current agency id.
Trace step 113 - Manager gets only their own agency.

Trace step 114 - Optional step when accessing admin endpoints.
Trace step 115 - Admin can search and manage all agencies.

Trace step 116 - Optional step when using H2 console.
Trace step 117 - SecurityConfig allows /h2-console/**.
Trace step 118 - These routes are not related to Specification.

Trace step 119 - Optional step when entity has lazy relations.
Trace step 120 - Agency has lazy managers and cars.
Trace step 121 - Filtering does not fetch those relations.
Trace step 122 - DTO mapping avoids lazy loading in controllers.

12. How filters map to SQL
Filter name -> lower(agencies.name) like ?
Filter city -> lower(agencies.city) like ?
Filter email -> lower(agencies.email) like ?
Filter phone -> lower(agencies.phone) like ?
Filter status -> agencies.status = ?
Filter minRating -> agencies.rating >= ?
Filter maxRating -> agencies.rating <= ?
Filter approvalFrom -> agencies.approval_date >= ?
Filter approvalTo -> agencies.approval_date <= ?
Filter inscriptionFrom -> agencies.inscription_date >= ?
Filter inscriptionTo -> agencies.inscription_date <= ?
All filters are combined with AND in the current implementation.
If you need OR logic, you can build it with builder.or.
If you need exact match instead of like, use equal.
If you need case sensitive match, remove lower.

13. Sorting and pagination details
Sort from Pageable is applied before pagination.
If no sort is provided, database order is undefined.
Undefined order can lead to duplicate or missing records across pages.
Always provide a stable sort for paging.
A safe default sort is by id or created date.
Pageable size should be capped to prevent heavy queries.
Large page sizes can increase memory usage.
If results are large, consider using Slice.

14. Count query details
Page requires total elements.
Total elements are computed with a count query.
The count query uses the same predicate as the data query.
The count query removes sorting.
Count queries can be expensive on large tables.
If you do not need totals, use Slice to skip count.
Count queries do not include select fields, only count(*).

15. Performance and indexing notes
lower(field) like pattern can bypass normal indexes.
If this becomes slow, consider functional indexes.
PostgreSQL supports indexes on lower(field).
Alternatively, store normalized search fields.
Range filters on dates and rating use B tree indexes well.
Compound filtering can reduce index selectivity.
Use proper indexes on status and dates if frequent filters.

16. Validation and error behavior
Binding errors happen before the service is called.
Enum conversion errors produce 400 responses.
Date format errors produce 400 responses.
Null criteria is valid and means no filtering.
Empty string criteria are ignored via StringUtils.hasText.
Validation annotations on DTOs do not apply to query params.
If you need validation for query params, use @Validated and custom rules.

17. Common pitfalls and fixes
Pitfall: missing sort with paging.
Fix: add default sort in controller or service.
Pitfall: using like on numeric fields.
Fix: use numeric comparison for numbers.
Pitfall: mixing fetch joins with paging.
Fix: avoid fetch joins or use distinct and careful paging.
Pitfall: too many optional fields with null checks.
Fix: keep criteria minimal and use helper methods.
Pitfall: using Specification in controllers directly.
Fix: keep it in service layer for separation of concerns.

18. Testing strategy
Use @DataJpaTest for repository and Specification tests.
Seed database with a few agencies.
Test each filter independently.
Test combinations of filters.
Test paging behavior with stable sort.
Test count query correctness.
Use slice tests if you decide to switch from Page.
Use integration tests for controller binding of query params.

19. FAQ
Q: Why not use QueryDSL?
A: Specification is built in and does not require extra codegen.
Q: Can Specification return null?
A: It should return a Predicate, not null.
Q: Can I use OR logic?
A: Yes, use builder.or to combine predicates.
Q: Does Specification allow joins?
A: Yes, you can create joins on root.
Q: Why do we use lower()?
A: For case insensitive matching.
Q: Who implements Predicate?
A: The JPA provider, Hibernate in this project.
Q: Does Spring Data implement Predicate?
A: No, Spring Data only calls CriteriaBuilder.
Q: Where does CriteriaBuilder come from?
A: It comes from EntityManager.getCriteriaBuilder().
Q: Can I see the SQL?
A: Enable Hibernate SQL logging in application.yml.

20. Summary checklist
Use Specification for dynamic optional filters.
Use Pageable for pagination and sorting.
Always sort when using pagination.
Understand that Page triggers count query.
Know that Predicate is provider specific.
Keep filtering in repository or query package.
Test filters, paging, and count behavior.

Appendix A - extended SQL generation trace
Appendix step 201 - EntityManager obtains a Hibernate Session.
Appendix step 202 - CriteriaQuery is translated to SQM tree.
Appendix step 203 - SQM tree represents the semantic query model.
Appendix step 204 - SQM tree is validated by Hibernate.
Appendix step 205 - Hibernate builds SQL AST nodes.
Appendix step 206 - SQL AST includes select, from, where, order, limit.
Appendix step 207 - Where clause is built from Predicate tree.
Appendix step 208 - Predicate tree contains and and or nodes.
Appendix step 209 - Like predicates become SQL like expressions.
Appendix step 210 - Lower function becomes SQL lower(...).
Appendix step 211 - Equal predicate becomes SQL =.
Appendix step 212 - GreaterThanOrEqual becomes SQL >=.
Appendix step 213 - LessThanOrEqual becomes SQL <=.
Appendix step 214 - Date values are passed as JDBC parameters.
Appendix step 215 - Enum values are passed as JDBC parameters.
Appendix step 216 - Pagination becomes SQL limit and offset.
Appendix step 217 - Sorting becomes SQL order by list.
Appendix step 218 - Hibernate generates parameter placeholders.
Appendix step 219 - Placeholder order matches predicate creation order.
Appendix step 220 - PreparedStatement is created by JDBC driver.
Appendix step 221 - Hibernate binds parameters to PreparedStatement.
Appendix step 222 - Database query planner chooses an index.
Appendix step 223 - Database executes the query plan.
Appendix step 224 - Rows are returned by the database.
Appendix step 225 - JDBC driver streams rows to Hibernate.
Appendix step 226 - Hibernate builds entity instances.
Appendix step 227 - Entities are attached to persistence context.
Appendix step 228 - Lazy relations are not loaded yet.
Appendix step 229 - DTO mapping accesses simple fields only.
Appendix step 230 - Response uses DTOs to avoid lazy loading issues.
Appendix step 231 - Count query is executed separately.
Appendix step 232 - Count query uses count(*) or count(id).
Appendix step 233 - Count query ignores order by for efficiency.
Appendix step 234 - Hibernate returns total count as Long.
Appendix step 235 - PageImpl combines content and total.
Appendix step 236 - Spring MVC serializes Page content to JSON.
Appendix step 237 - Pageable metadata is included in JSON.
Appendix step 238 - Client reads total pages and page info.
Appendix step 239 - Client requests next page if needed.
Appendix step 240 - Next request repeats the pipeline.
Appendix step 241 - Caching can happen at database or app level.
Appendix step 242 - Second level cache may store entities.
Appendix step 243 - Query cache is optional and off by default.
Appendix step 244 - SQL logging can be enabled for debugging.
Appendix step 245 - SQL logging shows placeholders, not values.
Appendix step 246 - Binding logs can show actual parameter values.
Appendix step 247 - Use logging level org.hibernate.SQL for SQL.
Appendix step 248 - Use logging level org.hibernate.orm.jdbc.bind for values.
Appendix step 249 - Avoid logging in production due to sensitive data.
Appendix step 250 - If criteria is null, no where clause is added.
Appendix step 251 - If only one predicate, and(...) is still ok.
Appendix step 252 - CriteriaBuilder.and ignores empty predicate list.
Appendix step 253 - If predicates list is empty, use conjunction.
Appendix step 254 - Conjunction means always true condition.
Appendix step 255 - This yields all rows with paging.
Appendix step 256 - If you need OR logic, build it explicitly.
Appendix step 257 - OR logic can reduce index usage.
Appendix step 258 - Combine OR with parentheses for correctness.
Appendix step 259 - For optional ranges, use between if both bounds exist.
Appendix step 260 - For optional ranges, use >= or <= if only one bound.
Appendix step 261 - For partial string search, use like with %.
Appendix step 262 - For prefix search, use value + %.
Appendix step 263 - For suffix search, use % + value.
Appendix step 264 - For exact search, use equal.
Appendix step 265 - For case insensitive search, use lower or upper.
Appendix step 266 - Consider collation settings if available.
Appendix step 267 - Do not concatenate raw SQL from user input.
Appendix step 268 - Always use parameter binding for safety.
Appendix step 269 - Criteria API uses parameter binding by default.
Appendix step 270 - This prevents SQL injection.
Appendix step 271 - Beware of wildcard injection in like patterns.
Appendix step 272 - Escape % and _ if user input is untrusted.
Appendix step 273 - You can add escaping logic in Specification.
Appendix step 274 - Sorting on non indexed fields can be slow.
Appendix step 275 - Sorting on large text fields is costly.
Appendix step 276 - Prefer sorting on small indexed columns.
Appendix step 277 - If using join, duplicates can appear.
Appendix step 278 - Use distinct if join introduces duplicates.
Appendix step 279 - Distinct can change count query behavior.
Appendix step 280 - Test with real data to confirm performance.
