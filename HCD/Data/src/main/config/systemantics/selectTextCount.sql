SELECT COUNT(*) FROM
    vertices a,
    edges ab,
    vertices b,
    edges bc,
    vertices c
  WHERE a.id = ab.parent_id
    AND b.id = ab.child_id
    AND b.id = bc.child_id
    AND c.id = bc.parent_id
    AND a.type = 'TAXON'
    AND b.type = 'MORTEXT'
    AND c.type = 'MORVALUE'
