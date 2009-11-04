SELECT p.type parent, c.type child, COUNT(e.id) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;

DROP TABLE german IF EXISTS;
CREATE TEXT TABLE german (id INT, name VARCHAR);
SET TABLE german SOURCE "import/taxa_german.tsv;ignore_first=true;fs=\t";

DROP TABLE scientific IF EXISTS;
CREATE TEXT TABLE scientific (id INT, name VARCHAR);
SET TABLE scientific SOURCE "import/taxa_scientific.tsv;ignore_first=true;fs=\t";

// Translate taxon names
DROP TABLE match IF EXISTS;
CREATE TEMP TABLE match (german VARCHAR, scientific VARCHAR);
INSERT INTO match (scientific, german) SELECT s.name, g.name FROM german g, scientific s WHERE g.id = s.id;

// should have 0 rows
SELECT v.name FROM vertices v WHERE v.name NOT IN (SELECT m.scientific FROM match m) AND v.type = 'TAXON';
// following two should have same size
-- SELECT * FROM vertices WHERE type = 'TAXON';
-- SELECT v.name, m.german FROM vertices v, match m WHERE v.name = m.scientific AND v.type = 'TAXON';
DELETE FROM match WHERE scientific NOT IN (SELECT v.name FROM vertices v WHERE type = 'TAXON');
DELETE FROM match WHERE scientific = german;

UPDATE vertices
SET name = (SELECT m.german FROM match m WHERE  m.scientific = name AND type = 'TAXON')
WHERE type = 'TAXON' AND name IN (SELECT scientific FROM match);


// Mount new text mapping
DROP TABLE newtext IF EXISTS;
CREATE TEXT TABLE newtext (rank INT, taxon VARCHAR, text VARCHAR, pic VARCHAR, theme VARCHAR);
SET TABLE newtext SOURCE "import/picturemapping_german.csv;ignore_first=false;fs=\semi";

// Check taxon names, should be zero size
-- SELECT * FROM newtext n WHERE n.taxon NOT IN (SELECT t.name FROM vertices t WHERE type = 'TAXON');
// Replace remaining scientific names in mapping by german names until zero size
-- SELECT taxon, scientific, german FROM newtext, match WHERE scientific = taxon;
-- UPDATE newtext SET taxon = (SELECT DISTINCT german FROM match WHERE scientific = taxon AND scientific <> german) WHERE taxon IN (SELECT DISTINCT scientific FROM match WHERE scientific = taxon AND scientific <> german);
// Remove typos
SELECT * FROM vertices WHERE type = 'TAXON' AND name LIKE 'Ginkg%';
UPDATE newtext SET taxon = 'Ginkgo-Gewächse' WHERE taxon = 'Ginkgogewächse';
UPDATE newtext SET taxon = 'Primelgewächse' WHERE taxon = 'Primelgwächse';
UPDATE newtext SET taxon = 'Bärlappähnliche' WHERE taxon = 'Bärlappartige';
UPDATE vertices SET name = 'Rundblättriger Steinbrech' WHERE name = 'Rundblätriger Steinbrech';

// Check picture names, should be zero
SELECT pic FROM newtext WHERE pic NOT IN (SELECT name FROM vertices WHERE type = 'PICTURE');

// Translate picture texts
DROP TABLE temptext IF EXISTS;
SELECT v.id, n.text INTO temptext FROM vertices v, edges tv, edges pv, vertices t, vertices p, newtext n
WHERE v.id = tv.child_id AND v.id = pv.child_id AND tv.parent_id = t.id AND pv.parent_id = p.id AND t.type = 'TAXON' AND p.type = 'PICTURE' AND n.taxon = t.name AND n.pic = p.name;
UPDATE temptext SET text = "" WHERE text IS NULL;
// Should be zero
SELECT * FROM (SELECT id, COUNT(*) c FROM temptext GROUP BY id) WHERE c > 1;
// Patch
UPDATE vertices SET name = (SELECT n.text FROM temptext n WHERE n.id = vertices.id) WHERE id IN (SELECT n.id FROM temptext n);

SELECT p.type parent, c.type child, COUNT(e.id) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;

DROP TABLE temptext;
DROP TABLE newtext;
DROP TABLE german;
DROP TABLE scientific;
DROP TABLE match;

