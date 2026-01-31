-- ============================================================
-- In-game queries
-- Replace :hero_id with the actual hero ID (e.g. 1 for Arthas)
-- Replace :slot_code with the slot code (e.g. 'main_hand', 'chest')
-- Usage in psql:  \set hero_id 1  \set slot_code '''main_hand'''
-- ============================================================


-- 1) Hero profile: name, level, race, profession, health, mana, total stats
--    (reads from the precomputed materialized view)
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
WHERE hero_id = :hero_id;


-- 2) All hero's spells, marked by source (race or profession — mutually exclusive)
WITH hero AS (
    SELECT id, race, profession
    FROM public.heroes
    WHERE id = :hero_id
)
SELECT
    s.name AS skill_name,
    s.is_active,
    s.mana_cost,
    s.required_lvl,
    s.cooldown_time_ms,
    s.range,
    'race' AS source
FROM public.skills_race sr
JOIN hero h ON sr.race_id = h.race
JOIN public.skills s ON s.id = sr.skills_id

UNION ALL

SELECT
    s.name AS skill_name,
    s.is_active,
    s.mana_cost,
    s.required_lvl,
    s.cooldown_time_ms,
    s.range,
    'profession' AS source
FROM public.skills_profession sp
JOIN hero h ON sp.profession_id = h.profession
JOIN public.skills s ON s.id = sp.skills_id

ORDER BY source, required_lvl, skill_name;


-- 3) All hero's armor in each slot
SELECT
    sl.name AS slot,
    a.name AS armor_name,
    a.price,
    a.required_lvl
FROM public.heroes_armor_slots has
JOIN public.armor a ON a.id = has.armor_id
JOIN public.slots sl ON sl.id = has.slot_id
WHERE has.hero_id = :hero_id
ORDER BY sl.name;


-- 4) All hero's weapons in each slot
SELECT
    sl.name AS slot,
    w.name AS weapon_name,
    w.price,
    w.range,
    w.required_lvl,
    w.cooldown_time_ms,
    w.mana_cost
FROM public.heroes_weapons_slots hws
JOIN public.weapons w ON w.id = hws.weapon_id
JOIN public.slots sl ON sl.id = hws.slot_id
WHERE hws.hero_id = :hero_id
ORDER BY sl.name;


-- 5) Available items (weapons & armor) for hero by level, filtered by slot
--    e.g. "What can Arthas equip in his Main Hand?"
WITH hero_level AS (
    SELECT MAX(l.level) AS level
    FROM public.levels l
    JOIN public.heroes h ON l.required_xp <= h.experience_points
    WHERE h.id = :hero_id
)
SELECT
    'weapon' AS item_type,
    w.name AS item_name,
    w.price,
    w.required_lvl,
    w.range,
    w.cooldown_time_ms,
    w.mana_cost
FROM public.weapons w
JOIN public.weapons_slots ws ON ws.weapons_id = w.id
JOIN public.slots sl ON sl.id = ws.slots_id
CROSS JOIN hero_level hl
WHERE sl.code = :slot_code
  AND w.required_lvl <= hl.level

UNION ALL

SELECT
    'armor' AS item_type,
    a.name AS item_name,
    a.price,
    a.required_lvl,
    NULL AS range,
    NULL AS cooldown_time_ms,
    NULL AS mana_cost
FROM public.armor a
JOIN public.armor_slots ars ON ars.armor_id = a.id
JOIN public.slots sl ON sl.id = ars.slots_id
CROSS JOIN hero_level hl
WHERE sl.code = :slot_code
  AND a.required_lvl <= hl.level

ORDER BY item_type, required_lvl, item_name;


-- 6) Hero's inventory (purchased but not equipped items)
SELECT
    'weapon' AS item_type,
    w.name AS item_name,
    w.price,
    hiw.quantity
FROM public.heroes_inventory_weapons hiw
JOIN public.weapons w ON w.id = hiw.weapon_id
WHERE hiw.hero_id = :hero_id

UNION ALL

SELECT
    'armor' AS item_type,
    a.name AS item_name,
    a.price,
    hia.quantity
FROM public.heroes_inventory_armor hia
JOIN public.armor a ON a.id = hia.armor_id
WHERE hia.hero_id = :hero_id

ORDER BY item_type, item_name;


-- 7) Shop: items the hero can afford AND meets the level requirement for
WITH hero_info AS (
    SELECT
        h.gold,
        (SELECT MAX(l.level) FROM public.levels l WHERE l.required_xp <= h.experience_points) AS level
    FROM public.heroes h
    WHERE h.id = :hero_id
)
SELECT
    'weapon' AS item_type,
    w.name AS item_name,
    w.price,
    w.required_lvl
FROM public.weapons w
CROSS JOIN hero_info hi
WHERE w.price <= hi.gold
  AND w.required_lvl <= hi.level

UNION ALL

SELECT
    'armor' AS item_type,
    a.name AS item_name,
    a.price,
    a.required_lvl
FROM public.armor a
CROSS JOIN hero_info hi
WHERE a.price <= hi.gold
  AND a.required_lvl <= hi.level

ORDER BY item_type, price, item_name;
