SELECT COUNT(*) FROM edges; // 15733
SELECT COUNT(*) FROM vertices; // 9158
SELECT p.*, c.* FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id; // 15733

SELECT DISTINCT parent_id FROM edges; // 6823
SELECT DISTINCT child_id FROM edges; // 8954

// Delete all unused vertices. The following two should be of same size.
-- SELECT parent_id id FROM edges UNION SELECT child_id id FROM edges; // 9062
-- SELECT DISTINCT id FROM vertices; // 9157
SELECT * FROM vertices EXCEPT { SELECT parent_id id FROM edges UNION SELECT child_id id FROM edges }; // 95 orphans
DROP TABLE temp IF EXISTS;
CREATE TEMP TABLE temp (id INTEGER);
INSERT INTO temp (id) SELECT id FROM vertices EXCEPT { SELECT parent_id id FROM edges UNION SELECT child_id id FROM edges };
DELETE FROM vertices WHERE id IN (SELECT temp.id FROM temp);
DROP TABLE temp;
SELECT COUNT(*) FROM vertices; // 9063

// Check for multiple entries for one taxon
SELECT * FROM (SELECT name, COUNT(name) c FROM vertices WHERE type = 'TAXON' GROUP BY name) WHERE c > 1; // 174
-- Paste script of CheckDB here

// Delete type to replace
DROP TABLE temp IF EXISTS;
CREATE TABLE temp (id INTEGER);
INSERT INTO temp (id) SELECT DISTINCT id FROM vertices
WHERE type = 'MEDICINE'
OR type = 'MEDSUBJECT'
OR type = 'MEDATTRIBUTE'
OR type = 'MEDVALUE'
OR type = 'PICTURES'
OR type = 'PICTURE'
OR type = 'PICTURETEXT'
OR type = 'PICTURETHEME';
DELETE FROM edges WHERE parent_id IN (SELECT temp.id FROM temp);
DELETE FROM edges WHERE child_id IN (SELECT temp.id FROM temp);
DELETE FROM vertices WHERE id IN (SELECT temp.id FROM temp);
-- SELECT COUNT(*) FROM edges;
-- SELECT COUNT(*) FROM vertices;
DROP TABLE temp;

// Delete self linked mortext
SELECT COUNT(*) FROM edges;
-- SELECT DISTINCT p.id, c.id FROM vertices p, edges e, vertices c
-- 	WHERE p.id = e.parent_id AND c.id = e.child_id
-- 	AND p.type = 'MORTEXT' AND c.type = 'MORTEXT';
DELETE FROM edges WHERE id IN (
SELECT DISTINCT e.id FROM vertices p, edges e, vertices c
WHERE p.id = e.parent_id AND c.id = e.child_id
AND p.type = 'MORTEXT' AND c.type = 'MORTEXT');
SELECT COUNT(*) FROM edges;

// -------------------------------------------------------------------------------------------------------------------
// Clean up ids
SELECT DISTINCT type FROM vertices;
SELECT * FROM vertices WHERE type = 'TAXONLEVEL';
SELECT p.type parent, c.type child, COUNT(e.id) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;

UPDATE vertices SET type = 'MORSUBJECT' WHERE type = 'SUBJECT';
UPDATE vertices SET type = 'MORATTRIBUTE' WHERE type = 'ATTRIBUTE';
UPDATE vertices SET type = 'MORVALUE' WHERE type = 'VALUE';

-- SELECT p.type parent, c.type child, COUNT(e.id) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;
DROP TABLE tempvertices IF EXISTS;
CREATE TABLE tempvertices (id IDENTITY, oldid INTEGER, name VARCHAR, rank INTEGER, type VARCHAR);
INSERT INTO tempvertices VALUES (0, 0, 'Dummy', 0, 'DUMMY');
INSERT INTO tempvertices SELECT NULL, * FROM vertices WHERE type = 'ROOT';
INSERT INTO tempvertices SELECT NULL, * FROM vertices WHERE type = 'TAXON';
INSERT INTO tempvertices SELECT NULL, * FROM vertices WHERE type = 'TAXONLEVEL' AND rank > 0 ORDER BY rank;
INSERT INTO tempvertices SELECT NULL, * FROM vertices WHERE type = 'MORPHOLOGY';
INSERT INTO tempvertices SELECT NULL, * FROM vertices WHERE type = 'MORSUBJECT';
INSERT INTO tempvertices SELECT NULL, * FROM vertices WHERE type = 'MORATTRIBUTE';
INSERT INTO tempvertices SELECT NULL, * FROM vertices WHERE type = 'MORVALUE';
INSERT INTO tempvertices SELECT NULL, * FROM vertices WHERE type = 'MORTEXT';
DELETE FROM tempvertices WHERE type = 'DUMMY';
-- SELECT p.type parent, c.type child, COUNT(e.id) c FROM tempvertices p, edges e, tempvertices c WHERE p.oldid = e.parent_id AND c.oldid = e.child_id GROUP BY p.type, c.type;

DROP TABLE tempedges IF EXISTS;
CREATE TABLE tempedges (id IDENTITY, parent INTEGER, child INTEGER, oldparent INTEGER, oldchild INTEGER);
INSERT INTO tempedges SELECT NULL, p.id, c.id, p.oldid, c.oldid FROM edges e, tempvertices p, tempvertices c WHERE e.parent_id = p.oldid AND e.child_id = c.oldid;
-- SELECT p.type parent, c.type child, COUNT(e.id) c FROM tempvertices p, tempedges e, tempvertices c WHERE p.id = e.parent AND c.id = e.child GROUP BY p.type, c.type;

DELETE FROM edges;
DELETE FROM vertices;
INSERT INTO vertices (id, name, rank, type) SELECT id, name, rank, type FROM tempvertices;
INSERT INTO edges (parent_id, child_id) SELECT parent, child FROM tempedges;
-- SELECT p.type parent, c.type child, COUNT(e.id) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;

DROP TABLE tempedges;
DROP TABLE tempvertices;

// -------------------------------------------------------------------------------------------------------------------
// Rank taxa
DROP TABLE pictures IF EXISTS;
CREATE TEXT TABLE pictures (rank INTEGER, tax VARCHAR, text VARCHAR, pic VARCHAR, theme VARCHAR);
SET TABLE pictures SOURCE "picturemapping.csv;fs=\semi";

// Running checks on picture mapping. All checks should return 0 recoreds, except where indicated.
// Check which taxa are in picture mapping but not in vertices and vice verca
SELECT tax FROM pictures EXCEPT SELECT name FROM vertices WHERE type = 'TAXON';
SELECT name FROM vertices WHERE type = 'TAXON' EXCEPT SELECT tax FROM pictures; // root may be without pictures
// Check whether ranks are unique for taxa in picture mapping
SELECT rank, c FROM (SELECT rank, COUNT(rank) c FROM (SELECT DISTINCT rank, tax FROM pictures) GROUP BY rank) WHERE c > 1;
// Check whether pictures just occur once for each taxon
SELECT * FROM (SELECT c, COUNT(c) cnt FROM (SELECT DISTINCT CONCAT(CAST(rank AS VARCHAR), pic) c FROM pictures) GROUP BY c) WHERE cnt > 1;
// Check whether multible taxon entries are in pictures
SELECT * FROM (SELECT tax, COUNT(tax) c FROM (SELECT DISTINCT rank, tax FROM pictures) GROUP BY tax) WHERE c > 1;
// Check that themes are not empty
SELECT * FROM pictures WHERE theme IS NULL;

// Table holding new ranks for taxa
DROP TABLE temp IF EXISTS;
CREATE TEMP TABLE temp (rank INTEGER, tax VARCHAR);
INSERT INTO temp (rank, tax) SELECT DISTINCT rank, tax FROM pictures;
SELECT COUNT(tax) FROM temp;
// Checks
SELECT * FROM (SELECT tax, COUNT(tax) c FROM  temp GROUP BY tax) WHERE c > 1;
SELECT * FROM (SELECT rank, COUNT(rank) c FROM  temp GROUP BY rank) WHERE c > 1;
SELECT * FROM (SELECT name, COUNT(name) c FROM  vertices WHERE type = 'TAXON' GROUP BY name) WHERE c > 1;
// Update
UPDATE vertices SET rank = (SELECT temp.rank FROM temp WHERE vertices.name = temp.tax) WHERE type = 'TAXON' AND name IN (SELECT tax FROM temp);
-- SELECT * FROM vertices WHERE type = 'TAXON';
DROP TABLE temp;

DROP TABLE pictures;
