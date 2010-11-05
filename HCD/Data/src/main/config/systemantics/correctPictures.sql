SELECT p.type parent, c.type child, COUNT(e.id) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;
SELECT type, COUNT(*) FROM vertices GROUP BY type;

SELECT p.*, c.* FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id AND (c.name = 'vege134.jpg' OR c.name = 'Vege134.jpg' OR p.name = 'vege134.jpg' OR p.name = 'Vege134.jpg');
UPDATE edges SET parent_id = 7001 WHERE parent_id = 7098 AND child_id = 11639;
DELETE FROM edges WHERE parent_id = 3941 AND child_id = 7098;
DELETE FROM vertices WHERE id = 7098;

UPDATE vertices SET name = 'Eric004.jpg' WHERE id = 7090;
