# Spring Data filtering and paging (learning notes)

## Pageable
`Pageable` is an interface that describes page, size, and sort information. Spring MVC can build it automatically from query params:
- `page` (0 based)
- `size`
- `sort=field,asc` (can repeat)

Typical usage:
- Build it in code with `PageRequest.of(page, size, Sort.by("name").ascending())`
- Or let Spring bind it from request params

`Pageable` returns a `Page<T>` which contains:
- content list
- total elements
- total pages
- current page, size, and sort

## Specification
`Specification<T>` lets you build dynamic queries based on optional filters. It is a functional interface used by Spring Data JPA. You return a `Predicate` that describes the where clause.

Why it exists:
- avoids repository method explosion
- supports optional filters without string concatenation
- allows reusable filter building blocks

## Predicate
`Predicate` is a Criteria API interface for boolean expressions. It is chosen because the Criteria API is type safe and composable. You can combine predicates with:
- `builder.and(p1, p2, ...)`
- `builder.or(p1, p2, ...)`

## Who creates Predicate
`Predicate` is implemented by the JPA provider (Hibernate). Spring Data calls `CriteriaBuilder` to create predicates; it does not implement them itself.

## How SQL is produced
1. Controller receives query params
2. Spring binds `AgencySearchCriteria` + `Pageable`
3. Service calls `findAll(spec, pageable)`
4. Spring Data creates a CriteriaQuery and applies the predicate
5. Hibernate converts the criteria tree into SQL with parameters
6. Database executes SQL; paging uses `LIMIT`/`OFFSET`

## Common pitfalls
- `like lower(...)` can be slow without indexes
- paging triggers a count query
- avoid fetch joins with paging unless you handle duplicates

## When to use Specification
- Advanced search endpoints
- Many optional filters
- Need to combine filters dynamically
