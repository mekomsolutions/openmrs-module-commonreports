select
    condition_id,
    previous_condition_id,
    patient_id,
    status,
    concept_id,
    condition_coded,
    condition_non_coded,
    onset_date,
    additional_detail,
    end_date,
    end_reason,
    creator,
    date_created,
    voided,
    voided_by,
    date_voided,
    void_reason,
    uuid
from
    conditions
WHERE
    onset_date >= :onsetDate
    AND end_date IS NULL
    OR end_date <= :endDate