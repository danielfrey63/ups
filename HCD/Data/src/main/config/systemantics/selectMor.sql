SELECT x.name AS "TAXON"
  , t.name AS "TEXT"
  , v.name AS "VALUE"
  , a.name AS "ATTRIBUTE"
  , v1.name AS "S1"
  , v2.name AS "S2"
  , v3.name AS "S3"
  , v4.name AS "S4"
  , v5.name AS "S5"
  , v6.name AS "S6"
  , v7.name AS "S7"
  , v8.name AS "S8"
FROM  vertices x
  , edges ex
  , vertices t
  , edges et
  , vertices v
  , edges ev
  , vertices a
  , edges ea
  , vertices v1
  , edges e12
  , vertices v2
  , edges e23
  , vertices v3
  , edges e34
  , vertices v4
  , edges e45
  , vertices v5
  , edges e56
  , vertices v6
  , edges e67
  , vertices v7
  , edges e78
  , vertices v8
WHERE x.id = ex.parent_id
  AND t.id = ex.child_id
  AND t.id = et.child_id
  AND v.id = et.parent_id
  AND v.id = ev.child_id
  AND a.id = ev.parent_id
  AND a.id = ea.child_id
  AND v1.id = ea.parent_id
  AND v1.id = e12.child_id
  AND v2.id = e12.parent_id
  AND v2.id = e23.child_id
  AND v3.id = e23.parent_id
  AND v3.id = e34.child_id
  AND v4.id = e34.parent_id
  AND v4.id = e45.child_id
  AND v5.id = e45.parent_id
  AND v5.id = e56.child_id
  AND v6.id = e56.parent_id
  AND v6.id = e67.child_id
  AND v7.id = e67.parent_id
  AND v7.id = e78.child_id
  AND v8.id = e78.parent_id
  AND x.type = 'TAXON'
  AND t.type = 'MORTEXT'
  AND v.type = 'MORVALUE'
  AND a.type = 'MORATTRIBUTE'
  AND v1.type = 'MORSUBJECT'
  AND v2.type = 'MORSUBJECT'
  AND v3.type = 'MORSUBJECT'
  AND v4.type = 'MORSUBJECT'
  AND v5.type = 'MORSUBJECT'
  AND v6.type = 'MORSUBJECT'
  AND v7.type = 'MORSUBJECT'
  AND v8.type = 'MORSUBJECT'
;
