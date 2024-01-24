-- Patients aged 40 and above with NCD form filled out for the first time
SELECT 
    'Registered patients aged 40 and above that have had their NCD screenign for the first time' AS 'Indicator', 
    CAST(COUNT(DISTINCT p.person_id) AS CHAR) AS 'Value'
FROM 
    encounter e 
INNER JOIN 
    person p ON e.patient_id = p.person_id
WHERE 
    e.location_id in (:locationList)
    AND e.encounter_datetime > :startDate
    AND e.encounter_datetime < :endDate
    AND e.encounter_type = (
        SELECT encounter_type_id 
        FROM encounter_type s_et 
        WHERE s_et.uuid LIKE '422b7e0c-b8f3-4748-8e60-d6684315f141'
    )
    AND ROUND(DATEDIFF(e.encounter_datetime, p.birthdate) / 365.25, 0) >= 40
    AND e.patient_id NOT IN (
        SELECT patient_id 
        FROM encounter s_e
        WHERE s_e.encounter_type = (
            SELECT encounter_type_id 
            FROM encounter_type ss_et 
            WHERE ss_et.uuid LIKE '422b7e0c-b8f3-4748-8e60-d6684315f141'
        ) 
        AND s_e.encounter_datetime <= :startDate
    )

UNION ALL

-- Women aged 30 to 49 years with CCS form filled out for the first time
SELECT 
    'Registered women aged 30 to 49 years that have had their CCS screening for the first time' AS 'Indicator', 
    CAST(COUNT(DISTINCT p.person_id) AS CHAR) AS 'Value'
FROM 
    encounter e 
INNER JOIN 
    person p ON e.patient_id = p.person_id
WHERE 
    e.location_id in (:locationList)
    AND e.encounter_datetime > :startDate
    AND e.encounter_datetime < :endDate
    AND e.encounter_type = (
        SELECT encounter_type_id 
        FROM encounter_type s_et 
        WHERE s_et.uuid LIKE '3fd606b6-4c9d-4077-a532-c1ac58644ad2'
    )
    AND ROUND(DATEDIFF(e.encounter_datetime, p.birthdate) / 365.25, 0) BETWEEN 30 AND 49
    AND e.patient_id NOT IN (
        SELECT patient_id 
        FROM encounter s_e
        WHERE s_e.encounter_type = (
            SELECT encounter_type_id 
            FROM encounter_type ss_et 
            WHERE ss_et.uuid LIKE '3fd606b6-4c9d-4077-a532-c1ac58644ad2'
        ) 
        AND s_e.encounter_datetime <= :startDate
    )

UNION ALL

-- 80% (of women aged 30 to 49 years with CCS form filled out for the first time) were VIA positive, and referred
SELECT 
    '80% (of registered patients with a Follow-up date) were given medication with at least a 3 weeks prescription' AS 'Indicator',
    CASE 
        WHEN SUM(CASE 
            WHEN o_v_p.value_coded = (
                SELECT concept_id 
                FROM concept 
                WHERE uuid LIKE '703AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA'
            ) 
            AND o_f.concept_id = (
                SELECT concept_id 
                FROM concept 
                WHERE uuid LIKE '758b9dd8-b6d0-4ac2-b245-0e7bffb4693a'
            ) THEN 1 
            ELSE 0 
        END) >= 0.8 * COUNT(*) THEN 'Yes'
        ELSE 'No'
    END AS 'Value'
FROM 
    encounter e 
INNER JOIN 
    person p ON e.patient_id = p.person_id
LEFT OUTER JOIN 
    obs o_v_p ON o_v_p.encounter_id = e.encounter_id 
    AND o_v_p.concept_id = (
        SELECT concept_id 
        FROM concept 
        WHERE uuid LIKE '27912a31-4b1e-40d4-a3a0-947e0eb2e588'
    ) 
    AND o_v_p.value_coded = (
        SELECT concept_id 
        FROM concept 
        WHERE uuid LIKE '703AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA'
    )
LEFT OUTER JOIN 
    obs o_f ON o_f.encounter_id = e.encounter_id 
    AND o_f.concept_id = (
        SELECT concept_id 
        FROM concept 
        WHERE uuid LIKE '758b9dd8-b6d0-4ac2-b245-0e7bffb4693a'
    )
WHERE 
    e.location_id in (:locationList)
    AND e.encounter_datetime > :startDate
    AND e.encounter_datetime < :endDate
    AND e.encounter_type = (
        SELECT encounter_type_id 
        FROM encounter_type s_et 
        WHERE s_et.uuid LIKE '3fd606b6-4c9d-4077-a532-c1ac58644ad2'
    )
    AND ROUND(DATEDIFF(e.encounter_datetime, p.birthdate) / 365.25, 0) BETWEEN 30 AND 49
    AND e.patient_id NOT IN (
        SELECT patient_id 
        FROM encounter s_e
        WHERE s_e.encounter_type = (
            SELECT encounter_type_id 
            FROM encounter_type ss_et 
            WHERE ss_et.uuid LIKE '3fd606b6-4c9d-4077-a532-c1ac58644ad2'
        ) 
        AND s_e.encounter_datetime <= :startDate
    )

UNION ALL

-- 80% have a Follow-up date and were given medication at least 3 weeks ago
SELECT
    '80% (of registered women aged 30 to 49 years that have had their CCS screening for the first time) were VIA positive and referred' AS 'Indicator',
    CASE 
        WHEN SUM(CASE 
            WHEN medication_table.medication_duration_in_weeks >= 4 THEN 1 
            ELSE 0 
        END) >= 0.8 * COUNT(*) THEN 'Yes'
        ELSE 'No'
    END AS 'Value'
FROM 
    patient p 
INNER JOIN 
    encounter e ON p.patient_id = e.patient_id 
INNER JOIN 
    obs o_f ON e.encounter_id = o_f.encounter_id 
    AND o_f.concept_id = (
        SELECT concept_id 
        FROM concept 
        WHERE uuid LIKE 'e9c145c1-f4e9-4c34-b237-da069939dc38'
    )
LEFT OUTER JOIN
    (
        SELECT 
            CASE 
                WHEN c.uuid LIKE '1822AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' THEN (d_o.duration / 672)
                WHEN c.uuid LIKE '1072AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' THEN (d_o.duration / 7)
                WHEN c.uuid LIKE '1073AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' THEN d_o.duration
                WHEN c.uuid LIKE '1074AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' THEN (d_o.duration * 4.34524)
                WHEN c.uuid LIKE '1734AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' THEN (d_o.duration * 52.1429)
                ELSE 0
            END AS 'medication_duration_in_weeks',
            o.encounter_id
        FROM 
            drug_order d_o
        INNER JOIN 
            orders o ON d_o.order_id = o.order_id
        INNER JOIN 
            concept c ON d_o.duration_units = c.concept_id
    ) medication_table ON medication_table.encounter_id = e.encounter_id
WHERE 
    e.location_id in (:locationList)
    AND o_f.value_datetime BETWEEN :startDate AND :endDate
