SELECT c.name, p.name, l.name FROM
    vertices p,
    edges pc,
    vertices c,
    edges cl,
    vertices l
  WHERE p.id = pc.parent_id
    AND c.id = pc.child_id
    AND c.id = cl.parent_id
    AND l.id = cl.child_id
    AND p.type = 'TAXON'
    AND c.type = 'TAXON'
    AND l.type = 'TAXONLEVEL';
