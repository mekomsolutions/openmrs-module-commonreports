## Disbursement Report
It is a monthly report requested for NCD and CCS screening visits.

**Setup**

It is setup by setting the following properties in [initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    "report.MSPP.disbursement.active" : "true"
}
```
`report.MSPP.disbursement.active` activates the report to be usable when the module is loaded.


Find the report template below 

```
+----------------------------------------------------------------------------------------------------------------------------------------------------------+-------+
|Indicator                                                                                                                                                 |Value  |
|----------------------------------------------------------------------------------------------------------------------------------------------------------+-------+
|Registered patients aged 40 and above that have had their NCD screening for the first time                                                                |55     |
|Registered women aged 30 to 49 years that have had their CCS screening for the first time                                                                 |66     |
|80% (of registered patients with a Follow-up date and diagnosed with Hypertension & Diabetes) were given medication with at least a 4 weeks prescription  |Yes/No |
|80% (of registered patients with a Follow-up date) were given medication with at least a 4 weeks prescription                                             |Yes/No |
+----------------------------------------------------------------------------------------------------------------------------------------------------------+-------+
```