UPDATE vertices SET type = 'ADDITIONAL' WHERE type = 'MEDICINE';
UPDATE vertices SET type = 'ADDSUBJECT' WHERE type = 'MEDSUBJECT';
UPDATE vertices SET type = 'ADDATTRIBUTE' WHERE type = 'MEDATTRIBUTE';
UPDATE vertices SET type = 'ADDVALUE' WHERE type = 'MEDVALUE';
UPDATE vertices SET type = 'ADDTEXT' WHERE type = 'MEDTEXT';

INSERT INTO vertices VALUES (NULL, 'Medizin', 0, 'MEDICINE');
INSERT INTO edges (parent_id, child_id) VALUES (SELECT MAX(v.id) FROM vertices v WHERE v.type = 'ROOT', SELECT MAX(w.id) FROM vertices w WHERE w.type = 'MEDICINE')

DROP TABLE t1 IF EXISTS;
CREATE TEMP TABLE t1 (id INT);
INSERT INTO t1 SELECT v.id FROM vertices v  WHERE v.name = 'Medizin' AND v.type = 'ADDSUBJECT';
UPDATE vertices SET type = 'MEDSUBJECT' WHERE id IN (SELECT t.id FROM t1 t);
UPDATE edges SET parent_id = (SELECT MAX(v.id) FROM vertices v WHERE v.type = 'MEDICINE') WHERE child_id IN (SELECT t.id FROM t1 t);

DROP TABLE t2 IF EXISTS;
CREATE TEMP TABLE t2 (id INT);
INSERT INTO t2 SELECT child_id FROM edges WHERE parent_id IN (SELECT v.id FROM t1 v)
UPDATE vertices SET type = 'MEDATTRIBUTE' WHERE id IN (SELECT t.id FROM t2 t);

DROP TABLE t3 IF EXISTS;
CREATE TEMP TABLE t3 (id INT);
INSERT INTO t3 SELECT child_id FROM edges WHERE parent_id IN (SELECT v.id FROM t2 v)
UPDATE vertices SET type = 'MEDVALUE' WHERE id IN (SELECT t.id FROM t3 t);

DROP TABLE t4 IF EXISTS;
CREATE TEMP TABLE t4 (id INT);
INSERT INTO t4 SELECT child_id FROM edges WHERE parent_id IN (SELECT v.id FROM t3 v)
UPDATE vertices SET type = 'MEDTEXT' WHERE id IN (SELECT t.id FROM t4 t);

SELECT p.type parent, c.type child, COUNT(e.id) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;
