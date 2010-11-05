SET MAXROWS 0;

// -------------------------------------------------------------------------------------------------------------------
// Run this part step by step manually to investigate the db
-- SELECT COUNT(*) FROM edges; // 15733
-- SELECT COUNT(*) FROM vertices; // 9158
-- SELECT p.*, c.* FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id; // 15733
-- SELECT DISTINCT parent_id FROM edges; // 6823
-- SELECT DISTINCT child_id FROM edges; // 8954

// -------------------------------------------------------------------------------------------------------------------
// Run it
// Delete all unused vertices
-- SELECT parent_id id FROM edges UNION SELECT child_id id FROM edges; // 9062
-- SELECT DISTINCT id FROM vertices; // 9157
SELECT id FROM vertices EXCEPT { SELECT parent_id id FROM edges UNION SELECT child_id id FROM edges }; // 95 orphans
DROP TABLE temp IF EXISTS;
CREATE TEMP TABLE temp (id INTEGER);
INSERT INTO temp (id) SELECT id FROM vertices EXCEPT { SELECT parent_id id FROM edges UNION SELECT child_id id FROM edges };
DELETE FROM vertices WHERE id IN (SELECT id FROM temp);
DROP TABLE temp;
-- SELECT COUNT(*) FROM vertices; // 9063

// -------------------------------------------------------------------------------------------------------------------
// Run this part step by step manually to investigate the db
// Check for multiple entries for one taxon. Should be 0.
SELECT * FROM (SELECT name, COUNT(name) c FROM vertices WHERE type = 'TAXON' GROUP BY name) WHERE c > 1; // 174
-- Paste script to remove unwanted taxa here (CheckDB).

// -------------------------------------------------------------------------------------------------------------------
// Run it
// Link pictures
DROP TABLE pictures IF EXISTS;
CREATE TEXT TABLE pictures (rank INTEGER, tax VARCHAR, text VARCHAR, pic VARCHAR, theme VARCHAR);
SET TABLE pictures SOURCE "picturemapping.csv;fs=\semi";

// Translate TIFs to JPGs
UPDATE vertices SET name = (SELECT REPLACE(name, '.tif', '.jpg') FROM vertices v WHERE type = 'PICTURE' AND vertices.name = v.name) WHERE type = 'PICTURE';

// -------------------------------------------------------------------------------------------------------------------
// Run this part step by step manually to investigate the db
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

// -------------------------------------------------------------------------------------------------------------------
// Run it
// Table holding new ranks
DROP TABLE temp IF EXISTS;
CREATE TEMP TABLE temp (rank INTEGER, tax VARCHAR);
INSERT INTO temp (rank, tax) SELECT DISTINCT rank, tax FROM pictures;
SELECT COUNT(tax) FROM temp;
// -------------------------------------------------------------------------------------------------------------------
// Run this part step by step manually to investigate the db
// Checks
SELECT * FROM (SELECT tax, COUNT(tax) c FROM  temp GROUP BY tax) WHERE c > 1;
SELECT * FROM (SELECT rank, COUNT(rank) c FROM  temp GROUP BY rank) WHERE c > 1;
SELECT * FROM (SELECT name, COUNT(name) c FROM  vertices WHERE type = 'TAXON' GROUP BY name) WHERE c > 1;
// Update
// -------------------------------------------------------------------------------------------------------------------
SET MAXROWS 0;

// -------------------------------------------------------------------------------------------------------------------
// Run this part step by step manually to investigate the db
-- SELECT COUNT(*) FROM edges; // 15733
-- SELECT COUNT(*) FROM vertices; // 9158
-- SELECT p.*, c.* FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id; // 15733
-- SELECT DISTINCT parent_id FROM edges; // 6823
-- SELECT DISTINCT child_id FROM edges; // 8954

// -------------------------------------------------------------------------------------------------------------------
// Run it
// Delete all unused vertices
-- SELECT parent_id id FROM edges UNION SELECT child_id id FROM edges; // 9062
-- SELECT DISTINCT id FROM vertices; // 9157
SELECT id FROM vertices EXCEPT { SELECT parent_id id FROM edges UNION SELECT child_id id FROM edges }; // 95 orphans
DROP TABLE temp IF EXISTS;
CREATE TEMP TABLE temp (id INTEGER);
INSERT INTO temp (id) SELECT id FROM vertices EXCEPT { SELECT parent_id id FROM edges UNION SELECT child_id id FROM edges };
DELETE FROM vertices WHERE id IN (SELECT id FROM temp);
DROP TABLE temp;
-- SELECT COUNT(*) FROM vertices; // 9063

// -------------------------------------------------------------------------------------------------------------------
// Run this part step by step manually to investigate the db
// Check for multiple entries for one taxon. Should be 0.
SELECT * FROM (SELECT name, COUNT(name) c FROM vertices WHERE type = 'TAXON' GROUP BY name) WHERE c > 1; // 174
-- Paste script to remove unwanted taxa here (CheckDB).

// -------------------------------------------------------------------------------------------------------------------
// Run it
// Link pictures
DROP TABLE pictures IF EXISTS;
CREATE TEXT TABLE pictures (rank INTEGER, tax VARCHAR, text VARCHAR, pic VARCHAR, theme VARCHAR);
SET TABLE pictures SOURCE "picturemapping.csv;fs=\semi";

// -------------------------------------------------------------------------------------------------------------------
// Run this part step by step manually to investigate the db
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

// -------------------------------------------------------------------------------------------------------------------
// Run it
// Table holding new ranks
DROP TABLE temp IF EXISTS;
CREATE TEMP TABLE temp (rank INTEGER, tax VARCHAR);
INSERT INTO temp (rank, tax) SELECT DISTINCT rank, tax FROM pictures;
SELECT COUNT(tax) FROM temp;
// -------------------------------------------------------------------------------------------------------------------
// Run this part step by step manually to investigate the db
// Checks
SELECT * FROM (SELECT tax, COUNT(tax) c FROM  temp GROUP BY tax) WHERE c > 1;
SELECT * FROM (SELECT rank, COUNT(rank) c FROM  temp GROUP BY rank) WHERE c > 1;
SELECT * FROM (SELECT name, COUNT(name) c FROM  vertices WHERE type = 'TAXON' GROUP BY name) WHERE c > 1;
// Update
// -------------------------------------------------------------------------------------------------------------------
// Run it
UPDATE vertices SET rank = (SELECT temp.rank FROM temp WHERE vertices.name = temp.tax) WHERE type = 'TAXON' AND name IN (SELECT tax FROM temp);
-- SELECT * FROM vertices WHERE type = 'TAXON';

DROP TABLE temp1 IF EXISTS;
CREATE TEMP TABLE temp1 (rank INTEGER, tax VARCHAR);
INSERT INTO temp1 (rank, tax) SELECT DISTINCT rank, tax FROM pictures;
SELECT * FROM temp1;

DROP TABLE temp2 IF EXISTS;
CREATE TEMP TABLE temp2 (id IDENTITY, name VARCHAR, rank INTEGER, type VARCHAR);
INSERT INTO temp2 (id, name, rank, type) SELECT id, name, rank, type FROM vertices;

UPDATE temp2 SET rank = (SELECT temp1.rank FROM temp1 WHERE temp2.name = temp1.tax) WHERE type = 'TAXON' AND name IN (SELECT tax FROM temp1);
-- UPDATE temp2 SET rank = (SELECT temp1.rank FROM temp1 WHERE temp2.name = temp1.tax) WHERE type = 'TYP1';
-- SELECT * FROM temp2 WHERE type = 'TAXON';
-- SELECT * FROM temp1;
-- SELECT COUNT(*) FROM temp2 WHERE rank > 0;

DROP TABLE temp1;
DROP TABLE temp2;

// Connect to text tables
DROP TABLE root_temp IF EXISTS;
DROP TABLE picture_temp IF EXISTS;
DROP TABLE theme_temp IF EXISTS;
DROP TABLE mapping_temp IF EXISTS;

CREATE TEMP TABLE root_temp (id IDENTITY, node VARCHAR, parent INTEGER);
CREATE TEMP TABLE picture_temp (id IDENTITY, node VARCHAR, parent INTEGER);
CREATE TEMP TABLE theme_temp (id IDENTITY, node VARCHAR, parent INTEGER);
CREATE TEMP TABLE mapping_temp (id IDENTITY, tax_id INTEGER, pic_id INTEGER, theme_id INTEGER, text VARCHAR);

INSERT INTO root_temp (node, parent) VALUES ('Bilder', (SELECT MAX(vertices.id) FROM vertices WHERE type = 'ROOT'));
INSERT INTO picture_temp (node) SELECT DISTINCT pic FROM pictures;
INSERT INTO theme_temp (node) SELECT DISTINCT theme FROM pictures;
INSERT INTO mapping_temp (tax_id, pic_id, theme_id, text)
SELECT t.id, p.id, h.id, a.text
FROM pictures a, vertices t, picture_temp p, theme_temp h
WHERE a.tax = t.name AND t.type = 'TAXON' AND a.pic = p.node AND a.theme = h.node;

DROP TABLE pictures;

// Insert pictures. Autogenerate a PK
UPDATE root_temp SET id = id + (SELECT MAX(vertices.id) + 1 FROM vertices);
UPDATE theme_temp SET id = id + (SELECT MAX(root_temp.id) + 1 FROM root_temp);
UPDATE mapping_temp SET theme_id = theme_id + (SELECT MAX(root_temp.id) + 1 FROM root_temp);
UPDATE picture_temp SET id = id + (SELECT MAX(theme_temp.id) + 1 FROM theme_temp);
UPDATE mapping_temp SET pic_id = pic_id + (SELECT MAX(theme_temp.id) + 1 FROM theme_temp);
UPDATE mapping_temp SET id = id + (SELECT MAX(picture_temp.id) + 1 FROM picture_temp);
UPDATE mapping_temp SET text = '' WHERE text IS NULL;

INSERT INTO vertices (id, name, rank, type) SELECT id, node, 0, 'PICTURES' FROM root_temp;
INSERT INTO vertices (id, name, rank, type) SELECT id, node, 0, 'PICTURETHEME' FROM theme_temp;
INSERT INTO vertices (id, name, rank, type) SELECT id, node, 0, 'PICTURE' FROM picture_temp;
INSERT INTO vertices (id, name, rank, type) SELECT id, text, 0, 'PICTURETEXT' FROM mapping_temp;

INSERT INTO edges (parent_id, child_id) SELECT tax_id, id FROM mapping_temp;
INSERT INTO edges (parent_id, child_id) SELECT pic_id, id FROM mapping_temp;
INSERT INTO edges (parent_id, child_id) SELECT id, theme_id FROM mapping_temp;
INSERT INTO edges (parent_id, child_id) SELECT parent, id FROM root_temp;
INSERT INTO edges (parent_id, child_id) SELECT (SELECT vertices.id FROM vertices WHERE type = 'PICTURES'), id FROM picture_temp;

UPDATE vertices SET rank = 1 WHERE name = 'Herbar' AND type = 'PICTURETHEME';
UPDATE vertices SET rank = 2 WHERE name = 'Portrait' AND type = 'PICTURETHEME';
UPDATE vertices SET rank = 3 WHERE name = 'Detail' AND type = 'PICTURETHEME';
UPDATE vertices SET rank = 4 WHERE name = 'Standort' AND type = 'PICTURETHEME';

INSERT INTO edges (parent_id, child_id) SELECT (SELECT MAX(v.id) FROM vertices v WHERE v.type = 'ROOT'), id FROM vertices WHERE type = 'PICTURETHEME';

SELECT p.type parent, c.type child, COUNT(e.id) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;

--SELECT * FROM edges; // 17595

DROP TABLE new IF EXISTS;
DROP TABLE picture_to_insert IF EXISTS;
DROP TABLE theme_to_insert IF EXISTS;
DROP TABLE text_to_insert IF EXISTS;
DROP TABLE mapping_temp IF EXISTS;
DROP TABLE taxon_temp IF EXISTS;
DROP TABLE picture_temp IF EXISTS;
DROP TABLE theme_temp IF EXISTS;
DROP TABLE vertices_taxon IF EXISTS;

--SELECT p.type "P", c.type "C", COUNT(*) "N" FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;

// Translate TIFs to JPGs
UPDATE vertices SET name = (SELECT REPLACE(name, '.tif', '.jpg') FROM vertices v WHERE type = 'PICTURE' AND vertices.name = v.name) WHERE type = 'PICTURE';

SHUTDOWN COMPACT;
