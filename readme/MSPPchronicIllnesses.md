## Chronic Illnesses Report
This report searches through a new diagnoses and existing active conditions. It is a MSPP Report requested by the Haitian MoH to any clinic in Haiti.

**Setup**

It is setup by setting the following properties in [Initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
    "report.MSPP.chronicIllnesses.active" : "true",
    "report.MSPP.chronicIllnesses.conceptSet" : "uuid-to-the-concept-set-of-diagnoses",
    "report.MSPP.chronicIllnesses.referral.concept" : "uuid-to-referral-questionConcept-or-obsGroupingConcept",
    "report.MSPP.chronicIllnesses.diagnosisQuestion.concept" : "uuid-to-diagnosis-coded-question"
}
```
`report.MSPP.chronicIllnesses.active` activates the report to be usable when the module is loaded.

`report.MSPP.chronicIllnesses.conceptSet` specifies the diagnoses to filter from. This set's members will represent each row on the report.MSPP. If a member is a set itself, the report will aggregate such sub-members and to show it as one line.

`report.MSPP.chronicIllnesses.referral.concept` specifies a coded question concept which allows for determining referred outcome cases. The answer to this question is not looked at though: any obs saved with this concept will make the visit considered "Referred". This means this property can also point to a concept set (saved as obs group).

`report.MSPP.chronicIllnesses.diagnosisQuestion.concept` specifies a coded question to the captured recorded diagnoses answers.

Find the report template at [MSPP: Chronic Illnesses](https://docs.google.com/spreadsheets/d/13A3gBRwi45-YwnArNsDgQB4EPVwsTswp/edit#gid=704979704)
