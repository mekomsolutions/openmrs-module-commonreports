SELECT
count(if( concept_id=:FPAdministred && value_coded=:microgynon && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 1 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newMycogynonFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:microgynon && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 1 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newMycogynonFemaleGT25,
count(if( concept_id=:FPAdministred && value_coded=:microgynon && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 1 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentMycogynonFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:microgynon && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 1 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentMycogynonFemaleGT25,

count(if( concept_id=:FPAdministred && value_coded=:microlut && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 1 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newMicrolutFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:microlut && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 1 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newMicrolutFemaleGT25,
count(if( concept_id=:FPAdministred && value_coded=:microlut && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 1 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentMicrolutFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:microlut && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 1 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentMicrolutFemaleGT25,

count(if( concept_id=:FPAdministred && value_coded=:depoProveraInjection && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 3 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newDepoFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:depoProveraInjection && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 3 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newDepoFemaleGT25,
count(if( concept_id=:FPAdministred && value_coded=:depoProveraInjection && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 3 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentDepoFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:depoProveraInjection && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 3 MONTH) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentDepoFemaleGT25,

count(if( concept_id=:FPAdministred && value_coded=:jadel && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 5 YEAR) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newJadelFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:jadel && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 5 YEAR) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newJadelFemaleGT25,
count(if( concept_id=:FPAdministred && value_coded=:jadel && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 5 YEAR) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentJadelFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:jadel && :FPObsGroupId where obs_datetime >= DATE_SUB(:startDate, INTERVAL 5 YEAR) AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentJadelFemaleGT25,

count(if( concept_id=:FPAdministred && value_coded=:condom && :FPObsGroupId where obs_datetime >= :startDate AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newCondomFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:condom && :FPObsGroupId where obs_datetime >= :startDate AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newCondomFemaleGT25,
count(if( concept_id=:FPAdministred && value_coded=:condom && :FPObsGroupId where obs_datetime >= :startDate AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleLT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentCondomFemaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:condom && :FPObsGroupId where obs_datetime >= :startDate AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :femaleGT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentCondomFemaleGT25,

count(if( concept_id=:FPAdministred && value_coded=:condom && :FPObsGroupId where obs_datetime >= :startDate AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :maleLT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newCondomMaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:condom && :FPObsGroupId where obs_datetime >= :startDate AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :maleGT25 AND concept_id=:typeOfUser AND value_coded=:new) ,1,null)) as newCondomMaleGT25,
count(if( concept_id=:FPAdministred && value_coded=:condom && :FPObsGroupId where obs_datetime >= :startDate AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :maleLT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentCondomMaleLT25,
count(if( concept_id=:FPAdministred && value_coded=:condom && :FPObsGroupId where obs_datetime >= :startDate AND obs_datetime <= :endDate AND concept_id=:familyPlanning) AND :maleGT25 AND concept_id=:typeOfUser AND value_coded=:existent) ,1,null)) as existentCondomMaleGT25
from obs inner join person ON obs.person_id=person.person_id where obs.voided = 0 ;


