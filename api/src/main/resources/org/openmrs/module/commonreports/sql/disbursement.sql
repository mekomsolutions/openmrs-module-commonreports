SELECT 'Patients aged 40 and above with NCD form filled out for the rist time' AS 'Indicator', 
	CAST(COUNT(DISTINCT(p.person_id)) AS CHAR) AS 'Value'
FROM encounter e INNER JOIN person p ON e.patient_id = p.person_id
where e.encounter_datetime > '2023-07-31'
AND e.encounter_datetime < '2023-09-01'
AND e.encounter_type = (select encounter_type_id from encounter_type s_et where s_et.`name` like 'Health Center - NCD Screening')
AND round(DATEDIFF(e.encounter_datetime, p.birthdate)/365.25, 0) >= 40
AND e.patient_id NOT IN
(select patient_id 
from encounter s_e
where s_e.encounter_type=(select encounter_type_id from encounter_type ss_et where ss_et.`name` like 'Health Center - NCD Screening') and s_e.encounter_datetime <= '2023-07-31')

UNION ALL

SELECT 'Women aged 30 to 49 years with CCS form filled out for the first time' as 'Indicator', 
	CAST(COUNT(DISTINCT(p.person_id)) AS CHAR) AS 'Value'
FROM encounter e INNER JOIN person p ON e.patient_id = p.person_id
WHERE e.encounter_datetime > '2023-11-31'
AND e.encounter_datetime < '2023-12-31'
AND e.encounter_type = (select encounter_type_id from encounter_type s_et where s_et.`name` like 'Cervical Cancer Screening')
AND round(DATEDIFF(e.encounter_datetime, p.birthdate)/365.25, 0) >= 30
AND round(DATEDIFF(e.encounter_datetime, p.birthdate)/365.25, 0) <= 49
AND e.patient_id NOT IN
(select patient_id 
from encounter s_e
where s_e.encounter_type=(select encounter_type_id from encounter_type ss_et where ss_et.`name` like 'Cervical Cancer Screening') and s_e.encounter_datetime <= '2023-11-31')

UNION ALL

SELECT '80% had their first CCS screening, were VIA positive and referred' AS 'Indicator',
	CASE 
 		WHEN SUM(CASE WHEN o_v_p.value_coded = (select concept_id from concept where uuid like '703AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA') AND o_f.concept_id = (select concept_id from concept where uuid like '758b9dd8-b6d0-4ac2-b245-0e7bffb4693a') THEN 1 ELSE 0 END) >= 0.8 * COUNT(*) THEN 'Yes'
 		ELSE 'No'
 	END AS 'Value'
FROM encounter e 
INNER JOIN person p ON e.patient_id = p.person_id
LEFT OUTER JOIN obs o_v_p ON o_v_p.encounter_id = e.encounter_id AND o_v_p.concept_id = (select concept_id from concept where uuid like '27912a31-4b1e-40d4-a3a0-947e0eb2e588') AND o_v_p.value_coded = (select concept_id from concept where uuid like '703AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA')
LEFT OUTER JOIN obs o_f ON o_f.encounter_id = e.encounter_id AND o_f.concept_id = (select concept_id from concept where uuid like '758b9dd8-b6d0-4ac2-b245-0e7bffb4693a')
WHERE e.encounter_datetime > '2023-11-31'
AND e.encounter_datetime < '2023-12-31'
AND e.encounter_type = (select encounter_type_id from encounter_type s_et where s_et.`name` like 'Cervical Cancer Screening')
AND round(DATEDIFF(e.encounter_datetime, p.birthdate)/365.25, 0) >= 30
AND round(DATEDIFF(e.encounter_datetime, p.birthdate)/365.25, 0) <= 49
AND e.patient_id NOT IN
(select patient_id 
from encounter s_e
where s_e.encounter_type=(select encounter_type_id from encounter_type ss_et where ss_et.`name` like 'Cervical Cancer Screening') and s_e.encounter_datetime <= '2023-11-31')

UNION ALL

SELECT
	'80% have a Follow-up date and where given medication at-least 3 weeks ago' AS 'Indicator',
	CASE 
        WHEN SUM(CASE WHEN o_m.value_coded = (select concept_id from concept where uuid like '1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA') THEN 1 ELSE 0 END) >= 0.8 * COUNT(*) THEN 'Yes'
        ELSE 'No'
    END AS 'Value'
FROM patient p 
INNER JOIN encounter e 
	ON p.patient_id = e.patient_id 
INNER JOIN obs o_f 
	ON e.encounter_id = o_f.encounter_id AND o_f.concept_id = (select concept_id from concept where uuid like 'e9c145c1-f4e9-4c34-b237-da069939dc38')
INNER JOIN obs o_m
	ON e.encounter_id = o_m.encounter_id AND o_m.concept_id = (select concept_id from concept where uuid like '805c3a0b-cd38-4ed6-b4f8-f3af0fc118ad') AND o_m.value_coded = (select concept_id from concept where uuid like '1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA')    
WHERE ((o_f.value_datetime >= '2024-01-01' AND o_f.value_datetime <= '2024-01-3') AND (o_m.obs_datetime < '2024-01-01') OR (o_f.value_datetime >= '2024-01-01' AND o_f.value_datetime <= '2024-01-3'));
