## Family Planning
This report searches different lab tests. It is a MSPP Report requested by the Haitian MoH to any clinic in Haiti.

**Setup**

It is setup by setting the following properties in [initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
    "report.familyPlanning.active" : "true",
    "report.familyPlanning.FPAdministred" : "uuid-to-FP-Administred-concept",
    "report.familyPlanning.familyPlanning" : "uuid-to-Family-Planning-concept",
    "report.familyPlanning.typeOfUser" : "uuid-to-Type-Of-User-concept",
    "report.familyPlanning.new" : "uuid-to-New-concept",
    "report.familyPlanning.existent" : "uuid-to-Existent-concept",
    "report.familyPlanning.microgynon" : "uuid-to-Microgynon-concept",
    "report.familyPlanning.microlut" : "uuid-to-Microlut-concept",
    "report.familyPlanning.depoProveraInjection" : "uuid-to-Depo-Provera-Injection-concept",
    "report.familyPlanning.jadel" : "uuid-to-Jadel-concept",
    "report.familyPlanning.condom" : "uuid-to-Condom-concept"
}
```
`report.familyPlanning.active` activates the report to be usable when the module is loaded.
The report doesn't have any visit type filter.
The report template can be found at [MSPP 'Clients PF' report](https://docs.google.com/spreadsheets/d/13A3gBRwi45-YwnArNsDgQB4EPVwsTswp/edit#gid=906556663)