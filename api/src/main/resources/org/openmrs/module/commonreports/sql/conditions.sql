select
    *
from
    conditions
    WHERE
    onset_date >= :onsetDate
    AND end_date IS NULL OR end_date <= :endDate