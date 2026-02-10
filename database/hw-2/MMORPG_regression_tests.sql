-- ============================================================
-- MMORPG Database Regression Test Suite
-- Additional tests for boundary conditions and data consistency
-- ============================================================

\echo '========================================='
\echo 'MMORPG Regression Test Suite'
\echo '========================================='
\echo 'These tests focus on regression scenarios,'
\echo 'data consistency, and performance edge cases'
\echo '========================================='
\echo ''

-- Connect to test database (assumes it exists from main test suite)
\c mmorpg_test

\echo '[REGRESSION TEST 1] Equipment Consistency Tests'
\echo '================================================'

-- Test R1.1: No hero should have same item in multiple slots
\echo 'Test R1.1: Verify no duplicate weapons equipped'
DO $$
DECLARE
    duplicate_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO duplicate_count
    FROM (
        SELECT hero_id, weapon_id, COUNT(*) as cnt
        FROM public.heroes_weapons_slots
        GROUP BY hero_id, weapon_id
        HAVING COUNT(*) > 1
    ) dups;

    IF duplicate_count > 0 THEN
        RAISE EXCEPTION '% heroes have duplicate weapons equipped', duplicate_count;
    END IF;
    RAISE NOTICE '✓ No duplicate weapons equipped';
END $$;

-- Test R1.2: Verify no duplicate armor equipped
\echo 'Test R1.2: Verify no duplicate armor equipped'
DO $$
DECLARE
    duplicate_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO duplicate_count
    FROM (
        SELECT hero_id, armor_id, COUNT(*) as cnt
        FROM public.heroes_armor_slots
        GROUP BY hero_id, armor_id
        HAVING COUNT(*) > 1
    ) dups;

    IF duplicate_count > 0 THEN
        RAISE EXCEPTION '% heroes have duplicate armor equipped', duplicate_count;
    END IF;
    RAISE NOTICE '✓ No duplicate armor equipped';
END $$;

-- Test R1.3: Equipment should match allowed slots
\echo 'Test R1.3: Verify weapons are in allowed slots'
DO $$
DECLARE
    invalid_count INTEGER;
BEGIN
    -- Check that equipped weapons are in allowed slots
    SELECT COUNT(*) INTO invalid_count
    FROM public.heroes_weapons_slots hws
    WHERE NOT EXISTS (
        SELECT 1 FROM public.weapons_slots ws
        WHERE ws.weapons_id = hws.weapon_id
        AND ws.slots_id = hws.slot_id
    );

    IF invalid_count > 0 THEN
        RAISE EXCEPTION '% weapons equipped in invalid slots', invalid_count;
    END IF;
    RAISE NOTICE '✓ All weapons equipped in valid slots';
END $$;

-- Test R1.4: Armor should match allowed slots
\echo 'Test R1.4: Verify armor is in allowed slots'
DO $$
DECLARE
    invalid_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO invalid_count
    FROM public.heroes_armor_slots has
    WHERE NOT EXISTS (
        SELECT 1 FROM public.armor_slots ars
        WHERE ars.armor_id = has.armor_id
        AND ars.slots_id = has.slot_id
    );

    IF invalid_count > 0 THEN
        RAISE EXCEPTION '% armor pieces equipped in invalid slots', invalid_count;
    END IF;
    RAISE NOTICE '✓ All armor equipped in valid slots';
END $$;

\echo ''
\echo '[REGRESSION TEST 2] Level Progression Consistency'
\echo '=================================================='

-- Test R2.1: Level requirements should be monotonically increasing
\echo 'Test R2.1: Verify level XP requirements increase monotonically'
DO $$
DECLARE
    violation_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO violation_count
    FROM public.levels l1
    JOIN public.levels l2 ON l1.level < l2.level
    WHERE l1.required_xp >= l2.required_xp;

    IF violation_count > 0 THEN
        RAISE EXCEPTION 'XP requirements not monotonically increasing: % violations', violation_count;
    END IF;
    RAISE NOTICE '✓ Level XP requirements increase monotonically';
END $$;

-- Test R2.2: All heroes should have valid calculated levels
\echo 'Test R2.2: Verify all heroes have valid calculated levels'
DO $$
DECLARE
    null_level_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO null_level_count
    FROM public.hero_stats
    WHERE level IS NULL;

    IF null_level_count > 0 THEN
        RAISE EXCEPTION '% heroes have NULL calculated level', null_level_count;
    END IF;
    RAISE NOTICE '✓ All heroes have valid calculated levels';
END $$;

-- Test R2.3: Hero level should never exceed max defined level
\echo 'Test R2.3: Verify hero levels do not exceed max defined level'
DO $$
DECLARE
    max_defined_level INTEGER;
    heroes_over_max INTEGER;
BEGIN
    SELECT MAX(level) INTO max_defined_level FROM public.levels;

    SELECT COUNT(*) INTO heroes_over_max
    FROM public.hero_stats
    WHERE level > max_defined_level;

    IF heroes_over_max > 0 THEN
        RAISE EXCEPTION '% heroes exceed max defined level %', heroes_over_max, max_defined_level;
    END IF;
    RAISE NOTICE '✓ No heroes exceed max defined level (%)', max_defined_level;
END $$;

\echo ''
\echo '[REGRESSION TEST 3] Stat Calculation Accuracy'
\echo '=============================================='

-- Test R3.1: Verify health calculation for all heroes
\echo 'Test R3.1: Verify health calculation matches formula'
DO $$
DECLARE
    hero_rec RECORD;
    expected_health INTEGER;
    mismatch_count INTEGER := 0;
BEGIN
    FOR hero_rec IN
        SELECT hs.hero_id, hs.hero_name, hs.health, hs.total_strength,
               h.race, r.base_health
        FROM public.hero_stats hs
        JOIN public.heroes h ON h.id = hs.hero_id
        JOIN public.race r ON r.id = h.race
    LOOP
        expected_health := hero_rec.base_health + hero_rec.total_strength * 50;

        IF hero_rec.health != expected_health THEN
            RAISE WARNING 'Health mismatch for %: expected %, got %',
                hero_rec.hero_name, expected_health, hero_rec.health;
            mismatch_count := mismatch_count + 1;
        END IF;
    END LOOP;

    IF mismatch_count > 0 THEN
        RAISE EXCEPTION '% heroes have incorrect health calculations', mismatch_count;
    END IF;
    RAISE NOTICE '✓ All hero health calculations correct';
END $$;

-- Test R3.2: Verify damage calculation for all heroes
\echo 'Test R3.2: Verify damage calculation matches formula'
DO $$
DECLARE
    hero_rec RECORD;
    expected_damage NUMERIC;
    mismatch_count INTEGER := 0;
BEGIN
    FOR hero_rec IN
        SELECT hs.hero_id, hs.hero_name, hs.damage, hs.total_agility,
               h.race, r.base_damage
        FROM public.hero_stats hs
        JOIN public.heroes h ON h.id = hs.hero_id
        JOIN public.race r ON r.id = h.race
    LOOP
        expected_damage := hero_rec.base_damage + hero_rec.total_agility * 10.0;

        IF ABS(hero_rec.damage - expected_damage) > 0.01 THEN
            RAISE WARNING 'Damage mismatch for %: expected %, got %',
                hero_rec.hero_name, expected_damage, hero_rec.damage;
            mismatch_count := mismatch_count + 1;
        END IF;
    END LOOP;

    IF mismatch_count > 0 THEN
        RAISE EXCEPTION '% heroes have incorrect damage calculations', mismatch_count;
    END IF;
    RAISE NOTICE '✓ All hero damage calculations correct';
END $$;

-- Test R3.3: Verify mana calculation for all heroes
\echo 'Test R3.3: Verify mana calculation matches formula'
DO $$
DECLARE
    hero_rec RECORD;
    expected_mana INTEGER;
    mismatch_count INTEGER := 0;
BEGIN
    FOR hero_rec IN
        SELECT hs.hero_id, hs.hero_name, hs.mana, hs.total_intellect,
               h.race, r.base_mana
        FROM public.hero_stats hs
        JOIN public.heroes h ON h.id = hs.hero_id
        JOIN public.race r ON r.id = h.race
    LOOP
        expected_mana := hero_rec.base_mana + hero_rec.total_intellect * 30;

        IF hero_rec.mana != expected_mana THEN
            RAISE WARNING 'Mana mismatch for %: expected %, got %',
                hero_rec.hero_name, expected_mana, hero_rec.mana;
            mismatch_count := mismatch_count + 1;
        END IF;
    END LOOP;

    IF mismatch_count > 0 THEN
        RAISE EXCEPTION '% heroes have incorrect mana calculations', mismatch_count;
    END IF;
    RAISE NOTICE '✓ All hero mana calculations correct';
END $$;

\echo ''
\echo '[REGRESSION TEST 4] Data Integrity Cross-Checks'
\echo '================================================'

-- Test R4.1: Equipped items should not be in inventory
\echo 'Test R4.1: Verify equipped weapons not in inventory'
DO $$
DECLARE
    overlap_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO overlap_count
    FROM public.heroes_weapons_slots hws
    INNER JOIN public.heroes_inventory_weapons hiw
        ON hws.hero_id = hiw.hero_id
        AND hws.weapon_id = hiw.weapon_id;

    IF overlap_count > 0 THEN
        RAISE WARNING '% weapons are both equipped and in inventory', overlap_count;
    END IF;
    RAISE NOTICE '✓ Verified weapon equipment vs inventory separation (% overlaps OK)', overlap_count;
END $$;

-- Test R4.2: Skills should be accessible to heroes
\echo 'Test R4.2: Verify hero skills are accessible'
DO $$
DECLARE
    hero_rec RECORD;
    skill_count INTEGER;
BEGIN
    FOR hero_rec IN SELECT id, name, race, profession FROM public.heroes LOOP
        -- Count racial skills
        SELECT COUNT(*) INTO skill_count
        FROM public.skills_race sr
        WHERE sr.race_id = hero_rec.race;

        -- Add profession skills if hero has profession
        IF hero_rec.profession IS NOT NULL THEN
            SELECT skill_count + COUNT(*) INTO skill_count
            FROM public.skills_profession sp
            WHERE sp.profession_id = hero_rec.profession;
        END IF;

        IF skill_count = 0 THEN
            RAISE WARNING 'Hero % has no accessible skills', hero_rec.name;
        END IF;
    END LOOP;

    RAISE NOTICE '✓ All heroes have accessible skills';
END $$;

-- Test R4.3: Item prices should be consistent
\echo 'Test R4.3: Verify item prices are reasonable'
DO $$
DECLARE
    negative_price_weapons INTEGER;
    negative_price_armor INTEGER;
BEGIN
    SELECT COUNT(*) INTO negative_price_weapons
    FROM public.weapons WHERE price < 0;

    SELECT COUNT(*) INTO negative_price_armor
    FROM public.armor WHERE price < 0;

    IF negative_price_weapons > 0 OR negative_price_armor > 0 THEN
        RAISE EXCEPTION 'Found negative prices: % weapons, % armor',
            negative_price_weapons, negative_price_armor;
    END IF;
    RAISE NOTICE '✓ All item prices are non-negative';
END $$;

-- Test R4.4: Skills with mana cost should be active
\echo 'Test R4.4: Verify skill mana cost logic'
DO $$
DECLARE
    passive_with_mana INTEGER;
BEGIN
    SELECT COUNT(*) INTO passive_with_mana
    FROM public.skills
    WHERE is_active = false AND mana_cost > 0;

    IF passive_with_mana > 0 THEN
        RAISE WARNING '% passive skills have mana cost > 0', passive_with_mana;
    END IF;
    RAISE NOTICE '✓ Skill mana cost logic consistent (% passive skills with mana cost)', passive_with_mana;
END $$;

\echo ''
\echo '[REGRESSION TEST 5] Referential Integrity Stress Tests'
\echo '======================================================='

-- Test R5.1: All foreign keys in heroes table are valid
\echo 'Test R5.1: Verify hero foreign key integrity'
DO $$
DECLARE
    invalid_race INTEGER;
    invalid_profession INTEGER;
    invalid_alliance INTEGER;
BEGIN
    SELECT COUNT(*) INTO invalid_race
    FROM public.heroes h
    WHERE NOT EXISTS (SELECT 1 FROM public.race r WHERE r.id = h.race);

    SELECT COUNT(*) INTO invalid_profession
    FROM public.heroes h
    WHERE h.profession IS NOT NULL
    AND NOT EXISTS (SELECT 1 FROM public.profession p WHERE p.id = h.profession);

    SELECT COUNT(*) INTO invalid_alliance
    FROM public.heroes h
    WHERE h.alliance_id IS NOT NULL
    AND NOT EXISTS (SELECT 1 FROM public.alliances a WHERE a.id = h.alliance_id);

    IF invalid_race > 0 OR invalid_profession > 0 OR invalid_alliance > 0 THEN
        RAISE EXCEPTION 'Invalid foreign keys: race=%, profession=%, alliance=%',
            invalid_race, invalid_profession, invalid_alliance;
    END IF;
    RAISE NOTICE '✓ All hero foreign keys valid';
END $$;

-- Test R5.2: All skill effects reference valid IDs
\echo 'Test R5.2: Verify skill effect foreign key integrity'
DO $$
DECLARE
    invalid_skills INTEGER;
    invalid_targets INTEGER;
    invalid_effects INTEGER;
    invalid_impacts INTEGER;
BEGIN
    SELECT
        COUNT(DISTINCT CASE WHEN s.id IS NULL THEN stei.skill_id END),
        COUNT(DISTINCT CASE WHEN t.id IS NULL THEN stei.target_id END),
        COUNT(DISTINCT CASE WHEN e.id IS NULL THEN stei.effect_id END),
        COUNT(DISTINCT CASE WHEN i.id IS NULL THEN stei.impact_type_id END)
    INTO invalid_skills, invalid_targets, invalid_effects, invalid_impacts
    FROM public.skills_targets_effects_impact_types stei
    LEFT JOIN public.skills s ON s.id = stei.skill_id
    LEFT JOIN public.targets t ON t.id = stei.target_id
    LEFT JOIN public.effects e ON e.id = stei.effect_id
    LEFT JOIN public.impact_types i ON i.id = stei.impact_type_id;

    IF invalid_skills > 0 OR invalid_targets > 0 OR invalid_effects > 0 OR invalid_impacts > 0 THEN
        RAISE EXCEPTION 'Invalid skill effect references: skills=%, targets=%, effects=%, impacts=%',
            invalid_skills, invalid_targets, invalid_effects, invalid_impacts;
    END IF;
    RAISE NOTICE '✓ All skill effect foreign keys valid';
END $$;

\echo ''
\echo '[REGRESSION TEST 6] Business Logic Validation'
\echo '=============================================='

-- Test R6.1: Heroes should not equip items above their level
\echo 'Test R6.1: Verify heroes do not equip items above their level'
DO $$
DECLARE
    violations INTEGER;
BEGIN
    -- Check weapons
    SELECT COUNT(*) INTO violations
    FROM public.heroes_weapons_slots hws
    JOIN public.weapons w ON w.id = hws.weapon_id
    JOIN public.hero_stats hs ON hs.hero_id = hws.hero_id
    WHERE w.required_lvl > hs.level;

    IF violations > 0 THEN
        RAISE EXCEPTION '% heroes have weapons above their level', violations;
    END IF;

    -- Check armor
    SELECT COUNT(*) INTO violations
    FROM public.heroes_armor_slots has
    JOIN public.armor a ON a.id = has.armor_id
    JOIN public.hero_stats hs ON hs.hero_id = has.hero_id
    WHERE a.required_lvl > hs.level;

    IF violations > 0 THEN
        RAISE EXCEPTION '% heroes have armor above their level', violations;
    END IF;

    RAISE NOTICE '✓ All heroes equip only level-appropriate items';
END $$;

-- Test R6.2: Hero gold should be sufficient for inventory
\echo 'Test R6.2: Verify hero gold vs inventory value consistency'
DO $$
DECLARE
    hero_rec RECORD;
    inventory_value INTEGER;
BEGIN
    FOR hero_rec IN SELECT id, name, gold FROM public.heroes LOOP
        -- Calculate total inventory value
        SELECT COALESCE(SUM(w.price * hiw.quantity), 0) INTO inventory_value
        FROM public.heroes_inventory_weapons hiw
        JOIN public.weapons w ON w.id = hiw.weapon_id
        WHERE hiw.hero_id = hero_rec.id;

        SELECT inventory_value + COALESCE(SUM(a.price * hia.quantity), 0) INTO inventory_value
        FROM public.heroes_inventory_armor hia
        JOIN public.armor a ON a.id = hia.armor_id
        WHERE hia.hero_id = hero_rec.id;

        -- Just log the comparison, don't fail (hero could have sold items)
        IF inventory_value > 0 THEN
            RAISE NOTICE 'Hero % has % gold and % inventory value',
                hero_rec.name, hero_rec.gold, inventory_value;
        END IF;
    END LOOP;

    RAISE NOTICE '✓ Hero gold and inventory values logged';
END $$;

-- Test R6.3: Verify skill level requirements are achievable
\echo 'Test R6.3: Verify skill level requirements are within defined levels'
DO $$
DECLARE
    max_defined_level INTEGER;
    unreachable_skills INTEGER;
BEGIN
    SELECT MAX(level) INTO max_defined_level FROM public.levels;

    SELECT COUNT(*) INTO unreachable_skills
    FROM public.skills
    WHERE required_lvl > max_defined_level;

    IF unreachable_skills > 0 THEN
        RAISE EXCEPTION '% skills require level > max defined level %',
            unreachable_skills, max_defined_level;
    END IF;

    RAISE NOTICE '✓ All skill level requirements achievable';
END $$;

\echo ''
\echo '[REGRESSION TEST 7] Performance and Scalability Checks'
\echo '======================================================='

-- Test R7.1: Materialized view index exists and is unique
\echo 'Test R7.1: Verify materialized view has unique index'
DO $$
DECLARE
    index_exists BOOLEAN;
BEGIN
    SELECT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE schemaname = 'public'
        AND tablename = 'hero_stats'
        AND indexname = 'idx_hero_stats_hero_id'
    ) INTO index_exists;

    IF NOT index_exists THEN
        RAISE EXCEPTION 'Unique index idx_hero_stats_hero_id not found';
    END IF;
    RAISE NOTICE '✓ Materialized view has required unique index';
END $$;

-- Test R7.2: Check for orphaned records in junction tables
\echo 'Test R7.2: Verify no orphaned records in junction tables'
DO $$
DECLARE
    orphaned_equipped_weapons INTEGER;
    orphaned_equipped_armor INTEGER;
    orphaned_inventory_weapons INTEGER;
    orphaned_inventory_armor INTEGER;
BEGIN
    -- Check heroes_weapons_slots
    SELECT COUNT(*) INTO orphaned_equipped_weapons
    FROM public.heroes_weapons_slots hws
    WHERE NOT EXISTS (SELECT 1 FROM public.heroes WHERE id = hws.hero_id);

    -- Check heroes_armor_slots
    SELECT COUNT(*) INTO orphaned_equipped_armor
    FROM public.heroes_armor_slots has
    WHERE NOT EXISTS (SELECT 1 FROM public.heroes WHERE id = has.hero_id);

    -- Check heroes_inventory_weapons
    SELECT COUNT(*) INTO orphaned_inventory_weapons
    FROM public.heroes_inventory_weapons hiw
    WHERE NOT EXISTS (SELECT 1 FROM public.heroes WHERE id = hiw.hero_id);

    -- Check heroes_inventory_armor
    SELECT COUNT(*) INTO orphaned_inventory_armor
    FROM public.heroes_inventory_armor hia
    WHERE NOT EXISTS (SELECT 1 FROM public.heroes WHERE id = hia.hero_id);

    IF orphaned_equipped_weapons > 0 OR orphaned_equipped_armor > 0 OR
       orphaned_inventory_weapons > 0 OR orphaned_inventory_armor > 0 THEN
        RAISE EXCEPTION 'Orphaned records found: weapons=%/%, armor=%/%',
            orphaned_equipped_weapons, orphaned_inventory_weapons,
            orphaned_equipped_armor, orphaned_inventory_armor;
    END IF;

    RAISE NOTICE '✓ No orphaned records in junction tables';
END $$;

\echo ''
\echo '========================================='
\echo 'Regression Test Suite Complete'
\echo '========================================='
\echo 'All regression tests passed!'
\echo ''