SELECT
    COUNT(
        DISTINCT CASE
            WHEN (
                age < 1
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) IS NULL
                )
            ) THEN pd
        END
    ) LT1N,
    COUNT(
        DISTINCT CASE
            WHEN (
                age < 1
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) = pd
                )
            ) THEN pd
        END
    ) LT1S,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 1
                AND age < 5
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) IS NULL
                )
            ) THEN pd
        END
    ) 1T4N,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 1
                AND age < 5
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) = pd
                )
            ) THEN pd
        END
    ) 1T4S,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 5
                AND age < 10
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) IS NULL
                )
            ) THEN pd
        END
    ) 5T9N,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 5
                AND age < 10
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) = pd
                )
            ) THEN pd
        END
    ) 5T9S,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 10
                AND age < 15
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) IS NULL
                )
            ) THEN pd
        END
    ) 10T14N,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 10
                AND age < 15
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) = pd
                )
            ) THEN pd
        END
    ) 10T14S,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 15
                AND age < 20
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) IS NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT patient_id
                        FROM
                            visit
                            JOIN visit_type ON visit.visit_type_id = visit_type.visit_type_id
                        WHERE
                            patient_id = pd
                            AND date_started >= :startDate
                            AND visit_type.uuid <> :familyPlanningVisitTypeUuid
                            AND visit_type.uuid <> :prenatalVisitTypeUuid
                    ) = pd
                )
            ) THEN pd
        END
    ) 15T19N,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 15
                AND age < 20
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) = pd
                )
                AND (
                    (
                        SELECT
                            DISTINCT patient_id
                        FROM
                            visit
                            JOIN visit_type ON visit.visit_type_id = visit_type.visit_type_id
                        WHERE
                            patient_id = pd
                            AND date_started >= :startDate
                            AND visit_type.uuid <> :familyPlanningVisitTypeUuid
                            AND visit_type.uuid <> :prenatalVisitTypeUuid
                    ) = pd
                )
            ) THEN pd
        END
    ) 15T19S,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 20
                AND age < 25
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) IS NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT patient_id
                        FROM
                            visit
                            JOIN visit_type ON visit.visit_type_id = visit_type.visit_type_id
                        WHERE
                            patient_id = pd
                            AND date_started >= :startDate
                            AND visit_type.uuid <> :familyPlanningVisitTypeUuid
                            AND visit_type.uuid <> :prenatalVisitTypeUuid
                    ) = pd
                )
            ) THEN pd
        END
    ) 20T24N,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 20
                AND age < 25
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) = pd
                )
                AND (
                    (
                        SELECT
                            DISTINCT patient_id
                        FROM
                            visit
                            JOIN visit_type ON visit.visit_type_id = visit_type.visit_type_id
                        WHERE
                            patient_id = pd
                            AND date_started >= :startDate
                            AND visit_type.uuid <> :familyPlanningVisitTypeUuid
                            AND visit_type.uuid <> :prenatalVisitTypeUuid
                    ) = pd
                )
            ) THEN pd
        END
    ) 20T24S,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 15
                AND 'Bahmni^Consultation prénatale' IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path LIKE 'Bahmni^Consultation pr%'
                    ) IS NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT patient_id
                        FROM
                            visit
                            JOIN visit_type ON visit.visit_type_id = visit_type.visit_type_id
                        WHERE
                            patient_id = pd
                            AND date_started >= :startDate
                            AND visit_type.uuid = :prenatalVisitTypeUuid
                    ) = pd
                )
            ) THEN pd
        END
    ) GT15ANCN,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 15
                AND 'Bahmni^Consultation prénatale' IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path LIKE 'Bahmni^Consultation pr%'
                    ) = pd
                )
                AND (
                    (
                        SELECT
                            DISTINCT patient_id
                        FROM
                            visit
                            JOIN visit_type ON visit.visit_type_id = visit_type.visit_type_id
                        WHERE
                            patient_id = pd
                            AND date_started >= :startDate
                            AND visit_type.uuid = :prenatalVisitTypeUuid
                    ) = pd
                )
            ) THEN pd
        END
    ) GT15ANCS,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 15
                AND 'Bahmni^Planification familiale' IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path LIKE 'Bahmni^Planification familiale%'
                    ) IS NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT patient_id
                        FROM
                            visit
                            JOIN visit_type ON visit.visit_type_id = visit_type.visit_type_id
                        WHERE
                            patient_id = pd
                            AND date_started >= :startDate
                            AND visit_type.uuid = :familyPlanningVisitTypeUuid
                    ) = pd
                )
            ) THEN pd
        END
    ) GT15FPN,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 15
                AND 'Bahmni^Planification familiale' IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path LIKE 'Bahmni^Planification familiale%'
                    ) = pd
                )
                AND (
                    (
                        SELECT
                            DISTINCT patient_id
                        FROM
                            visit
                            JOIN visit_type ON visit.visit_type_id = visit_type.visit_type_id
                        WHERE
                            patient_id = pd
                            AND date_started >= :startDate
                            AND visit_type.uuid = :familyPlanningVisitTypeUuid
                    ) = pd
                )
            ) THEN pd
        END
    ) GT15FPS,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 25
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) IS NULL
                )
            ) THEN pd
        END
    ) GT24OTN,
    COUNT(
        DISTINCT CASE
            WHEN (
                age >= 25
                AND 'Bahmni^Planification familiale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Consultation prénatale' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie gynécologique' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 1' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 2' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND 'Bahmni^Echographie obstétricale 3' NOT IN (
                    SELECT
                        DISTINCT SUBSTRING_INDEX(form_namespace_and_path, '\.', 1)
                    FROM
                        obs
                    where
                        obs.person_id = pd
                        and obs.obs_datetime >= :startDate
                        AND obs.obs_datetime <= :endDate
                        AND obs.form_namespace_and_path IS NOT NULL
                )
                AND (
                    (
                        SELECT
                            DISTINCT person_id
                        FROM
                            obs
                        WHERE
                            person_id = pd
                            AND obs_datetime < :startDate
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Planification familiale%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie gyn%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Echographie obst%'
                            AND form_namespace_and_path NOT LIKE 'Bahmni^Consultation pr%'
                    ) = pd
                )
            ) THEN pd
        END
    ) GT24OTS
FROM
    (
        SELECT
            person_id as pd,
            form,
            age
        FROM
            (
                SELECT
                    obs.person_id,
                    SUBSTRING_INDEX(form_namespace_and_path, '\.', 1) as form,
                    round(
                        DATEDIFF(obs.obs_datetime, person.birthdate) / 365.25,
                        1
                    ) as age
                FROM
                    obs
                    INNER JOIN person ON obs.person_id = person.person_id
                where
                    obs_datetime >= :startDate
                    AND obs_datetime <= :endDate
                    AND obs.voided = 0
                    AND form_namespace_and_path IS NOT NULL
            ) initialTable
        GROUP BY
            person_id,
            form
    ) breeze_table;