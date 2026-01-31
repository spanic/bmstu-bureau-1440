BEGIN;

-- Lookup tables (no foreign keys)

DROP TABLE IF EXISTS public.race CASCADE;

CREATE TABLE IF NOT EXISTS public.race
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    description varchar,
    base_health integer NOT NULL DEFAULT 0 CHECK (base_health >= 0),
    base_mana integer NOT NULL DEFAULT 0 CHECK (base_mana >= 0),
    base_damage double precision NOT NULL DEFAULT 0.0,
    base_strength integer NOT NULL DEFAULT 0 CHECK (base_strength >= 0),
    base_agility integer NOT NULL DEFAULT 0 CHECK (base_agility >= 0),
    base_intellect integer NOT NULL DEFAULT 0 CHECK (base_intellect >= 0)
);

DROP TABLE IF EXISTS public.profession CASCADE;

CREATE TABLE IF NOT EXISTS public.profession
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    description varchar
);

DROP TABLE IF EXISTS public.alliances CASCADE;

CREATE TABLE IF NOT EXISTS public.alliances
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    description varchar
);

DROP TABLE IF EXISTS public.levels CASCADE;

CREATE TABLE IF NOT EXISTS public.levels
(
    level smallint PRIMARY KEY CHECK (level > 0),
    required_xp integer NOT NULL UNIQUE CHECK (required_xp >= 0)
);

DROP TABLE IF EXISTS public.skills CASCADE;

CREATE TABLE IF NOT EXISTS public.skills
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    is_active boolean NOT NULL,
    mana_cost integer DEFAULT 0 CHECK (mana_cost >= 0),
    required_lvl smallint NOT NULL DEFAULT 1 REFERENCES public.levels (level),
    cooldown_time_ms integer DEFAULT 0 CHECK (cooldown_time_ms >= 0),
    range integer NOT NULL DEFAULT 0 CHECK (range >= 0)
);

DROP TABLE IF EXISTS public.effects CASCADE;

CREATE TABLE IF NOT EXISTS public.effects
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    code varchar(50) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS public.targets CASCADE;

CREATE TABLE IF NOT EXISTS public.targets
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    code varchar(50) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS public.impact_types CASCADE;

CREATE TABLE IF NOT EXISTS public.impact_types
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    code varchar(50) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS public.slots CASCADE;

CREATE TABLE IF NOT EXISTS public.slots
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    code varchar(50) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS public.weapons CASCADE;

CREATE TABLE IF NOT EXISTS public.weapons
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    price integer NOT NULL CHECK (price >= 0),
    range integer NOT NULL DEFAULT 0 CHECK (range >= 0),
    required_lvl smallint NOT NULL DEFAULT 1 REFERENCES public.levels (level),
    cooldown_time_ms integer NOT NULL DEFAULT 0 CHECK (cooldown_time_ms >= 0),
    mana_cost integer DEFAULT 0 CHECK (mana_cost >= 0)
);

DROP TABLE IF EXISTS public.armor CASCADE;

CREATE TABLE IF NOT EXISTS public.armor
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    price integer NOT NULL CHECK (price >= 0),
    required_lvl smallint NOT NULL DEFAULT 1 REFERENCES public.levels (level)
);

-- Entity tables (with foreign keys to lookup tables)

DROP TABLE IF EXISTS public.heroes CASCADE;

CREATE TABLE IF NOT EXISTS public.heroes
(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    sex varchar(10) NOT NULL CHECK (sex IN ('male', 'female')),
    race integer NOT NULL REFERENCES public.race (id) ON UPDATE CASCADE ON DELETE CASCADE,
    profession integer REFERENCES public.profession (id) ON UPDATE CASCADE ON DELETE CASCADE,
    created_date timestamp without time zone NOT NULL,
    experience_points integer NOT NULL DEFAULT 0 CHECK (experience_points >= 0),
    gold integer NOT NULL DEFAULT 0 CHECK (gold >= 0),
    strength integer NOT NULL DEFAULT 0 CHECK (strength >= 0),
    agility integer NOT NULL DEFAULT 0 CHECK (agility >= 0),
    intellect integer NOT NULL DEFAULT 0 CHECK (intellect >= 0),
    alliance_id integer REFERENCES public.alliances (id)
);

-- Junction tables

DROP TABLE IF EXISTS public.skills_race CASCADE;

CREATE TABLE IF NOT EXISTS public.skills_race
(
    skills_id integer NOT NULL REFERENCES public.skills (id) ON UPDATE CASCADE ON DELETE CASCADE,
    race_id integer NOT NULL REFERENCES public.race (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (skills_id, race_id)
);

DROP TABLE IF EXISTS public.skills_profession CASCADE;

CREATE TABLE IF NOT EXISTS public.skills_profession
(
    skills_id integer NOT NULL REFERENCES public.skills (id) ON UPDATE CASCADE ON DELETE CASCADE,
    profession_id integer NOT NULL REFERENCES public.profession (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (skills_id, profession_id)
);

DROP TABLE IF EXISTS public.skills_targets_effects_impact_types CASCADE;

CREATE TABLE IF NOT EXISTS public.skills_targets_effects_impact_types
(
    skill_id integer NOT NULL REFERENCES public.skills (id),
    target_id integer NOT NULL REFERENCES public.targets (id),
    effect_id integer NOT NULL REFERENCES public.effects (id),
    impact_type_id integer NOT NULL REFERENCES public.impact_types (id),
    value double precision NOT NULL DEFAULT 0,
    interval_ms integer CHECK (interval_ms >= 0),
    total_time_ms integer CHECK (total_time_ms >= 0),
    PRIMARY KEY (skill_id, target_id, effect_id, impact_type_id)
);

DROP TABLE IF EXISTS public.weapons_slots CASCADE;

CREATE TABLE IF NOT EXISTS public.weapons_slots
(
    weapons_id integer NOT NULL REFERENCES public.weapons (id),
    slots_id integer NOT NULL REFERENCES public.slots (id),
    PRIMARY KEY (weapons_id, slots_id)
);

DROP TABLE IF EXISTS public.weapons_targets_effects_impact_types CASCADE;

CREATE TABLE IF NOT EXISTS public.weapons_targets_effects_impact_types
(
    weapon_id integer NOT NULL REFERENCES public.weapons (id),
    effect_id integer NOT NULL REFERENCES public.effects (id),
    impact_type_id integer NOT NULL REFERENCES public.impact_types (id),
    target_id integer NOT NULL REFERENCES public.targets (id),
    value double precision DEFAULT 0,
    interval_ms integer CHECK (interval_ms >= 0),
    total_time_ms integer CHECK (total_time_ms >= 0),
    PRIMARY KEY (weapon_id, effect_id, impact_type_id, target_id)
);

DROP TABLE IF EXISTS public.heroes_weapons_slots CASCADE;

CREATE TABLE IF NOT EXISTS public.heroes_weapons_slots
(
    hero_id integer NOT NULL REFERENCES public.heroes (id),
    weapon_id integer NOT NULL REFERENCES public.weapons (id),
    slot_id integer NOT NULL REFERENCES public.slots (id),
    PRIMARY KEY (hero_id, weapon_id, slot_id)
);

DROP TABLE IF EXISTS public.armor_slots CASCADE;

CREATE TABLE IF NOT EXISTS public.armor_slots
(
    armor_id integer NOT NULL REFERENCES public.armor (id),
    slots_id integer NOT NULL REFERENCES public.slots (id),
    PRIMARY KEY (armor_id, slots_id)
);

DROP TABLE IF EXISTS public.armor_targets_effects_impact_types CASCADE;

CREATE TABLE IF NOT EXISTS public.armor_targets_effects_impact_types
(
    armor_id integer NOT NULL REFERENCES public.armor (id),
    effect_id integer NOT NULL REFERENCES public.effects (id),
    impact_type_id integer NOT NULL REFERENCES public.impact_types (id),
    target_id integer NOT NULL REFERENCES public.targets (id),
    value double precision DEFAULT 0,
    interval_ms integer CHECK (interval_ms >= 0),
    PRIMARY KEY (effect_id, impact_type_id, target_id, armor_id)
);

DROP TABLE IF EXISTS public.heroes_armor_slots CASCADE;

CREATE TABLE IF NOT EXISTS public.heroes_armor_slots
(
    hero_id integer NOT NULL REFERENCES public.heroes (id),
    armor_id integer NOT NULL REFERENCES public.armor (id),
    slot_id integer NOT NULL REFERENCES public.slots (id),
    PRIMARY KEY (hero_id, slot_id, armor_id)
);

-- Inventory tables (purchased but not equipped items)

DROP TABLE IF EXISTS public.heroes_inventory_weapons CASCADE;

CREATE TABLE IF NOT EXISTS public.heroes_inventory_weapons
(
    hero_id integer NOT NULL REFERENCES public.heroes (id),
    weapon_id integer NOT NULL REFERENCES public.weapons (id),
    quantity integer NOT NULL DEFAULT 1 CHECK (quantity > 0),
    PRIMARY KEY (hero_id, weapon_id)
);

DROP TABLE IF EXISTS public.heroes_inventory_armor CASCADE;

CREATE TABLE IF NOT EXISTS public.heroes_inventory_armor
(
    hero_id integer NOT NULL REFERENCES public.heroes (id),
    armor_id integer NOT NULL REFERENCES public.armor (id),
    quantity integer NOT NULL DEFAULT 1 CHECK (quantity > 0),
    PRIMARY KEY (hero_id, armor_id)
);

-- Materialized view: precomputed hero stats
-- Refresh after any change to heroes, race, or levels:
--   REFRESH MATERIALIZED VIEW CONCURRENTLY public.hero_stats;

DROP MATERIALIZED VIEW IF EXISTS public.hero_stats;

CREATE MATERIALIZED VIEW public.hero_stats AS
SELECT
    h.id AS hero_id,
    h.name AS hero_name,
    h.sex,
    r.name AS race,
    p.name AS profession,
    a.name AS alliance,
    (
        SELECT MAX(l.level)
        FROM public.levels l
        WHERE l.required_xp <= h.experience_points
    ) AS level,
    h.experience_points,
    h.gold,
    h.strength  + r.base_strength AS total_strength,
    h.agility   + r.base_agility AS total_agility,
    h.intellect + r.base_intellect AS total_intellect,
    r.base_health + (h.strength + r.base_strength) * 50 AS health,
    r.base_damage + (h.agility  + r.base_agility)  * 10.0 AS damage,
    r.base_mana   + (h.intellect + r.base_intellect) * 30 AS mana
FROM public.heroes h
JOIN public.race r ON r.id = h.race
LEFT JOIN public.profession p ON p.id = h.profession
LEFT JOIN public.alliances a ON a.id = h.alliance_id;

-- Unique index enables CONCURRENTLY refresh
CREATE UNIQUE INDEX idx_hero_stats_hero_id ON public.hero_stats (hero_id);

END;
