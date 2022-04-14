## Lab
This report searches different lab tests. It is a MSPP Report requested by the Haitian MoH to any clinic in Haiti.

**Setup**

It is setup by setting the following properties in [Initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
    "report.MSPP.lab.active" : "true",
    "report.MSPP.lab.serialSputumBacilloscopy" : "uuid-to-serial-sputum-bacilloscopy-concept",
    "report.MSPP.lab.positive" : "uuid-to-positive-concept",
    "report.MSPP.lab.negative" : "uuid-to-negative-concept",
    "report.MSPP.lab.indeterminate" : "uuid-to-indeterminate-concept",
    "report.MSPP.lab.zero" : "uuid-to-zero-concept",
    "report.MSPP.lab.onePlus" : "uuid-to-One-plus-concept",
    "report.MSPP.lab.twoPlus" : "uuid-to-Two-plus-concept",
    "report.MSPP.lab.threePlus" : "uuid-to-Three-plus-concept",
    "report.MSPP.lab.fourPlus" : "uuid-to-Four-plus-concept",
    "report.MSPP.lab.malaria" : "uuid-to-Malaria-concept",
    "report.MSPP.lab.completeBloodCount" : "uuid-to-Complete-blood-count-concept",
    "report.MSPP.lab.sicklingTest" : "uuid-to-Sickling-test-concept",
    "report.MSPP.lab.bloodGroup" : "uuid-to-Blood-group-concept",
    "report.MSPP.lab.urinalysis" : "uuid-to-Urinalysis-concept",
    "report.MSPP.lab.prenatalVisitType" : "uuid-to-Prenatal-visit-type"
}
```
The prenatal visit type filter is used on some of the lab tests to differetiate between pregnant mothers and other female patients.
`report.MSPP.lab.active` activates the report to be usable when the module is loaded.
The report template can be found at [MSPP: Examens de laboratoire](https://docs.google.com/spreadsheets/d/13A3gBRwi45-YwnArNsDgQB4EPVwsTswp/edit#gid=2062213411)
