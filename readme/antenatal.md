## Antenatal Reports
This report searches different lab tests. It is a MSPP Report requested by the Haitian MoH to any clinic in Haiti.

**Setup**

It is setup by setting the following properties in [Initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
    "report.antenatal.active": "true",
    "report.antenatal.gestationDuration" : "list-of-gestation-phases",
    "report.antenatal.numberOfWeeks" : "uuid-to-number-of-weeks-concept",
    "report.antenatal.estimatedGestationalAge" : "uuid-to-estimated-gestation-concept",
    "report.antenatal.ferrousFolate" : "uuid-to-ferrous-folate-concept",
    "report.antenatal.chloroquine" : "uuid-to-chloroquine-concept",
    "report.antenatal.drugOrder" : "uuid-to-drug-order-concept",
    "report.antenatal.prenatalVisitType" : "uuid-to-prenatal-visit-types-concept",
    "report.antenatal.otherVisitTypes" : "list-of-uuids-of-other-visit-types-except-prenatal-visit-type",
    "report.antenatal.riskyPregnancy" : "uuid-to-risky-pregnancy-concept",
    "report.antenatal.codedDiagnosis" : "uuid-to-coded-diagnosis-concept",
    "report.antenatal.anemiaIronDeficiency" : "uuid-to-anemia-Iron-Deficiency-concept",
    "report.antenatal.malaria" : "uuid-to-malaria-concept",
    "report.antenatal.positive" : "uuid-to-positive-concept",
    "report.antenatal.onePlus" : "uuid-to-one-plus-concept",
    "report.antenatal.twoPlus" : "uuid-to-two-plus-concept",
    "report.antenatal.threePlus" : "uuid-to-three-plus-concept",
    "report.antenatal.fourPlus" : "uuid-to-four-Plus-concept",
    "report.antenatal.midUpperArmCircumference" : "uuid-to-Mid-Upper-Arm-Circumference-concept",
    "report.antenatal.visitNumber" : "uuid-to-visit-number-concept",
    "report.antenatal.one" : "uuid-to-one-concept",
    "report.antenatal.two" : "uuid-to-two-concept",
    "report.antenatal.three" : "uuid-to-three-concept",
    "report.antenatal.four" : "uuid-to-four-concept",
    "report.antenatal.fivePlus" : "uuid-to-five-plus-concept",
    "report.antenatal.yes" : "uuid-to-yes-concept"
}
```
`report.antenatal.active` activates the antenatal report to be usable when the module is loaded.
An example of `report.antenatal.gestationDuration` is "0-13,14-27,28-40,Total".

The report template can be found at [MSPP: Prise en charge de la mère](https://docs.google.com/spreadsheets/d/13A3gBRwi45-YwnArNsDgQB4EPVwsTswp/edit#gid=477266631)