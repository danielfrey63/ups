// Corrections

// Rhizocarpon geographicum: remove doublicate entry
SELECT t.id, t.name, x.id, x.name, p.id, p.name FROM vertices t, edges tx, vertices x, edges px, vertices p
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Rhizocarpon geographicum' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE'
-- SELECT * FROM edges WHERE child_id = 7119 OR parent_id = 7119;
-- SELECT * FROM vertices WHERE id = 3944;
DELETE FROM edges WHERE child_id = 7119 OR parent_id = 7119;
DELETE FROM vertices WHERE id = 7119;

// Chenopodiaceae: change an image name
SELECT t.id, t.name, x.id, x.name, p.id, p.name FROM vertices t, edges tx, vertices x, edges px, vertices p
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Chenopodiaceae' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE'
-- SELECT * FROM vertices WHERE name = 'ChenChebon.1.jpg';
UPDATE edges SET parent_id = 4362 WHERE child_id = 7888 AND parent_id = 4361;

// Humulus lupulus: change theme from Portrait to Detail
SELECT t.id, t.name, x.id, x.name, p.id, p.name, h.id, h.name FROM vertices t, edges tx, vertices x, edges px, vertices p, edges xh, vertices h
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Humulus lupulus' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE'
AND h.id = xh.child_id AND x.id = xh.parent_id;
UPDATE edges SET child_id = 3942 WHERE parent_id = 4170 AND child_id = 3944;

// Androsace alpina: map to another new image
SELECT t.id, t.name, x.id, x.name, p.id, p.name FROM vertices t, edges tx, vertices x, edges px, vertices p
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Androsace alpina' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE'
INSERT INTO vertices VALUES (NULL, 'Prim075.jpg', 0, 'PICTURE');
INSERT INTO edges (parent_id, child_id) VALUES (3941, 13237);
-- SELECT * FROM vertices WHERE name = 'Prim075.jpg';
UPDATE edges SET parent_id = 13237 WHERE child_id = 9356 AND parent_id = 6209;

// Cornaceae: map to another image
SELECT t.id, t.name, x.id, x.name, p.id, p.name FROM vertices t, edges tx, vertices x, edges px, vertices p
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Cornaceae' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE'
-- SELECT * FROM vertices WHERE name = 'Corn005.jpg';
UPDATE edges SET parent_id = 4695 WHERE child_id = 9435 AND parent_id = 4700;

// Cornus: change text and remap to another image
SELECT t.id, t.name, x.id, x.name, p.id, p.name FROM vertices t, edges tx, vertices x, edges px, vertices p
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Cornus' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE';
-- SELECT * FROM vertices WHERE name = 'Corn005.jpg';
UPDATE edges SET parent_id = 4695 WHERE child_id = 9445 AND parent_id = 4700;

// Ericales: map to another image
SELECT t.id, t.name, x.id, x.name, p.id, p.name FROM vertices t, edges tx, vertices x, edges px, vertices p
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Ericales' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE' ORDER BY p.name;
UPDATE vertices SET name = 'Blüten 4zählig, hier Cornus sanguinea' WHERE id = 9445;
-- SELECT * FROM vertices WHERE name = 'EricLoipro.1.jpg';
UPDATE edges SET parent_id = 5058 WHERE child_id = 9467 AND parent_id = 5059;
UPDATE edges SET parent_id = 5059 WHERE child_id = 9468 AND parent_id = 5058;
UPDATE edges SET parent_id = 5061 WHERE child_id = 9469 AND parent_id = 5063;

// Vinca minor
SELECT t.id, t.name, x.id, x.name, p.id, p.name FROM vertices t, edges tx, vertices x, edges px, vertices p
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Vinca minor' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE' ORDER BY p.name;
DELETE FROM edges WHERE parent_id = 3993 AND child_id = 9769;
DELETE FROM edges WHERE parent_id = 612 AND child_id = 9769;
DELETE FROM edges WHERE parent_id = 9769
DELETE FROM vertices WHERE id = 9769;

// Lamiaceae
SELECT t.id, t.name, x.id, x.name, p.id, p.name FROM vertices t, edges tx, vertices x, edges px, vertices p
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Lamiaceae' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE' ORDER BY p.name;
DELETE FROM edges WHERE parent_id = 5541 AND child_id = 10257;
DELETE FROM edges WHERE parent_id = 781 AND child_id = 10257;
DELETE FROM edges WHERE parent_id = 10257
DELETE FROM vertices WHERE id = 10257;

// Senecio
SELECT t.id, t.name, x.id, x.name, p.id, p.name FROM vertices t, edges tx, vertices x, edges px, vertices p
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Senecio' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE' ORDER BY p.name;
DELETE FROM edges WHERE parent_id = 4675 AND child_id = 10717;
DELETE FROM edges WHERE parent_id = 329 AND child_id = 10717;
DELETE FROM edges WHERE parent_id = 10717;
DELETE FROM vertices WHERE id = 10717;
UPDATE edges SET parent_id = 4675 WHERE child_id = 10718 AND parent_id = 4673;

// Matricaria recutita: doubles not found
SELECT t.id, t.name, x.id, x.name, p.id, p.name, h.id, h.name FROM vertices t, edges tx, vertices x, edges px, vertices p, edges xh, vertices h
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Matricaria recutita' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE'
AND h.id = xh.child_id AND x.id = xh.parent_id;

// Poa
SELECT t.id, t.name, x.id, x.name, p.id, p.name, h.id, h.name FROM vertices t, edges tx, vertices x, edges px, vertices p, edges xh, vertices h
WHERE t.id = tx.parent_id AND x.id = tx.child_id AND t.name = 'Poa' AND x.type = 'PICTURETEXT'
AND p.id = px.parent_id AND x.id = px.child_id AND p.type = 'PICTURE'
AND h.id = xh.child_id AND x.id = xh.parent_id;
INSERT INTO vertices VALUES (NULL, 'GramPoatri.1.jpg', 0, 'PICTURE');
INSERT INTO edges (parent_id, child_id) VALUES (3941, 13239);
-- SELECT * FROM vertices WHERE name = 'GramPoatri.1.jpg';
UPDATE edges SET parent_id = 13239 WHERE child_id = 11626 AND parent_id = 5429;
