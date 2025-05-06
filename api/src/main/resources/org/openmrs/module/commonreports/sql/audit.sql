-- PATIENT CREATED
SELECT 
    u.username AS username,
    'Patient' AS entity,
    'Created' AS action,
    p.date_created AS action_time,
    pi.identifier AS patient_identifier,
    'Patient record created' AS entity_detail
FROM patient p
JOIN users u ON p.creator = u.user_id
LEFT JOIN patient_identifier pi ON p.patient_id = pi.patient_id AND pi.preferred = 1
WHERE p.date_created >= :startDateTime
  AND p.date_created <= :endDate
  AND u.username = :username

UNION

-- PERSON ATTRIBUTE CREATED
SELECT
    u.username AS username,
    'Person Attribute' AS entity,
    'Created' AS action,
    pa.date_created AS action_time,
    pi.identifier AS patient_identifier,
    pat.name AS entity_detail
FROM person_attribute pa
JOIN users u ON pa.creator = u.user_id
JOIN person p ON pa.person_id = p.person_id
LEFT JOIN patient_identifier pi ON p.person_id = pi.patient_id AND pi.preferred = 1
LEFT JOIN person_attribute_type pat ON pa.person_attribute_type_id = pat.person_attribute_type_id
WHERE pa.date_created >= :startDateTime
  AND pa.date_created <= :endDate
  AND u.username = :username

UNION

-- PERSON ATTRIBUTE VOIDED
SELECT
    u.username AS username,
    'Person Attribute' AS entity,
    'Voided' AS action,
    pa.date_voided AS action_time,
    pi.identifier AS patient_identifier,
    pat.name AS entity_detail
FROM person_attribute pa
JOIN users u ON pa.voided_by = u.user_id
JOIN person p ON pa.person_id = p.person_id
LEFT JOIN patient_identifier pi ON p.person_id = pi.patient_id AND pi.preferred = 1
LEFT JOIN person_attribute_type pat ON pa.person_attribute_type_id = pat.person_attribute_type_id
WHERE pa.voided = 1
  AND pa.date_voided >= :startDateTime
  AND pa.date_voided <= :endDate
  AND u.username = :username

UNION

-- OBSERVATION CREATED
SELECT
    u.username AS username,
    'Observation' AS entity,
    'Created' AS action,
    o.date_created AS action_time,
    pi.identifier AS patient_identifier,
    cn.name AS entity_detail
FROM obs o
JOIN users u ON o.creator = u.user_id
JOIN encounter e ON o.encounter_id = e.encounter_id
JOIN patient_identifier pi ON e.patient_id = pi.patient_id AND pi.preferred = 1
LEFT JOIN concept_name cn ON o.concept_id = cn.concept_id AND cn.locale_preferred = 1 AND cn.voided = 0
WHERE o.date_created >= :startDateTime
  AND o.date_created <= :endDate
  AND u.username = :username

UNION

-- OBSERVATION VOIDED
SELECT
    u.username AS username,
    'Observation' AS entity,
    'Voided' AS action,
    o.date_voided AS action_time,
    pi.identifier AS patient_identifier,
    cn.name AS entity_detail
FROM obs o
JOIN users u ON o.voided_by = u.user_id
JOIN encounter e ON o.encounter_id = e.encounter_id
JOIN patient_identifier pi ON e.patient_id = pi.patient_id AND pi.preferred = 1
LEFT JOIN concept_name cn ON o.concept_id = cn.concept_id AND cn.locale_preferred = 1 AND cn.voided = 0
WHERE o.voided = 1
  AND o.date_voided >= :startDateTime
  AND o.date_voided <= :endDate
  AND u.username = :username

UNION

-- ENCOUNTER CREATED
SELECT
    u.username AS username,
    'Encounter' AS entity,
    'Created' AS action,
    e.date_created AS action_time,
    pi.identifier AS patient_identifier,
    et.name AS entity_detail
FROM encounter e
JOIN users u ON e.creator = u.user_id
JOIN encounter_type et ON e.encounter_type = et.encounter_type_id
JOIN patient_identifier pi ON e.patient_id = pi.patient_id AND pi.preferred = 1
WHERE e.date_created >= :startDateTime
  AND e.date_created <= :endDate
  AND u.username = :username

UNION

-- ENCOUNTER UPDATED
SELECT
    u.username AS username,
    'Encounter' AS entity,
    'Updated' AS action,
    e.date_changed AS action_time,
    pi.identifier AS patient_identifier,
    et.name AS entity_detail
FROM encounter e
JOIN users u ON e.changed_by = u.user_id
JOIN encounter_type et ON e.encounter_type = et.encounter_type_id
JOIN patient_identifier pi ON e.patient_id = pi.patient_id AND pi.preferred = 1
WHERE e.date_changed IS NOT NULL
  AND e.date_changed >= :startDateTime
  AND e.date_changed <= :endDate
  AND u.username = :username

UNION

-- ENCOUNTER VOIDED
SELECT
    u.username AS username,
    'Encounter' AS entity,
    'Voided' AS action,
    e.date_voided AS action_time,
    pi.identifier AS patient_identifier,
    et.name AS entity_detail
FROM encounter e
JOIN users u ON e.voided_by = u.user_id
JOIN encounter_type et ON e.encounter_type = et.encounter_type_id
JOIN patient_identifier pi ON e.patient_id = pi.patient_id AND pi.preferred = 1
WHERE e.voided = 1
  AND e.date_voided >= :startDateTime
  AND e.date_voided <= :endDate
  AND u.username = :username

UNION

-- VISIT CREATED
SELECT
    u.username AS username,
    'Visit' AS entity,
    'Created' AS action,
    v.date_created AS action_time,
    pi.identifier AS patient_identifier,
    vt.name AS entity_detail
FROM visit v
JOIN users u ON v.creator = u.user_id
JOIN visit_type vt ON v.visit_type_id = vt.visit_type_id
JOIN patient_identifier pi ON v.patient_id = pi.patient_id AND pi.preferred = 1
WHERE v.date_created >= :startDateTime
  AND v.date_created <= :endDate
  AND u.username = :username

UNION

-- VISIT VOIDED
SELECT
    u.username AS username,
    'Visit' AS entity,
    'Voided' AS action,
    v.date_voided AS action_time,
    pi.identifier AS patient_identifier,
    vt.name AS entity_detail
FROM visit v
JOIN users u ON v.voided_by = u.user_id
JOIN visit_type vt ON v.visit_type_id = vt.visit_type_id
JOIN patient_identifier pi ON v.patient_id = pi.patient_id AND pi.preferred = 1
WHERE v.voided = 1
  AND v.date_voided >= :startDateTime
  AND v.date_voided <= :endDate
  AND u.username = :username

UNION

-- ORDER CREATED
SELECT
    u.username AS username,
    'Order' AS entity,
    'Created' AS action,
    o.date_created AS action_time,
    pi.identifier AS patient_identifier,
    ot.name AS entity_detail
FROM orders o
JOIN users u ON o.creator = u.user_id
JOIN order_type ot ON o.order_type_id = ot.order_type_id
JOIN patient_identifier pi ON o.patient_id = pi.patient_id AND pi.preferred = 1
WHERE o.date_created >= :startDateTime
  AND o.date_created <= :endDate
  AND u.username = :username

UNION

-- ORDER VOIDED
SELECT
    u.username AS username,
    'Order' AS entity,
    'Voided' AS action,
    o.date_voided AS action_time,
    pi.identifier AS patient_identifier,
    ot.name AS entity_detail
FROM orders o
JOIN users u ON o.voided_by = u.user_id
JOIN order_type ot ON o.order_type_id = ot.order_type_id
JOIN patient_identifier pi ON o.patient_id = pi.patient_id AND pi.preferred = 1
WHERE o.voided = 1
  AND o.date_voided >= :startDateTime
  AND o.date_voided <= :endDate
  AND u.username = :username

ORDER BY action_time DESC;
