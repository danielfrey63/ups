SELECT * 
FROM
  vertices t,
  vertices x,
  vertices p,
  edges tx,
  edges px
WHERE t.id = tx.parent_id
  AND x.id = tx.child_id
  AND x.id = px.child_id
  AND p.id = px.parent_id
  AND t.type = 'TAXON'
  AND x.type = 'PICTURETEXT'
  AND p.type = 'PICTURE'
;
