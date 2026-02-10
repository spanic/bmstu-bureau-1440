# MMORPG Database Test Suite

## Overview

This directory contains comprehensive tests for the MMORPG database schema, data, and queries.

## Test Files

- `MMORPG_tests.sql` - Main test suite with 43 comprehensive tests
- `MMORPG_regression_tests.sql` - Regression test suite with 23 additional tests
- `validate_tests.sh` - Shell script for validating test file structure and syntax

## Test Categories

### 1. Schema Creation Tests (4 tests)
- Verifies all lookup tables exist
- Validates heroes table structure
- Checks junction tables
- Confirms materialized view creation

### 2. Data Loading Tests (4 tests)
- Verifies races loaded correctly
- Validates professions data
- Checks heroes data
- Confirms levels progression table

### 3. Constraint Tests (9 tests)
- CHECK constraints (base_health, sex, price, quantity, level)
- Foreign key constraints (hero race)
- UNIQUE constraints (hero name, race name, equipment slots)

### 4. Materialized View Tests (4 tests)
- Verifies hero_stats contains all heroes
- Tests level calculation logic
- Validates stat calculation formulas (health, damage, mana)
- Confirms gold display

### 5. Query Tests (7 tests)
- Query 1: Hero profile
- Query 2: Hero skills by source (race/profession)
- Query 3: Hero armor equipped
- Query 4: Hero weapons equipped
- Query 5: Available items by slot
- Query 6: Hero inventory
- Query 7: Shop items (affordable + level requirement)

### 6. Edge Cases and Negative Scenarios (15 tests)
- Zero experience → level 1 mapping
- Maximum experience handling
- Cascade delete prevention
- Skill exclusivity (race vs profession)
- Required lookup codes validation
- Boundary conditions (1000 XP = level 5)
- NULL profession handling
- Multi-slot weapon support
- Active vs passive skills
- Materialized view refresh

## How to Run the Tests

### Prerequisites

- PostgreSQL 12 or later installed
- `psql` command-line tool available
- Superuser or database creation privileges

### Execution

```bash
# Method 1: Run main test suite
psql -U postgres -f MMORPG_tests.sql

# Method 2: Run regression tests (assumes test database exists)
psql -U postgres -f MMORPG_regression_tests.sql

# Method 3: Run both test suites sequentially
psql -U postgres -f MMORPG_tests.sql && psql -U postgres -f MMORPG_regression_tests.sql

# Method 4: Validate test files before running
bash validate_tests.sh && psql -U postgres -f MMORPG_tests.sql

# Method 5: Run with output logging
psql -U postgres -f MMORPG_tests.sql > test_results.log 2>&1
psql -U postgres -f MMORPG_regression_tests.sql >> test_results.log 2>&1
```

### What the Tests Do

1. **Create Test Database**: Creates a fresh `mmorpg_test` database
2. **Load Schema**: Executes `MMORPG.sql` to create all tables
3. **Load Data**: Executes `MMORPG_data.sql` to populate tables
4. **Run Tests**: Executes 43 tests across 6 categories
5. **Display Summary**: Shows statistics and sample hero profile

### Expected Output

The test suite will output:
- ✓ Success messages for each passing test
- ⚠ Warnings for expected conditions
- ✗ Exceptions with details for any failures
- Summary statistics at the end
- Sample hero profile (Arthas)

### Clean Up

The test creates a temporary database `mmorpg_test`. To remove it:

```bash
psql -U postgres -c "DROP DATABASE IF EXISTS mmorpg_test;"
```

## Test Coverage

### Main Test Suite (MMORPG_tests.sql)

| Category | Tests | Coverage |
|----------|-------|----------|
| Schema | 4 | Table existence, structure, views |
| Data | 4 | Data loading, integrity |
| Constraints | 9 | CHECK, FK, UNIQUE constraints |
| Views | 4 | Materialized view calculations |
| Queries | 7 | All 7 game queries |
| Edge Cases | 15 | Boundaries, NULL values, cascades |
| **Total** | **43** | **Comprehensive coverage** |

### Regression Test Suite (MMORPG_regression_tests.sql)

| Category | Tests | Coverage |
|----------|-------|----------|
| Equipment Consistency | 4 | Duplicate checks, slot validation |
| Level Progression | 3 | XP monotonicity, level bounds |
| Stat Calculations | 3 | Health, damage, mana formulas |
| Data Integrity | 4 | Cross-table consistency |
| Referential Integrity | 2 | Foreign key validation |
| Business Logic | 3 | Level requirements, affordability |
| Performance | 2 | Indexes, orphaned records |
| **Total** | **23** | **Regression scenarios** |

**Grand Total: 66 Tests**

## Test Design Principles

1. **Isolation**: Tests run in a separate `mmorpg_test` database
2. **Rollback**: Constraint tests use transactions with rollback
3. **Comprehensive**: Covers positive cases, negative cases, and edge cases
4. **Clear Output**: Each test reports success or failure with details
5. **Automated**: Can be run with a single command

## Additional Tests

Beyond the main test suite, you may want to manually verify:

1. **Performance**: Test query execution time on large datasets
2. **Concurrency**: Test concurrent updates and materialized view refreshes
3. **Security**: Test role-based access controls (if implemented)
4. **Backup/Restore**: Test database backup and recovery procedures

## Troubleshooting

### Test Fails: "relation already exists"
- The test database wasn't cleaned up from a previous run
- Solution: `DROP DATABASE mmorpg_test;` before running again

### Test Fails: "database mmorpg_test is being accessed by other users"
- Another session is connected to the test database
- Solution: Disconnect other sessions or use a different database name

### Test Fails: Permission denied
- Insufficient privileges to create databases
- Solution: Run as PostgreSQL superuser or grant CREATE DATABASE privilege

## Contributing

When adding new features to the schema:

1. Update `MMORPG.sql` with schema changes
2. Update `MMORPG_data.sql` with sample data
3. Add corresponding tests to `MMORPG_tests.sql`
4. Update this README with new test descriptions
5. Run the full test suite to ensure nothing broke

## License

This test suite is part of a database homework assignment.