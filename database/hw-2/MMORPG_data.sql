BEGIN;

-- Races
INSERT INTO public.race (name, description, base_health, base_mana, base_damage, base_strength, base_agility, base_intellect) VALUES
    ('Human',  'Versatile and adaptive',       100, 80,  10.0, 10, 10, 10),
    ('Elf',    'Graceful and magically gifted', 80,  120, 8.0,  6,  14, 16),
    ('Dwarf',  'Sturdy and resilient',          130, 60,  12.0, 14, 6,  8),
    ('Orc',    'Fierce and powerful',            120, 40,  15.0, 16, 10, 4),
    ('Undead', 'Relentless and cunning',         90,  70,  11.0, 8,  12, 12);

-- Professions
INSERT INTO public.profession (name, description) VALUES
    ('Warrior', 'Master of melee combat and heavy armor'),
    ('Mage',    'Wielder of arcane and elemental magic'),
    ('Rogue',   'Expert in stealth and precision strikes'),
    ('Priest',  'Healer and protector of allies'),
    ('Ranger',  'Skilled marksman and survivalist');

-- Alliances
INSERT INTO public.alliances (name, description) VALUES
    ('The Silver Hand',  'Paladins devoted to justice and order'),
    ('Shadow Council',   'A secretive faction of dark schemers'),
    ('Iron Forge',       'Dwarven brotherhood of master smiths'),
    ('Natures Wrath',    'Guardians of the wild and natural balance'),
    ('The Arcane Circle', 'An elite society of the most powerful mages');

-- Levels
INSERT INTO public.levels (level, required_xp) VALUES
    (1,  0),
    (2,  100),
    (3,  300),
    (4,  600),
    (5,  1000),
    (6,  1500),
    (7,  2100),
    (8,  2800),
    (9,  3600),
    (10, 4500);

-- Skills
INSERT INTO public.skills (name, is_active, mana_cost, required_lvl, cooldown_time_ms, range) VALUES
    ('Fireball',      true,  30, 1, 3000,  25),
    ('Heal',          true,  25, 1, 5000,  15),
    ('Stealth',       true,  10, 3, 10000, 0),
    ('Shield Wall',   true,  0,  5, 15000, 0),
    ('Backstab',      true,  15, 2, 2000,  1),
    ('Arcane Shield', false, 0,  2, 0,     0),
    ('Thunder Strike', true, 40, 4, 4000,  20);

-- Effects (as specified)
INSERT INTO public.effects (name, code) VALUES
    ('Damage',          'damage'),
    ('Heal',            'heal'),
    ('Vulnerability',   'vulnerability'),
    ('Invulnerability', 'invulnerability');

-- Targets (as specified)
INSERT INTO public.targets (name, code) VALUES
    ('Self',        'self'),
    ('Allies',      'allies'),
    ('Enemy',       'enemy'),
    ('Area Radius', 'area_radius');

-- Impact types (as specified)
INSERT INTO public.impact_types (name, code) VALUES
    ('Physical', 'physical'),
    ('Magical',  'magical'),
    ('Fire',     'fire'),
    ('Water',    'water'),
    ('Earth',    'earth'),
    ('Air',      'air');

-- Slots
INSERT INTO public.slots (name, code) VALUES
    ('Head',      'head'),
    ('Chest',     'chest'),
    ('Main Hand', 'main_hand'),
    ('Off Hand',  'off_hand'),
    ('Legs',      'legs'),
    ('Feet',      'feet');

-- Weapons
INSERT INTO public.weapons (name, price, range, required_lvl, cooldown_time_ms, mana_cost) VALUES
    ('Iron Sword',    50,  1,  1, 1500, 0),
    ('Fire Staff',    200, 20, 3, 2000, 10),
    ('Longbow',       150, 30, 2, 2500, 0),
    ('Shadow Dagger', 300, 1,  4, 1000, 5),
    ('War Hammer',    400, 1,  5, 3000, 0);

-- Armor
INSERT INTO public.armor (name, price, required_lvl) VALUES
    ('Iron Helmet',   30,  1),
    ('Chainmail',     100, 2),
    ('Plate Armor',   350, 5),
    ('Leather Boots', 25,  1),
    ('Mage Robe',     180, 3);

-- Heroes
INSERT INTO public.heroes (name, sex, race, profession, created_date, experience_points, gold, strength, agility, intellect, alliance_id) VALUES
    ('Arthas',   'male',   1, 1, '2025-01-15 10:00:00', 1200, 500,  18, 10, 8,  1),
    ('Sylvanas', 'female', 5, 5, '2025-02-20 14:30:00', 950,  350,  10, 18, 12, 2),
    ('Thrall',   'male',   4, 2, '2025-03-10 09:15:00', 1800, 800,  14, 8,  16, 4),
    ('Jaina',    'female', 1, 2, '2025-04-05 16:45:00', 2200, 1200, 6,  10, 22, 5),
    ('Gimli',    'male',   3, 1, '2025-05-12 11:20:00', 700,  250,  20, 6,  6,  3);

-- Skills ↔ Races (innate racial abilities — no overlap with profession skills)
INSERT INTO public.skills_race (skills_id, race_id) VALUES
    (2, 1),  -- Heal → Human
    (2, 2),  -- Heal → Elf
    (2, 3),  -- Heal → Dwarf
    (3, 2),  -- Stealth → Elf
    (3, 4),  -- Stealth → Orc
    (3, 5),  -- Stealth → Undead
    (4, 1),  -- Shield Wall → Human
    (4, 3),  -- Shield Wall → Dwarf
    (4, 4);  -- Shield Wall → Orc

-- Skills ↔ Professions (trained abilities — no overlap with race skills)
INSERT INTO public.skills_profession (skills_id, profession_id) VALUES
    (1, 2),  -- Fireball → Mage
    (5, 3),  -- Backstab → Rogue
    (5, 5),  -- Backstab → Ranger
    (6, 2),  -- Arcane Shield → Mage
    (6, 4),  -- Arcane Shield → Priest
    (7, 1),  -- Thunder Strike → Warrior
    (7, 2);  -- Thunder Strike → Mage

-- Skills ↔ Targets / Effects / Impact types
INSERT INTO public.skills_targets_effects_impact_types (skill_id, target_id, effect_id, impact_type_id, value, interval_ms, total_time_ms) VALUES
    (1, 3, 1, 3, 45.0,  NULL, NULL),  -- Fireball → Enemy, Damage, Fire
    (1, 4, 1, 3, 25.0,  NULL, NULL),  -- Fireball → Area, Damage, Fire
    (2, 1, 2, 2, 30.0,  NULL, NULL),  -- Heal → Self, Heal, Magical
    (2, 2, 2, 2, 20.0,  NULL, NULL),  -- Heal → Allies, Heal, Magical
    (3, 1, 4, 2, 1.0,   NULL, 8000),  -- Stealth → Self, Invulnerability, Magical
    (4, 1, 4, 1, 1.0,   NULL, 6000),  -- Shield Wall → Self, Invulnerability, Physical
    (5, 3, 1, 1, 55.0,  NULL, NULL),  -- Backstab → Enemy, Damage, Physical
    (7, 4, 1, 6, 60.0,  NULL, NULL),  -- Thunder Strike → Area, Damage, Air
    (7, 3, 3, 6, 0.5,   3000, 9000);  -- Thunder Strike → Enemy, Vulnerability, Air

-- Weapons ↔ Slots
INSERT INTO public.weapons_slots (weapons_id, slots_id) VALUES
    (1, 3),  -- Iron Sword → Main Hand
    (2, 3),  -- Fire Staff → Main Hand
    (3, 3),  -- Longbow → Main Hand
    (4, 3),  -- Shadow Dagger → Main Hand
    (4, 4),  -- Shadow Dagger → Off Hand
    (5, 3);  -- War Hammer → Main Hand

-- Weapons ↔ Targets / Effects / Impact types
INSERT INTO public.weapons_targets_effects_impact_types (weapon_id, effect_id, impact_type_id, target_id, value, interval_ms, total_time_ms) VALUES
    (1, 1, 1, 3, 12.0, NULL, NULL),  -- Iron Sword → Damage, Physical, Enemy
    (2, 1, 3, 3, 18.0, NULL, NULL),  -- Fire Staff → Damage, Fire, Enemy
    (2, 1, 3, 4, 8.0,  NULL, NULL),  -- Fire Staff → Damage, Fire, Area
    (3, 1, 1, 3, 15.0, NULL, NULL),  -- Longbow → Damage, Physical, Enemy
    (4, 1, 1, 3, 20.0, NULL, NULL),  -- Shadow Dagger → Damage, Physical, Enemy
    (5, 1, 1, 3, 25.0, NULL, NULL),  -- War Hammer → Damage, Physical, Enemy
    (5, 1, 5, 4, 10.0, NULL, NULL);  -- War Hammer → Damage, Earth, Area

-- Heroes ↔ Weapons ↔ Slots
INSERT INTO public.heroes_weapons_slots (hero_id, weapon_id, slot_id) VALUES
    (1, 1, 3),  -- Arthas → Iron Sword in Main Hand
    (2, 3, 3),  -- Sylvanas → Longbow in Main Hand
    (3, 2, 3),  -- Thrall → Fire Staff in Main Hand
    (4, 2, 3),  -- Jaina → Fire Staff in Main Hand
    (5, 5, 3);  -- Gimli → War Hammer in Main Hand

-- Armor ↔ Slots
INSERT INTO public.armor_slots (armor_id, slots_id) VALUES
    (1, 1),  -- Iron Helmet → Head
    (2, 2),  -- Chainmail → Chest
    (3, 2),  -- Plate Armor → Chest
    (4, 6),  -- Leather Boots → Feet
    (5, 2);  -- Mage Robe → Chest

-- Armor ↔ Targets / Effects / Impact types
INSERT INTO public.armor_targets_effects_impact_types (armor_id, effect_id, impact_type_id, target_id, value, interval_ms) VALUES
    (1, 4, 1, 1, 5.0,  NULL),  -- Iron Helmet → Invulnerability, Physical, Self
    (2, 4, 1, 1, 12.0, NULL),  -- Chainmail → Invulnerability, Physical, Self
    (3, 4, 1, 1, 20.0, NULL),  -- Plate Armor → Invulnerability, Physical, Self
    (3, 4, 3, 1, 8.0,  NULL),  -- Plate Armor → Invulnerability, Fire, Self
    (4, 4, 1, 1, 3.0,  NULL),  -- Leather Boots → Invulnerability, Physical, Self
    (5, 4, 2, 1, 15.0, NULL);  -- Mage Robe → Invulnerability, Magical, Self

-- Heroes ↔ Armor ↔ Slots
INSERT INTO public.heroes_armor_slots (hero_id, armor_id, slot_id) VALUES
    (1, 1, 1),  -- Arthas → Iron Helmet on Head
    (1, 2, 2),  -- Arthas → Chainmail on Chest
    (2, 4, 6),  -- Sylvanas → Leather Boots on Feet
    (3, 5, 2),  -- Thrall → Mage Robe on Chest
    (4, 5, 2),  -- Jaina → Mage Robe on Chest
    (5, 3, 2),  -- Gimli → Plate Armor on Chest
    (5, 1, 1);  -- Gimli → Iron Helmet on Head

-- Inventory: weapons (purchased but not equipped)
INSERT INTO public.heroes_inventory_weapons (hero_id, weapon_id, quantity) VALUES
    (1, 4, 1),  -- Arthas has a Shadow Dagger in bag
    (2, 4, 1),  -- Sylvanas has a Shadow Dagger in bag
    (3, 5, 1),  -- Thrall has a War Hammer in bag
    (4, 1, 1),  -- Jaina has an Iron Sword in bag
    (5, 3, 1);  -- Gimli has a Longbow in bag

-- Inventory: armor (purchased but not equipped)
INSERT INTO public.heroes_inventory_armor (hero_id, armor_id, quantity) VALUES
    (1, 4, 1),  -- Arthas has Leather Boots in bag
    (2, 1, 1),  -- Sylvanas has an Iron Helmet in bag
    (3, 2, 1),  -- Thrall has a Chainmail in bag
    (4, 4, 2),  -- Jaina has 2x Leather Boots in bag
    (5, 2, 1);  -- Gimli has a Chainmail in bag

END;
