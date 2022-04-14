## Vaccination
This report searches different vaccinations. It is a MSPP Report requested by the Haitian MoH to any clinic in Haiti.

**Setup**

It is setup by setting the following properties in [Initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
        "report.MSPP.vaccination.active": "true",
        "report.MSPP.ecv.active": "true",
        "report.MSPP.vaccination.prenatalVisitType": "uuid-to-prenatal-visitType",
        "report.MSPP.vaccination.vaccinations": "uuid-to-vaccinations-question-concept",
        "report.MSPP.vaccination.vaccinationSequenceNumberConcept": "uuid-to-vaccination-sequence-number-concept",
        "report.MSPP.vaccination.vaccinationConceptsListWithSequence":"uuids-to-vaccinations:booster-or-sequence-number",
        "report.MSPP.vaccination.ecvList" : "uuid-to-ecv:booster-or-sequence-number",
        "report.MSPP.vaccination.vaccinationDateConcept" : "uuid-to-vaccination-date-concept"

}
```
`report.MSPP.vaccination.active` activates the vaccinations report to be usable when the module is loaded (without the ECV row).
`report.MSPP.ecv.active` activates the ECV part of the vaccinations report to be usable when the module is loaded.
`report.MSPP.vaccination.prenatalVisitType` specifies the UUID of the prenatal visit type to filter on.
`report.MSPP.vaccination.vaccinations` specifies the UUID of the vaccination question concept.
`report.MSPP.vaccination.vaccinationSequenceNumberConcept`specifies UUID of the vaccination sequence number concept.
`report.MSPP.vaccination.vaccinationConceptsListWithSequence` specifies the UUIDs of individual vaccinations with their sequence or booster number separated by a full-colon for example `"9199a4c6-b15f-482c-aa0e-c839bd0cf535,0661812a-6ff7-42f9-bbe7-f79c5bdd0f58:1,0661812a-6ff7-42f9-bbe7-f79c5bdd0f58:2"`
`report.MSPP.vaccination.ecvList` specifies the UUIDs of individual vaccinations for the full pediatric coverage with their sequence or booster number separated by a full-colon for example `"9199a4c6-b15f-482c-aa0e-c839bd0cf535,0661812a-6ff7-42f9-bbe7-f79c5bdd0f58:2,30fd2e76-bfcb-49b4-8668-64fd3d3ec0ea`. Currently the report expects this to be all children that have received 1 dose of BCG, 1 dose of VPI, 3 doses of Penta, 2 doses of VPO, 2 doses of Rota and 1 dose of RR
`report.MSPP.vaccination.vaccinationDateConcept` specifies the UUID of the vaccination date concept.

The report template can be found at [MSPP: Vaccination](https://docs.google.com/spreadsheets/d/13A3gBRwi45-YwnArNsDgQB4EPVwsTswp/edit#gid=1856133398)