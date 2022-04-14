## Family Planning
This report searches different lab tests. It is a MSPP Report requested by the Haitian MoH to any clinic in Haiti.

**Setup**

It is setup by setting the following properties in [initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
    "report.MSPP.familyPlanning.active" : "true",
    "report.MSPP.familyPlanning.FPAdministred" : "uuid-to-FP-Administred-concept",
    "report.MSPP.familyPlanning.familyPlanning" : "uuid-to-Family-Planning-concept",
    "report.MSPP.familyPlanning.typeOfUser" : "uuid-to-Type-Of-User-concept",
    "report.MSPP.familyPlanning.new" : "uuid-to-New-concept",
    "report.MSPP.familyPlanning.existent" : "uuid-to-Existent-concept",
    "report.MSPP.familyPlanning.microgynon" : "uuid-to-Microgynon-concept",
    "report.MSPP.familyPlanning.microlut" : "uuid-to-Microlut-concept",
    "report.MSPP.familyPlanning.depoProveraInjection" : "uuid-to-Depo-Provera-Injection-concept",
    "report.MSPP.familyPlanning.jadel" : "uuid-to-Jadel-concept",
    "report.MSPP.familyPlanning.condom" : "uuid-to-Condom-concept"
}
```
`report.MSPP.familyPlanning.active` activates the report to be usable when the module is loaded.
The report doesn't have any visit type filter.
The report template can be found at [MSPP 'Clients PF' report](https://docs.google.com/spreadsheets/d/13A3gBRwi45-YwnArNsDgQB4EPVwsTswp/edit#gid=906556663)