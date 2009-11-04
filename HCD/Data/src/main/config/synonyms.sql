SELECT p.type parent, c.type child, COUNT(*) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;

DROP TABLE synonyms IF EXISTS;
CREATE TEXT TABLE synonyms (tax VARCHAR, syn VARCHAR);
SET TABLE synonyms SOURCE "import/synonyme_german.csv;fs=\semi;ignore_first=true";

// Check for unknown taxa, should be zero
-- SELECT * FROM synonyms WHERE tax NOT IN (SELECT name FROM vertices WHERE type = 'TAXON');

DROP TABLE new IF EXISTS;
CREATE TEMP TABLE new (id IDENTITY, tax VARCHAR, syn VARCHAR);
INSERT INTO new (id, tax, syn) SELECT NULL, s.tax, s.syn FROM synonyms s;
UPDATE new SET id = id + (SELECT MAX(v.id) + 1 FROM vertices v);

DROP TABLE synonyms;

INSERT INTO vertices (id, name, rank, type) SELECT n.id, n.syn, 0, 'TAXONSYNONYM' FROM new n;
INSERT INTO edges (parent_id, child_id) SELECT v.id, n.id FROM vertices v, new n WHERE v.name = n.tax AND v.type = 'TAXON';

SELECT p.type parent, c.type child, COUNT(*) c FROM vertices p, edges e, vertices c WHERE p.id = e.parent_id AND c.id = e.child_id GROUP BY p.type, c.type;

SHUTDOWN COMPACT;
