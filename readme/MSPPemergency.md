## Emergency report
This is a MSPP  Statistic Report on emergency cases.

**Setup**

It is setup by setting the following properties in the [initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
    "report.MSPP.emergency.active " : "true",
    "report.MSPP.emergency.question.concept" : "uuid-to-emergency-question-concept",
    "report.MSPP.emergency.roadAccidents.concept" : "uuid-to-road-accidentTypes-setConcept",
    "report.MSPP.emergency.workAccident.concept" : "uuid-to-work-accident-concept",
    "report.MSPP.emergency.sexualViolence.concept" : "uuid-to-sexualViolence-concept",
    "report.MSPP.emergency.physicalViolence.concept" : "uuid-to-physicalViolence-concept",
    "report.MSPP.emergency.otherViolenceType.concept" : "uuid-to-otherViolence-types-concept",
    "report.MSPP.emergency.medicalAndSurgicalEmergenciesQuesion.concept" : "uuid-to-medicalAndSurgicalEmergency-question-concept",
    "report.MSPP.emergency.medicalAndSurgicalEmergenciesSetOfSets.concept" : "uuid-to-medicalAndSurgicalEmergency-superSet-concept",
    "report.MSPP.emergency.otherEmergencies.concept" : "uuid-to-otherEmergencies-concept",
    "report.MSPP.emergency.otherEmergenciesQuestion.concept" : "uuid-to-otherEmergencies-question-concept",
    "report.MSPP.emergency.referral.concept" : "uuid-to-referral-questionConcept-or-obsGroupingConcept",
    "report.MSPP.emergency.leftWithoutPermission.concept" : "uuid-to-leftWithoutPermission-concept",
    "report.MSPP.emergency.yes.concept" : "uuid-to-yes-answer-concept"
}
```
`report.MSPP.emergency.active` activates the report to be usable when the module is loaded.

`report.MSPP.emergency.question.concept` specifies the question answered by the type of emergency, it could be the Reason for consultation question concept.

`report.MSPP.emergency.roadAccidents.conceptSet` specifies a concept set defining the types of road accidents to report on. The concepts specified should be answering the concept defined by the `report.MSPP.emergency.question.concept`.

`report.MSPP.emergency.workAccident.concept` specifies a concept defining a work accident emergency. It should be answering the concept defined by the `report.MSPP.emergency.question.concept`.

`report.MSPP.emergency.physicalViolence.concept` specifies a concept defining a physical violence emergency. It should be answering the concept defined by the `report.MSPP.emergency.question.concept`.

`report.MSPP.emergency.sexualViolence.concept` specifies a concept defining sexual violence emergency. It should be answering the concept defined by the `report.MSPP.emergency.question.concept`.

`report.MSPP.emergency.otherViolenceType.concept` specifies a concept defining other violence type emergency. It should be answering the concept defined by the `report.MSPP.emergency.question.concept` property.

`report.MSPP.emergency.medicalAndSurgicalEmergenciesQuesion.concept` points to the question answered by the coded answers specified by the `report.MSPP.emergency.medicalAndSurgicalEmergenciesSetOfSets.concept` property. Eg, 'Visit Diagnoses'.

`report.MSPP.emergency.medicalAndSurgicalEmergenciesSetOfSets.concept` specifies a concept set that informs of 'Medical and Surgical Emergency' categories. These categories are sets that group all concepts to be reported on `report.MSPP.emergency.medicalAndSurgicalEmergenciesQuesion.concept`.

See an example of the concept Set of Sets structure:

- Medical and Surgical Emergency <- report.MSPP.emergency.medicalAndSurgicalEmergenciesSetOfSets.concept
    - Digestive
      - K29.7
      - K35
    - Unirary
      - N10
      - N20.0

`report.MSPP.emergency.otherEmergenciesQuestion.concept` specifies a coded question answered by the coded answers specified by the `report.MSPP.emergency.otherEmergencies.concept` property.

`report.MSPP.emergency.otherEmergencies.conceptSet` specifies a set of coded set members answering the question defined by `report.MSPP.emergency.otherEmergenciesQuestion.concept` property.

`report.MSPP.emergency.referral.concept` specifies a concept for reporting on referred patients. It could be a coded question which when answered means the patient has been referred or concept that defines a group of observations capturing referral data.

`report.MSPP.emergency.leftWithoutPermission.concept` specifies a coded question concept which allows for determining left without permission outcome cases. Trues cases are determined by the coded answer defined by `report.MSPP.emergency.yes.concept` property. 

`report.MSPP.emergency.yes.concept` specifies a `Yes`/`True` coded answer concept answering the the questino defined by `report.MSPP.emergency.leftWithoutPermission.concept` property.

**Note:** “Deceased” outcome cases take precedence over the other outcomes, followed by "Left without permission", "Referred", "Cared for" outcomes respectively.

Find the report template at [MSPP Statistic Report - Emergency](https://docs.google.com/spreadsheets/d/13A3gBRwi45-YwnArNsDgQB4EPVwsTswp/edit#gid=137605556).
