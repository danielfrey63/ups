SELECT *
FROM EDGES taxontext, VERTICES text, VERTICES taxon, VERTICES bild, EDGES bildtext
WHERE taxon.id = taxontext.parent_id
AND text.id = taxontext.child_id
AND taxon.name LIKE 'E%'
AND bild.ID = bildtext.PARENT_ID
AND bildtext.CHILD_ID = text.ID
AND taxon.TYPE = 'TAXON'
AND text.TYPE = 'PICTURETEXT'
AND bild.TYPE = 'PICTURE'
;
