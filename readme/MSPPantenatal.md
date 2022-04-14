## Antenatal Reports
This report searches different lab tests. It is a MSPP Report requested by the Haitian MoH to any clinic in Haiti.

**Setup**

It is setup by setting the following properties in [Initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
    "report.MSPP.antenatal.active": "true",
    "report.MSPP.antenatal.gestationDuration" : "list-of-gestation-phases",
    "report.MSPP.antenatal.numberOfWeeks" : "uuid-to-number-of-weeks-concept",
    "report.MSPP.antenatal.estimatedGestationalAge" : "uuid-to-estimated-gestation-concept",
    "report.MSPP.antenatal.ferrousFolate" : "uuid-to-ferrous-folate-concept",
    "report.MSPP.antenatal.chloroquine" : "uuid-to-chloroquine-concept",
    "report.MSPP.antenatal.drugOrder" : "uuid-to-drug-order-concept",
    "report.MSPP.antenatal.prenatalVisitType" : "uuid-to-prenatal-visit-types-concept",
    "report.MSPP.antenatal.otherVisitTypes" : "list-of-uuids-of-other-visit-types-except-prenatal-visit-type",
    "report.MSPP.antenatal.riskyPregnancy" : "uuid-to-risky-pregnancy-concept",
    "report.MSPP.antenatal.codedDiagnosis" : "uuid-to-coded-diagnosis-concept",
    "report.MSPP.antenatal.anemiaIronDeficiency" : "uuid-to-anemia-Iron-Deficiency-concept",
    "report.MSPP.antenatal.malaria" : "uuid-to-malaria-concept",
    "report.MSPP.antenatal.positive" : "uuid-to-positive-concept",
    "report.MSPP.antenatal.onePlus" : "uuid-to-one-plus-concept",
    "report.MSPP.antenatal.twoPlus" : "uuid-to-two-plus-concept",
    "report.MSPP.antenatal.threePlus" : "uuid-to-three-plus-concept",
    "report.MSPP.antenatal.fourPlus" : "uuid-to-four-Plus-concept",
    "report.MSPP.antenatal.midUpperArmCircumference" : "uuid-to-Mid-Upper-Arm-Circumference-concept",
    "report.MSPP.antenatal.visitNumber" : "uuid-to-visit-number-concept",
    "report.MSPP.antenatal.one" : "uuid-to-one-concept",
    "report.MSPP.antenatal.two" : "uuid-to-two-concept",
    "report.MSPP.antenatal.three" : "uuid-to-three-concept",
    "report.MSPP.antenatal.four" : "uuid-to-four-concept",
    "report.MSPP.antenatal.fivePlus" : "uuid-to-five-plus-concept",
    "report.MSPP.antenatal.yes" : "uuid-to-yes-concept"
}
```
`report.MSPP.antenatal.active` activates the antenatal report to be usable when the module is loaded.
An example of `report.MSPP.antenatal.gestationDuration` is "0-13,14-27,28-40,Total".

The report template can be found at [MSPP: Prise en charge de la mère](https://docs.google.com/spreadsheets/d/13A3gBRwi45-YwnArNsDgQB4EPVwsTswp/edit#gid=477266631)