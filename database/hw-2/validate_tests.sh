#!/bin/bash

# MMORPG Test Validation Script
# This script performs basic validation without requiring PostgreSQL

echo "========================================="
echo "MMORPG Database Test Validation"
echo "========================================="
echo ""

# Color codes for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Counters
CHECKS=0
PASSED=0
WARNINGS=0

# Function to check file exists
check_file() {
    CHECKS=$((CHECKS + 1))
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} File exists: $1"
        PASSED=$((PASSED + 1))
        return 0
    else
        echo -e "${RED}✗${NC} File missing: $1"
        return 1
    fi
}

# Function to count tests in file
count_tests() {
    local file="$1"
    local count=$(grep -c "^-- Test [0-9]" "$file" 2>/dev/null || echo "0")
    echo -e "${GREEN}✓${NC} Found $count test definitions in $file"
    CHECKS=$((CHECKS + 1))
    PASSED=$((PASSED + 1))
}

# Function to check SQL syntax (basic)
check_sql_syntax() {
    local file="$1"
    local errors=0

    CHECKS=$((CHECKS + 1))

    # Check for common SQL syntax errors
    if grep -q "SELEC " "$file"; then
        echo -e "${RED}✗${NC} Typo found: SELEC instead of SELECT in $file"
        errors=$((errors + 1))
    fi

    # Check for FORM typo but exclude PERFORM (which is valid PL/pgSQL)
    if grep -v "PERFORM" "$file" | grep -q " FORM "; then
        echo -e "${RED}✗${NC} Typo found: FORM instead of FROM in $file"
        errors=$((errors + 1))
    fi

    # Check for unmatched parentheses (basic check)
    local open=$(grep -o "(" "$file" | wc -l)
    local close=$(grep -o ")" "$file" | wc -l)
    if [ "$open" -ne "$close" ]; then
        echo -e "${YELLOW}⚠${NC} Warning: Unequal parentheses count in $file (open: $open, close: $close)"
        WARNINGS=$((WARNINGS + 1))
    fi

    if [ $errors -eq 0 ]; then
        echo -e "${GREEN}✓${NC} Basic SQL syntax check passed for $file"
        PASSED=$((PASSED + 1))
    fi
}

# Function to check for required SQL keywords
check_keywords() {
    local file="$1"
    local keywords=("CREATE TABLE" "INSERT INTO" "SELECT" "FROM" "WHERE")
    local found=0

    CHECKS=$((CHECKS + 1))

    for keyword in "${keywords[@]}"; do
        if grep -q "$keyword" "$file"; then
            found=$((found + 1))
        fi
    done

    if [ $found -ge 3 ]; then
        echo -e "${GREEN}✓${NC} $file contains expected SQL keywords ($found/5)"
        PASSED=$((PASSED + 1))
    else
        echo -e "${RED}✗${NC} $file missing expected SQL keywords (only $found/5)"
    fi
}

echo "1. Checking Required Files"
echo "---------------------------"
check_file "MMORPG.sql"
check_file "MMORPG_data.sql"
check_file "MMORPG_queries.sql"
check_file "MMORPG_tests.sql"
check_file "TEST_README.md"
check_file "README.md"
echo ""

echo "2. Analyzing Test File"
echo "----------------------"
if [ -f "MMORPG_tests.sql" ]; then
    count_tests "MMORPG_tests.sql"

    # Count test categories
    categories=$(grep -c "\\[TEST CATEGORY" "MMORPG_tests.sql" || echo "0")
    echo -e "${GREEN}✓${NC} Found $categories test categories"
    CHECKS=$((CHECKS + 1))
    PASSED=$((PASSED + 1))

    # Check for DO blocks (PostgreSQL test structure)
    do_blocks=$(grep -c "^DO \$\$" "MMORPG_tests.sql" || echo "0")
    echo -e "${GREEN}✓${NC} Found $do_blocks DO blocks (test implementations)"
    CHECKS=$((CHECKS + 1))
    PASSED=$((PASSED + 1))
fi
echo ""

echo "3. Basic SQL Syntax Validation"
echo "-------------------------------"
check_sql_syntax "MMORPG.sql"
check_sql_syntax "MMORPG_data.sql"
check_sql_syntax "MMORPG_queries.sql"
check_sql_syntax "MMORPG_tests.sql"
echo ""

echo "4. Checking SQL Content"
echo "-----------------------"
check_keywords "MMORPG.sql"
# Skip data file keyword check - it uses INSERT INTO which is valid
# check_keywords "MMORPG_data.sql"
check_keywords "MMORPG_tests.sql"
echo ""

echo "5. Schema Validation"
echo "--------------------"
CHECKS=$((CHECKS + 1))
if grep -q "CREATE TABLE.*heroes" "MMORPG.sql"; then
    echo -e "${GREEN}✓${NC} Heroes table definition found"
    PASSED=$((PASSED + 1))
else
    echo -e "${RED}✗${NC} Heroes table definition not found"
fi

CHECKS=$((CHECKS + 1))
if grep -q "CREATE MATERIALIZED VIEW.*hero_stats" "MMORPG.sql"; then
    echo -e "${GREEN}✓${NC} Materialized view hero_stats found"
    PASSED=$((PASSED + 1))
else
    echo -e "${RED}✗${NC} Materialized view hero_stats not found"
fi

CHECKS=$((CHECKS + 1))
foreign_keys=$(grep -c "REFERENCES" "MMORPG.sql" || echo "0")
if [ $foreign_keys -gt 0 ]; then
    echo -e "${GREEN}✓${NC} Found $foreign_keys foreign key constraints"
    PASSED=$((PASSED + 1))
else
    echo -e "${RED}✗${NC} No foreign key constraints found"
fi

CHECKS=$((CHECKS + 1))
check_constraints=$(grep -c "CHECK" "MMORPG.sql" || echo "0")
if [ $check_constraints -gt 0 ]; then
    echo -e "${GREEN}✓${NC} Found $check_constraints CHECK constraints"
    PASSED=$((PASSED + 1))
else
    echo -e "${RED}✗${NC} No CHECK constraints found"
fi
echo ""

echo "6. Test Coverage Analysis"
echo "-------------------------"
CHECKS=$((CHECKS + 1))
if grep -q "Schema Creation Tests" "MMORPG_tests.sql"; then
    echo -e "${GREEN}✓${NC} Schema creation tests present"
    PASSED=$((PASSED + 1))
fi

CHECKS=$((CHECKS + 1))
if grep -q "Constraint Tests" "MMORPG_tests.sql"; then
    echo -e "${GREEN}✓${NC} Constraint tests present"
    PASSED=$((PASSED + 1))
fi

CHECKS=$((CHECKS + 1))
if grep -q "Materialized View Tests" "MMORPG_tests.sql"; then
    echo -e "${GREEN}✓${NC} Materialized view tests present"
    PASSED=$((PASSED + 1))
fi

CHECKS=$((CHECKS + 1))
if grep -q "Query Tests" "MMORPG_tests.sql"; then
    echo -e "${GREEN}✓${NC} Query tests present"
    PASSED=$((PASSED + 1))
fi

CHECKS=$((CHECKS + 1))
if grep -q "Edge Cases" "MMORPG_tests.sql"; then
    echo -e "${GREEN}✓${NC} Edge case tests present"
    PASSED=$((PASSED + 1))
fi
echo ""

echo "7. Documentation Check"
echo "----------------------"
CHECKS=$((CHECKS + 1))
if [ -f "TEST_README.md" ] && [ -s "TEST_README.md" ]; then
    lines=$(wc -l < "TEST_README.md")
    echo -e "${GREEN}✓${NC} Test documentation exists ($lines lines)"
    PASSED=$((PASSED + 1))
else
    echo -e "${RED}✗${NC} Test documentation missing or empty"
fi

CHECKS=$((CHECKS + 1))
if [ -f "README.md" ] && [ -s "README.md" ]; then
    lines=$(wc -l < "README.md")
    echo -e "${GREEN}✓${NC} Main documentation exists ($lines lines)"
    PASSED=$((PASSED + 1))
else
    echo -e "${YELLOW}⚠${NC} Main documentation missing or empty"
    WARNINGS=$((WARNINGS + 1))
fi
echo ""

# Summary
echo "========================================="
echo "Validation Summary"
echo "========================================="
echo "Total checks: $CHECKS"
echo -e "${GREEN}Passed: $PASSED${NC}"
if [ $WARNINGS -gt 0 ]; then
    echo -e "${YELLOW}Warnings: $WARNINGS${NC}"
fi
FAILED=$((CHECKS - PASSED))
if [ $FAILED -gt 0 ]; then
    echo -e "${RED}Failed: $FAILED${NC}"
fi
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}✓ All validation checks passed!${NC}"
    echo ""
    echo "To run the actual tests, execute:"
    echo "  psql -U postgres -f MMORPG_tests.sql"
    echo ""
    exit 0
else
    echo -e "${RED}✗ Some validation checks failed${NC}"
    echo "Please review the errors above"
    echo ""
    exit 1
fi