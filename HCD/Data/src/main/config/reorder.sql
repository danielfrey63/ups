DROP TABLE temp IF EXISTS;
CREATE TABLE temp (id INT, name VARCHAR, rank IDENTITY, type VARCHAR);
INSERT INTO temp SELECT id, name, NULL, type FROM vertices WHERE type = 'MORSUBJECT' ORDER BY name;
INSERT INTO temp SELECT id, name, NULL, type FROM vertices WHERE type = 'MORATTRIBUTE' ORDER BY name;
INSERT INTO temp SELECT id, name, NULL, type FROM vertices WHERE type = 'MORVALUE' ORDER BY name;
INSERT INTO temp SELECT id, name, NULL, type FROM vertices WHERE type = 'MORTEXT' ORDER BY name;
UPDATE vertices SET rank = (SELECT t.rank FROM temp t WHERE id = t.id) WHERE id IN (SELECT m.id FROM temp m);
DROP TABLE temp;
SHUTDOWN COMPACT;
