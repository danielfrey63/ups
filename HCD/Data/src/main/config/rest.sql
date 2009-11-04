// Script for new simple hierarchic structure and mapping to taxa. Mapping is done by matching texts.
// ecology structure
DROP TABLE tempvertices IF EXISTS;
CREATE TEMP TEXT TABLE tempvertices (subject VARCHAR NOT NULL, attribute VARCHAR NOT NULL, value VARCHAR NOT NULL, text VARCHAR NOT NULL);
SET TABLE tempvertices SOURCE "rest_structure.csv;fs=\semi;ignore_first=true";

DROP TABLE temproot IF EXISTS;
CREATE TEMP TABLE temproot (id INTEGER IDENTITY, node VARCHAR NOT NULL, parent INTEGER);
INSERT INTO temproot (id, node, parent) VALUES ((SELECT MAX(vertices.id) + 1 FROM vertices), 'Weiteres', (SELECT MAX(vertices.id) FROM vertices WHERE type = 'ROOT'));

DROP TABLE tempsubject IF EXISTS;
CREATE TEMP TABLE tempsubject (id INTEGER IDENTITY, node VARCHAR NOT NULL, parent INTEGER);
INSERT INTO tempsubject (node, parent) SELECT DISTINCT subject, (SELECT MAX(id) + 1 FROM vertices) FROM tempvertices;
UPDATE tempsubject SET id = id + (SELECT MAX(temproot.id) + 1 FROM temproot);

DROP TABLE tempattribute IF EXISTS;
CREATE TEMP TABLE tempattribute (id INTEGER IDENTITY, node VARCHAR NOT NULL, parent INTEGER);
INSERT INTO tempattribute (node, parent) SELECT DISTINCT v.attribute, s.id FROM tempsubject s, tempvertices v WHERE s.node = v.subject;
UPDATE tempattribute SET id = id + (SELECT MAX(tempsubject.id) + 1 FROM tempsubject);

DROP TABLE tempvalue IF EXISTS;
CREATE TEMP TABLE tempvalue (id INTEGER IDENTITY, node VARCHAR NOT NULL, parent INTEGER);
INSERT INTO tempvalue (node, parent) SELECT DISTINCT v.value, a.id
FROM tempsubject s, tempattribute a, tempvertices v
WHERE s.node = v.subject AND a.node = v.attribute AND a.parent = s.id;
UPDATE tempvalue SET id = id + (SELECT MAX(tempattribute.id) + 1 FROM tempattribute);

DROP TABLE temptext IF EXISTS;
CREATE TEMP TABLE temptext (id INTEGER IDENTITY, node VARCHAR NOT NULL, parent INTEGER);
INSERT INTO temptext (node, parent) SELECT v.text, w.id
FROM tempsubject s, tempattribute a, tempvalue w, tempvertices v
WHERE s.node = v.subject AND a.node = v.attribute AND w.node = v.value
AND s.id = a.parent AND a.id = w.parent;
UPDATE temptext SET id = id + (SELECT MAX(tempvalue.id) + 1 FROM tempvalue);

DROP TABLE tempvertices;

INSERT INTO vertices (id, name, rank, type) SELECT id, node, 0, 'MEDICINE' FROM temproot;
INSERT INTO vertices (id, name, rank, type) SELECT id, node, 0, 'MEDSUBJECT' FROM tempsubject;
INSERT INTO vertices (id, name, rank, type) SELECT id, node, 0, 'MEDATTRIBUTE' FROM tempattribute;
INSERT INTO vertices (id, name, rank, type) SELECT id, node, 0, 'MEDVALUE' FROM tempvalue;
INSERT INTO vertices (id, name, rank, type) SELECT id, node, 0, 'MEDTEXT' FROM temptext;

INSERT INTO edges (child_id, parent_id) SELECT id, parent FROM temproot;
INSERT INTO edges (child_id, parent_id) SELECT id, parent FROM tempsubject;
INSERT INTO edges (child_id, parent_id) SELECT id, parent FROM tempattribute;
INSERT INTO edges (child_id, parent_id) SELECT id, parent FROM tempvalue;
INSERT INTO edges (child_id, parent_id) SELECT id, parent FROM temptext;

DROP TABLE temproot;
DROP TABLE tempsubject;
DROP TABLE tempattribute;
DROP TABLE tempvalue;
DROP TABLE temptext;

// ecology mapping
DROP TABLE match IF EXISTS;
CREATE TEMP TEXT TABLE match (tax VARCHAR NOT NULL, text VARCHAR NOT NULL);
SET TABLE match SOURCE "rest_mapping.csv;fs=\semi;ignore_first=true";

// Check
-- SELECT * FROM match m LEFT OUTER JOIN vertices v ON v.name = m.tax WHERE v.type = 'TAXON';
// Must be equal size
-- SELECT * FROM vertices t, vertices x, match m WHERE t.name = m.tax AND x.name = m.text AND t.type = 'TAXON' AND x.type = 'MEDTEXT';
-- SELECT * FROM match;

INSERT INTO edges (parent_id, child_id) SELECT t.id, x.id FROM vertices t, vertices x, match m WHERE t.name = m.tax AND x.name = m.text AND t.type = 'TAXON' AND x.type = 'MEDTEXT';

DROP TABLE match;

-- SELECT p.type parent, c.type child, COUNT(e.id) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;
