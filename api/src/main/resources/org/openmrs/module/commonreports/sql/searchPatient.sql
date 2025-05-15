SELECT
    name.given_name AS given_name,
    name.middle_name AS middle_name,
    name.family_name AS family_name,
    identifier.identifier AS identifier,
    person.gender AS gender,
    person.birthdate AS birthdate,
    person.birthdate_estimated AS birthdate_estimated,
    address.city_village AS city,
    address.address1 AS address1,
    address.address2 AS address2,
    address.state_province AS state_province,
    address.country AS country,
    person.dead AS dead,
    person.death_date AS death_date,
    person.cause_of_death AS cause_of_death,
    person.creator AS creator,
    person.date_created AS date_created,
    person.voided AS person_voided,
    person.void_reason AS person_void_reason
FROM
    patient
    LEFT JOIN person person ON patient.patient_id = person.person_id
    LEFT JOIN person_name name ON person.person_id = name.person_id
    LEFT JOIN person_address address ON person.person_id = address.person_id
    LEFT JOIN patient_identifier identifier ON patient.patient_id = identifier.patient_id
WHERE (
    (:patientNameOrID IS NOT NULL)
    AND (
        (identifier.identifier LIKE CONCAT('%', :patientNameOrID, '%'))
        OR 
        (CONCAT_WS(' ', name.given_name, name.middle_name, name.family_name) LIKE CONCAT('%', :patientNameOrID, '%'))
    )
)
AND identifier.identifier_type = (
    SELECT patient_identifier_type_id
    FROM patient_identifier_type
    WHERE name = 'MSF ID'
    LIMIT 1
)
AND patient.voided = 0
AND identifier.voided = 0
AND name.voided = 0
AND person.voided = 0;
