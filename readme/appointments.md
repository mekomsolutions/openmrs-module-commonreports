## Appointments Report
This report searches through patient Appointments.

**Setup**

It is setup by setting the following properties in [initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
    "report.appointments.active" : "true"
}
```
`report.appointments.active` activates the report to be usable when the module is loaded.
