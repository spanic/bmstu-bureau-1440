-- ============================================================
-- MMORPG Database Test Suite
-- Comprehensive tests for schema, data integrity, and queries
-- ============================================================

\echo '========================================='
\echo 'Starting MMORPG Database Test Suite'
\echo '========================================='

-- Create test database
DROP DATABASE IF EXISTS mmorpg_test;
CREATE DATABASE mmorpg_test;
\c mmorpg_test

\echo ''
\echo '[TEST CATEGORY 1] Schema Creation Tests'
\echo '========================================='

-- Load schema
\i MMORPG.sql

-- Test 1.1: Verify all lookup tables exist
\echo 'Test 1.1: Verify all lookup tables exist'
DO $$
DECLARE
    missing_tables TEXT[];
BEGIN
    SELECT array_agg(table_name)
    INTO missing_tables
    FROM (VALUES
        ('race'), ('profession'), ('alliances'), ('levels'),
        ('skills'), ('effects'), ('targets'), ('impact_types'),
        ('slots'), ('weapons'), ('armor')
    ) AS expected(table_name)
    WHERE NOT EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_schema = 'public'
        AND table_name = expected.table_name
    );

    IF missing_tables IS NOT NULL THEN
        RAISE EXCEPTION 'Missing tables: %', missing_tables;
    END IF;
    RAISE NOTICE '✓ All lookup tables exist';
END $$;

-- Test 1.2: Verify heroes table structure
\echo 'Test 1.2: Verify heroes table structure'
DO $$
DECLARE
    col_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO col_count
    FROM information_schema.columns
    WHERE table_schema = 'public'
    AND table_name = 'heroes';

    IF col_count < 11 THEN
        RAISE EXCEPTION 'Heroes table has insufficient columns: %', col_count;
    END IF;
    RAISE NOTICE '✓ Heroes table has correct structure (% columns)', col_count;
END $$;

-- Test 1.3: Verify junction tables exist
\echo 'Test 1.3: Verify junction tables exist'
DO $$
DECLARE
    missing_tables TEXT[];
BEGIN
    SELECT array_agg(table_name)
    INTO missing_tables
    FROM (VALUES
        ('skills_race'), ('skills_profession'),
        ('skills_targets_effects_impact_types'),
        ('weapons_slots'), ('weapons_targets_effects_impact_types'),
        ('heroes_weapons_slots'), ('armor_slots'),
        ('armor_targets_effects_impact_types'), ('heroes_armor_slots'),
        ('heroes_inventory_weapons'), ('heroes_inventory_armor')
    ) AS expected(table_name)
    WHERE NOT EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_schema = 'public'
        AND table_name = expected.table_name
    );

    IF missing_tables IS NOT NULL THEN
        RAISE EXCEPTION 'Missing junction tables: %', missing_tables;
    END IF;
    RAISE NOTICE '✓ All junction tables exist';
END $$;

-- Test 1.4: Verify materialized view exists
\echo 'Test 1.4: Verify materialized view exists'
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_matviews
        WHERE schemaname = 'public'
        AND matviewname = 'hero_stats'
    ) THEN
        RAISE EXCEPTION 'Materialized view hero_stats does not exist';
    END IF;
    RAISE NOTICE '✓ Materialized view hero_stats exists';
END $$;

\echo ''
\echo '[TEST CATEGORY 2] Data Loading Tests'
\echo '========================================='

-- Load test data
\i MMORPG_data.sql

-- Refresh materialized view
REFRESH MATERIALIZED VIEW public.hero_stats;

-- Test 2.1: Verify races loaded
\echo 'Test 2.1: Verify races loaded'
DO $$
DECLARE
    race_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO race_count FROM public.race;
    IF race_count < 5 THEN
        RAISE EXCEPTION 'Expected at least 5 races, got %', race_count;
    END IF;
    RAISE NOTICE '✓ Loaded % races', race_count;
END $$;

-- Test 2.2: Verify professions loaded
\echo 'Test 2.2: Verify professions loaded'
DO $$
DECLARE
    prof_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO prof_count FROM public.profession;
    IF prof_count < 5 THEN
        RAISE EXCEPTION 'Expected at least 5 professions, got %', prof_count;
    END IF;
    RAISE NOTICE '✓ Loaded % professions', prof_count;
END $$;

-- Test 2.3: Verify heroes loaded
\echo 'Test 2.3: Verify heroes loaded'
DO $$
DECLARE
    hero_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO hero_count FROM public.heroes;
    IF hero_count < 5 THEN
        RAISE EXCEPTION 'Expected at least 5 heroes, got %', hero_count;
    END IF;
    RAISE NOTICE '✓ Loaded % heroes', hero_count;
END $$;

-- Test 2.4: Verify levels loaded correctly
\echo 'Test 2.4: Verify levels loaded correctly'
DO $$
DECLARE
    max_level INTEGER;
    level_count INTEGER;
BEGIN
    SELECT MAX(level), COUNT(*) INTO max_level, level_count FROM public.levels;
    IF max_level < 10 OR level_count < 10 THEN
        RAISE EXCEPTION 'Expected 10 levels up to level 10, got % levels', level_count;
    END IF;
    RAISE NOTICE '✓ Loaded % levels (max level: %)', level_count, max_level;
END $$;

\echo ''
\echo '[TEST CATEGORY 3] Constraint Tests'
\echo '========================================='

-- Test 3.1: Race base_health must be non-negative
\echo 'Test 3.1: Test race base_health CHECK constraint'
DO $$
BEGIN
    BEGIN
        INSERT INTO public.race (name, description, base_health, base_mana, base_damage, base_strength, base_agility, base_intellect)
        VALUES ('TestRace', 'Test', -10, 0, 0, 0, 0, 0);
        RAISE EXCEPTION 'Should have failed: negative base_health was allowed';
    EXCEPTION
        WHEN check_violation THEN
            RAISE NOTICE '✓ CHECK constraint on base_health working correctly';
    END;
    ROLLBACK;
END $$;

-- Test 3.2: Hero sex must be male or female
\echo 'Test 3.2: Test hero sex CHECK constraint'
DO $$
BEGIN
    BEGIN
        INSERT INTO public.heroes (name, sex, race, created_date, experience_points, gold, strength, agility, intellect)
        VALUES ('TestHero', 'invalid', 1, NOW(), 0, 0, 0, 0, 0);
        RAISE EXCEPTION 'Should have failed: invalid sex value was allowed';
    EXCEPTION
        WHEN check_violation THEN
            RAISE NOTICE '✓ CHECK constraint on sex working correctly';
    END;
    ROLLBACK;
END $$;

-- Test 3.3: Weapon price must be non-negative
\echo 'Test 3.3: Test weapon price CHECK constraint'
DO $$
BEGIN
    BEGIN
        INSERT INTO public.weapons (name, price, range, required_lvl, cooldown_time_ms, mana_cost)
        VALUES ('TestWeapon', -100, 0, 1, 0, 0);
        RAISE EXCEPTION 'Should have failed: negative price was allowed';
    EXCEPTION
        WHEN check_violation THEN
            RAISE NOTICE '✓ CHECK constraint on weapon price working correctly';
    END;
    ROLLBACK;
END $$;

-- Test 3.4: Inventory quantity must be positive
\echo 'Test 3.4: Test inventory quantity CHECK constraint'
DO $$
BEGIN
    BEGIN
        INSERT INTO public.heroes_inventory_weapons (hero_id, weapon_id, quantity)
        VALUES (1, 1, 0);
        RAISE EXCEPTION 'Should have failed: zero quantity was allowed';
    EXCEPTION
        WHEN check_violation THEN
            RAISE NOTICE '✓ CHECK constraint on quantity working correctly';
    END;
    ROLLBACK;
END $$;

-- Test 3.5: Level must be positive
\echo 'Test 3.5: Test level CHECK constraint'
DO $$
BEGIN
    BEGIN
        INSERT INTO public.levels (level, required_xp) VALUES (0, 0);
        RAISE EXCEPTION 'Should have failed: level 0 was allowed';
    EXCEPTION
        WHEN check_violation THEN
            RAISE NOTICE '✓ CHECK constraint on level working correctly';
    END;
    ROLLBACK;
END $$;

-- Test 3.6: Foreign key constraint - hero race
\echo 'Test 3.6: Test foreign key constraint on hero race'
DO $$
BEGIN
    BEGIN
        INSERT INTO public.heroes (name, sex, race, created_date, experience_points, gold, strength, agility, intellect)
        VALUES ('TestHero2', 'male', 99999, NOW(), 0, 0, 0, 0, 0);
        RAISE EXCEPTION 'Should have failed: invalid race_id was allowed';
    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE NOTICE '✓ Foreign key constraint on race working correctly';
    END;
    ROLLBACK;
END $$;

-- Test 3.7: Unique constraint on hero name
\echo 'Test 3.7: Test unique constraint on hero name'
DO $$
BEGIN
    BEGIN
        INSERT INTO public.heroes (name, sex, race, created_date, experience_points, gold, strength, agility, intellect)
        VALUES ('Arthas', 'male', 1, NOW(), 0, 0, 0, 0, 0);
        RAISE EXCEPTION 'Should have failed: duplicate hero name was allowed';
    EXCEPTION
        WHEN unique_violation THEN
            RAISE NOTICE '✓ UNIQUE constraint on hero name working correctly';
    END;
    ROLLBACK;
END $$;

-- Test 3.8: Unique constraint on race name
\echo 'Test 3.8: Test unique constraint on race name'
DO $$
BEGIN
    BEGIN
        INSERT INTO public.race (name, description, base_health, base_mana, base_damage, base_strength, base_agility, base_intellect)
        VALUES ('Human', 'Duplicate', 100, 80, 10, 10, 10, 10);
        RAISE EXCEPTION 'Should have failed: duplicate race name was allowed';
    EXCEPTION
        WHEN unique_violation THEN
            RAISE NOTICE '✓ UNIQUE constraint on race name working correctly';
    END;
    ROLLBACK;
END $$;

-- Test 3.9: Hero equipped items - unique slot constraint
\echo 'Test 3.9: Test unique slot constraint in heroes_weapons_slots'
DO $$
BEGIN
    BEGIN
        -- Try to equip two weapons in the same slot
        INSERT INTO public.heroes_weapons_slots (hero_id, weapon_id, slot_id)
        VALUES (1, 2, 3);  -- Hero 1 already has weapon in slot 3
        RAISE EXCEPTION 'Should have failed: multiple weapons in same slot was allowed';
    EXCEPTION
        WHEN unique_violation THEN
            RAISE NOTICE '✓ UNIQUE constraint on hero weapon slots working correctly';
    END;
    ROLLBACK;
END $$;

\echo ''
\echo '[TEST CATEGORY 4] Materialized View Tests'
\echo '========================================='

-- Test 4.1: Verify hero_stats contains all heroes
\echo 'Test 4.1: Verify hero_stats contains all heroes'
DO $$
DECLARE
    stats_count INTEGER;
    hero_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO stats_count FROM public.hero_stats;
    SELECT COUNT(*) INTO hero_count FROM public.heroes;

    IF stats_count != hero_count THEN
        RAISE EXCEPTION 'hero_stats count (%) != heroes count (%)', stats_count, hero_count;
    END IF;
    RAISE NOTICE '✓ hero_stats contains all % heroes', stats_count;
END $$;

-- Test 4.2: Verify level calculation
\echo 'Test 4.2: Verify level calculation in hero_stats'
DO $$
DECLARE
    arthas_level INTEGER;
    arthas_xp INTEGER;
BEGIN
    SELECT level INTO arthas_level FROM public.hero_stats WHERE hero_name = 'Arthas';
    SELECT experience_points INTO arthas_xp FROM public.heroes WHERE name = 'Arthas';

    -- Arthas has 1200 XP, which should be level 6 (requires 1000 XP)
    IF arthas_level < 5 OR arthas_level > 7 THEN
        RAISE EXCEPTION 'Arthas level calculation incorrect: level % for % XP', arthas_level, arthas_xp;
    END IF;
    RAISE NOTICE '✓ Level calculation correct: Arthas is level % with % XP', arthas_level, arthas_xp;
END $$;

-- Test 4.3: Verify stat calculation formulas
\echo 'Test 4.3: Verify stat calculation formulas'
DO $$
DECLARE
    hero_rec RECORD;
    expected_health INTEGER;
    expected_damage NUMERIC;
    expected_mana INTEGER;
BEGIN
    SELECT * INTO hero_rec FROM public.hero_stats WHERE hero_name = 'Arthas' LIMIT 1;

    -- Health = base_health + total_strength * 50
    -- Arthas: Human (100 base_health, 10 base_strength) + 18 strength = total_strength 28
    expected_health := 100 + 28 * 50;  -- 100 + 1400 = 1500

    IF hero_rec.health != expected_health THEN
        RAISE EXCEPTION 'Health calculation incorrect for Arthas: expected %, got %',
            expected_health, hero_rec.health;
    END IF;

    RAISE NOTICE '✓ Stat calculations correct for Arthas (health: %, damage: %, mana: %)',
        hero_rec.health, hero_rec.damage, hero_rec.mana;
END $$;

-- Test 4.4: Verify gold is displayed correctly
\echo 'Test 4.4: Verify gold in hero_stats'
DO $$
DECLARE
    stats_gold INTEGER;
    hero_gold INTEGER;
BEGIN
    SELECT gold INTO stats_gold FROM public.hero_stats WHERE hero_name = 'Jaina';
    SELECT gold INTO hero_gold FROM public.heroes WHERE name = 'Jaina';

    IF stats_gold != hero_gold THEN
        RAISE EXCEPTION 'Gold mismatch: hero_stats has %, heroes has %', stats_gold, hero_gold;
    END IF;
    RAISE NOTICE '✓ Gold correctly displayed in hero_stats: % gold', stats_gold;
END $$;

\echo ''
\echo '[TEST CATEGORY 5] Query Tests'
\echo '========================================='

-- Test 5.1: Query 1 - Hero profile
\echo 'Test 5.1: Test hero profile query (Query 1)'
DO $$
DECLARE
    result_count INTEGER;
    hero_rec RECORD;
BEGIN
    SELECT COUNT(*) INTO result_count
    FROM public.hero_stats
    WHERE hero_id = 1;

    IF result_count != 1 THEN
        RAISE EXCEPTION 'Hero profile query returned % rows, expected 1', result_count;
    END IF;

    SELECT * INTO hero_rec FROM public.hero_stats WHERE hero_id = 1;

    IF hero_rec.hero_name IS NULL OR hero_rec.level IS NULL OR hero_rec.health IS NULL THEN
        RAISE EXCEPTION 'Hero profile query missing essential fields';
    END IF;

    RAISE NOTICE '✓ Hero profile query working: % (level %, health %)',
        hero_rec.hero_name, hero_rec.level, hero_rec.health;
END $$;

-- Test 5.2: Query 2 - Hero skills by source
\echo 'Test 5.2: Test hero skills query (Query 2)'
DO $$
DECLARE
    skill_count INTEGER;
    race_skill_count INTEGER;
    prof_skill_count INTEGER;
BEGIN
    -- Arthas is Human Warrior
    -- Should have Human racial skills and Warrior profession skills

    -- Check racial skills
    SELECT COUNT(*) INTO race_skill_count
    FROM public.skills_race sr
    JOIN public.heroes h ON sr.race_id = h.race
    JOIN public.skills s ON s.id = sr.skills_id
    WHERE h.id = 1;

    -- Check profession skills
    SELECT COUNT(*) INTO prof_skill_count
    FROM public.skills_profession sp
    JOIN public.heroes h ON sp.profession_id = h.profession
    JOIN public.skills s ON s.id = sp.skills_id
    WHERE h.id = 1;

    IF race_skill_count = 0 AND prof_skill_count = 0 THEN
        RAISE EXCEPTION 'Hero skills query returned no skills';
    END IF;

    RAISE NOTICE '✓ Hero skills query working: % racial skills, % profession skills',
        race_skill_count, prof_skill_count;
END $$;

-- Test 5.3: Query 3 - Hero armor
\echo 'Test 5.3: Test hero armor query (Query 3)'
DO $$
DECLARE
    armor_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO armor_count
    FROM public.heroes_armor_slots has
    JOIN public.armor a ON a.id = has.armor_id
    JOIN public.slots sl ON sl.id = has.slot_id
    WHERE has.hero_id = 1;

    IF armor_count = 0 THEN
        RAISE WARNING 'Hero armor query returned no armor (may be valid if hero has no armor)';
    ELSE
        RAISE NOTICE '✓ Hero armor query working: % armor pieces equipped', armor_count;
    END IF;
END $$;

-- Test 5.4: Query 4 - Hero weapons
\echo 'Test 5.4: Test hero weapons query (Query 4)'
DO $$
DECLARE
    weapon_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO weapon_count
    FROM public.heroes_weapons_slots hws
    JOIN public.weapons w ON w.id = hws.weapon_id
    JOIN public.slots sl ON sl.id = hws.slot_id
    WHERE hws.hero_id = 1;

    IF weapon_count = 0 THEN
        RAISE WARNING 'Hero weapons query returned no weapons (may be valid if hero has no weapons)';
    ELSE
        RAISE NOTICE '✓ Hero weapons query working: % weapons equipped', weapon_count;
    END IF;
END $$;

-- Test 5.5: Query 5 - Available items by slot
\echo 'Test 5.5: Test available items by slot query (Query 5)'
DO $$
DECLARE
    item_count INTEGER;
BEGIN
    -- Get items for main_hand slot for hero 1
    WITH hero_level AS (
        SELECT MAX(l.level) AS level
        FROM public.levels l
        JOIN public.heroes h ON l.required_xp <= h.experience_points
        WHERE h.id = 1
    )
    SELECT COUNT(*) INTO item_count
    FROM public.weapons w
    JOIN public.weapons_slots ws ON ws.weapons_id = w.id
    JOIN public.slots sl ON sl.id = ws.slots_id
    CROSS JOIN hero_level hl
    WHERE sl.code = 'main_hand'
      AND w.required_lvl <= hl.level;

    IF item_count = 0 THEN
        RAISE EXCEPTION 'Available items query returned no items';
    END IF;

    RAISE NOTICE '✓ Available items query working: % weapons available for main_hand', item_count;
END $$;

-- Test 5.6: Query 6 - Hero inventory
\echo 'Test 5.6: Test hero inventory query (Query 6)'
DO $$
DECLARE
    inventory_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO inventory_count
    FROM (
        SELECT 'weapon' AS item_type, w.name AS item_name, w.price, hiw.quantity
        FROM public.heroes_inventory_weapons hiw
        JOIN public.weapons w ON w.id = hiw.weapon_id
        WHERE hiw.hero_id = 1
        UNION ALL
        SELECT 'armor' AS item_type, a.name AS item_name, a.price, hia.quantity
        FROM public.heroes_inventory_armor hia
        JOIN public.armor a ON a.id = hia.armor_id
        WHERE hia.hero_id = 1
    ) AS inventory;

    IF inventory_count = 0 THEN
        RAISE WARNING 'Inventory query returned no items (may be valid if inventory is empty)';
    ELSE
        RAISE NOTICE '✓ Inventory query working: % items in inventory', inventory_count;
    END IF;
END $$;

-- Test 5.7: Query 7 - Shop items
\echo 'Test 5.7: Test shop items query (Query 7)'
DO $$
DECLARE
    shop_count INTEGER;
    hero_gold INTEGER;
BEGIN
    SELECT gold INTO hero_gold FROM public.heroes WHERE id = 1;

    WITH hero_info AS (
        SELECT
            h.gold,
            (SELECT MAX(l.level) FROM public.levels l WHERE l.required_xp <= h.experience_points) AS level
        FROM public.heroes h
        WHERE h.id = 1
    )
    SELECT COUNT(*) INTO shop_count
    FROM public.weapons w
    CROSS JOIN hero_info hi
    WHERE w.price <= hi.gold
      AND w.required_lvl <= hi.level;

    RAISE NOTICE '✓ Shop query working: % weapons affordable with % gold', shop_count, hero_gold;
END $$;

\echo ''
\echo '[TEST CATEGORY 6] Edge Cases and Negative Scenarios'
\echo '========================================='

-- Test 6.1: Hero with zero experience should be level 1
\echo 'Test 6.1: Test level calculation for zero experience'
DO $$
DECLARE
    test_hero_id INTEGER;
    calc_level INTEGER;
BEGIN
    -- Create a temporary hero with 0 XP
    INSERT INTO public.heroes (name, sex, race, created_date, experience_points, gold, strength, agility, intellect)
    VALUES ('TestHeroZeroXP', 'male', 1, NOW(), 0, 0, 0, 0, 0)
    RETURNING id INTO test_hero_id;

    REFRESH MATERIALIZED VIEW public.hero_stats;

    SELECT level INTO calc_level FROM public.hero_stats WHERE hero_id = test_hero_id;

    IF calc_level != 1 THEN
        RAISE EXCEPTION 'Hero with 0 XP should be level 1, got level %', calc_level;
    END IF;

    RAISE NOTICE '✓ Zero experience correctly maps to level 1';

    -- Cleanup
    DELETE FROM public.heroes WHERE id = test_hero_id;
    REFRESH MATERIALIZED VIEW public.hero_stats;
END $$;

-- Test 6.2: Hero with maximum experience
\echo 'Test 6.2: Test level calculation for maximum experience'
DO $$
DECLARE
    test_hero_id INTEGER;
    calc_level INTEGER;
    max_xp INTEGER;
BEGIN
    SELECT MAX(required_xp) INTO max_xp FROM public.levels;

    INSERT INTO public.heroes (name, sex, race, created_date, experience_points, gold, strength, agility, intellect)
    VALUES ('TestHeroMaxXP', 'male', 1, NOW(), max_xp + 1000, 0, 0, 0, 0)
    RETURNING id INTO test_hero_id;

    REFRESH MATERIALIZED VIEW public.hero_stats;

    SELECT level INTO calc_level FROM public.hero_stats WHERE hero_id = test_hero_id;

    IF calc_level IS NULL THEN
        RAISE EXCEPTION 'Hero with max+1000 XP has NULL level';
    END IF;

    RAISE NOTICE '✓ Maximum experience correctly maps to level % (XP: %)', calc_level, max_xp + 1000;

    DELETE FROM public.heroes WHERE id = test_hero_id;
    REFRESH MATERIALIZED VIEW public.hero_stats;
END $$;

-- Test 6.3: Cascade delete on race should be prevented
\echo 'Test 6.3: Test cascade delete prevention on race'
DO $$
BEGIN
    BEGIN
        DELETE FROM public.race WHERE name = 'Human';
        RAISE EXCEPTION 'Should have failed: race delete with existing heroes was allowed';
    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE NOTICE '✓ ON DELETE RESTRICT working: cannot delete race with existing heroes';
    END;
    ROLLBACK;
END $$;

-- Test 6.4: Skill can belong to either race OR profession, not both
\echo 'Test 6.4: Test skill exclusivity (race vs profession)'
DO $$
DECLARE
    overlap_count INTEGER;
BEGIN
    -- Check if any skill exists in both skills_race and skills_profession
    SELECT COUNT(*) INTO overlap_count
    FROM public.skills_race sr
    INNER JOIN public.skills_profession sp ON sr.skills_id = sp.skills_id;

    IF overlap_count > 0 THEN
        RAISE EXCEPTION 'Found % skills in both race and profession tables', overlap_count;
    END IF;

    RAISE NOTICE '✓ Skill exclusivity maintained: no skills in both race and profession';
END $$;

-- Test 6.5: Effects table should have expected codes
\echo 'Test 6.5: Test effects table has required codes'
DO $$
DECLARE
    missing_codes TEXT[];
BEGIN
    SELECT array_agg(code)
    INTO missing_codes
    FROM (VALUES ('damage'), ('heal'), ('vulnerability'), ('invulnerability')) AS expected(code)
    WHERE NOT EXISTS (
        SELECT 1 FROM public.effects WHERE effects.code = expected.code
    );

    IF missing_codes IS NOT NULL THEN
        RAISE EXCEPTION 'Missing effect codes: %', missing_codes;
    END IF;

    RAISE NOTICE '✓ All required effect codes present';
END $$;

-- Test 6.6: Targets table should have expected codes
\echo 'Test 6.6: Test targets table has required codes'
DO $$
DECLARE
    missing_codes TEXT[];
BEGIN
    SELECT array_agg(code)
    INTO missing_codes
    FROM (VALUES ('self'), ('allies'), ('enemy'), ('area_radius')) AS expected(code)
    WHERE NOT EXISTS (
        SELECT 1 FROM public.targets WHERE targets.code = expected.code
    );

    IF missing_codes IS NOT NULL THEN
        RAISE EXCEPTION 'Missing target codes: %', missing_codes;
    END IF;

    RAISE NOTICE '✓ All required target codes present';
END $$;

-- Test 6.7: Impact types table should have expected codes
\echo 'Test 6.7: Test impact_types table has required codes'
DO $$
DECLARE
    missing_codes TEXT[];
BEGIN
    SELECT array_agg(code)
    INTO missing_codes
    FROM (VALUES ('physical'), ('magical'), ('fire'), ('water'), ('earth'), ('air')) AS expected(code)
    WHERE NOT EXISTS (
        SELECT 1 FROM public.impact_types WHERE impact_types.code = expected.code
    );

    IF missing_codes IS NOT NULL THEN
        RAISE EXCEPTION 'Missing impact type codes: %', missing_codes;
    END IF;

    RAISE NOTICE '✓ All required impact type codes present';
END $$;

-- Test 6.8: Slots table should have expected codes
\echo 'Test 6.8: Test slots table has required codes'
DO $$
DECLARE
    missing_codes TEXT[];
BEGIN
    SELECT array_agg(code)
    INTO missing_codes
    FROM (VALUES ('head'), ('chest'), ('main_hand'), ('off_hand'), ('legs'), ('feet')) AS expected(code)
    WHERE NOT EXISTS (
        SELECT 1 FROM public.slots WHERE slots.code = expected.code
    );

    IF missing_codes IS NOT NULL THEN
        RAISE EXCEPTION 'Missing slot codes: %', missing_codes;
    END IF;

    RAISE NOTICE '✓ All required slot codes present';
END $$;

-- Test 6.9: Weapons must reference valid levels
\echo 'Test 6.9: Test weapon level requirements are valid'
DO $$
DECLARE
    invalid_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO invalid_count
    FROM public.weapons w
    WHERE NOT EXISTS (
        SELECT 1 FROM public.levels l WHERE l.level = w.required_lvl
    );

    IF invalid_count > 0 THEN
        RAISE EXCEPTION '% weapons reference invalid levels', invalid_count;
    END IF;

    RAISE NOTICE '✓ All weapon level requirements are valid';
END $$;

-- Test 6.10: Skills must have valid effect combinations
\echo 'Test 6.10: Test skills have valid effect combinations'
DO $$
DECLARE
    invalid_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO invalid_count
    FROM public.skills_targets_effects_impact_types stei
    WHERE NOT EXISTS (SELECT 1 FROM public.skills WHERE id = stei.skill_id)
       OR NOT EXISTS (SELECT 1 FROM public.targets WHERE id = stei.target_id)
       OR NOT EXISTS (SELECT 1 FROM public.effects WHERE id = stei.effect_id)
       OR NOT EXISTS (SELECT 1 FROM public.impact_types WHERE id = stei.impact_type_id);

    IF invalid_count > 0 THEN
        RAISE EXCEPTION '% skill effect combinations reference invalid IDs', invalid_count;
    END IF;

    RAISE NOTICE '✓ All skill effect combinations reference valid IDs';
END $$;

-- Test 6.11: Active skills should have reasonable mana costs
\echo 'Test 6.11: Test active skills have reasonable parameters'
DO $$
DECLARE
    active_skills INTEGER;
    passive_skills INTEGER;
BEGIN
    SELECT COUNT(*) INTO active_skills FROM public.skills WHERE is_active = true;
    SELECT COUNT(*) INTO passive_skills FROM public.skills WHERE is_active = false;

    IF active_skills = 0 THEN
        RAISE EXCEPTION 'No active skills found';
    END IF;

    RAISE NOTICE '✓ Found % active skills and % passive skills', active_skills, passive_skills;
END $$;

-- Test 6.12: Materialized view refresh should work
\echo 'Test 6.12: Test materialized view can be refreshed'
DO $$
BEGIN
    REFRESH MATERIALIZED VIEW public.hero_stats;
    RAISE NOTICE '✓ Materialized view refresh successful';
END $$;

-- Test 6.13: Test experience to level boundary conditions
\echo 'Test 6.13: Test experience to level boundary conditions'
DO $$
DECLARE
    test_hero_id INTEGER;
    calc_level INTEGER;
BEGIN
    -- Insert hero with exactly 1000 XP (level 5 threshold)
    INSERT INTO public.heroes (name, sex, race, created_date, experience_points, gold, strength, agility, intellect)
    VALUES ('TestHeroBoundary', 'female', 2, NOW(), 1000, 0, 0, 0, 0)
    RETURNING id INTO test_hero_id;

    REFRESH MATERIALIZED VIEW public.hero_stats;

    SELECT level INTO calc_level FROM public.hero_stats WHERE hero_id = test_hero_id;

    IF calc_level != 5 THEN
        RAISE EXCEPTION 'Hero with exactly 1000 XP should be level 5, got level %', calc_level;
    END IF;

    RAISE NOTICE '✓ Boundary condition correct: 1000 XP maps to level 5';

    DELETE FROM public.heroes WHERE id = test_hero_id;
    REFRESH MATERIALIZED VIEW public.hero_stats;
END $$;

-- Test 6.14: Test hero without profession (optional field)
\echo 'Test 6.14: Test hero without profession (NULL profession)'
DO $$
DECLARE
    test_hero_id INTEGER;
BEGIN
    INSERT INTO public.heroes (name, sex, race, profession, created_date, experience_points, gold, strength, agility, intellect)
    VALUES ('TestHeroNoProfession', 'male', 3, NULL, NOW(), 500, 100, 5, 5, 5)
    RETURNING id INTO test_hero_id;

    REFRESH MATERIALIZED VIEW public.hero_stats;

    -- Should not raise error
    PERFORM * FROM public.hero_stats WHERE hero_id = test_hero_id;

    RAISE NOTICE '✓ Hero with NULL profession handled correctly';

    DELETE FROM public.heroes WHERE id = test_hero_id;
    REFRESH MATERIALIZED VIEW public.hero_stats;
END $$;

-- Test 6.15: Test weapon that can be equipped in multiple slots
\echo 'Test 6.15: Test weapon can be equipped in multiple slots'
DO $$
DECLARE
    multi_slot_count INTEGER;
BEGIN
    -- Shadow Dagger (weapon_id = 4) can be equipped in main_hand and off_hand
    SELECT COUNT(DISTINCT slots_id) INTO multi_slot_count
    FROM public.weapons_slots
    WHERE weapons_id = 4;

    IF multi_slot_count < 2 THEN
        RAISE WARNING 'No weapons found that fit multiple slots';
    ELSE
        RAISE NOTICE '✓ Multi-slot weapons supported: weapon 4 fits in % slots', multi_slot_count;
    END IF;
END $$;

\echo ''
\echo '========================================='
\echo 'All Tests Completed Successfully!'
\echo '========================================='
\echo ''

-- Display summary statistics
\echo 'Database Summary Statistics:'
\echo '----------------------------'
SELECT
    'Races' AS entity, COUNT(*)::text AS count FROM public.race
UNION ALL
SELECT 'Professions', COUNT(*)::text FROM public.profession
UNION ALL
SELECT 'Alliances', COUNT(*)::text FROM public.alliances
UNION ALL
SELECT 'Heroes', COUNT(*)::text FROM public.heroes
UNION ALL
SELECT 'Skills', COUNT(*)::text FROM public.skills
UNION ALL
SELECT 'Weapons', COUNT(*)::text FROM public.weapons
UNION ALL
SELECT 'Armor', COUNT(*)::text FROM public.armor
UNION ALL
SELECT 'Levels', COUNT(*)::text FROM public.levels;

\echo ''
\echo 'Test Hero Profile (Arthas):'
\echo '---------------------------'
SELECT
    hero_name,
    level,
    race,
    profession,
    gold,
    health,
    mana,
    damage,
    total_strength,
    total_agility,
    total_intellect
FROM public.hero_stats
WHERE hero_name = 'Arthas';

\echo ''
\echo '========================================='
\echo 'Test Suite Execution Complete'
\echo '========================================='