## Orders Report
This report searches through patient Orders.

**Setup**

It is setup by setting the following properties in [initializer](https://github.com/mekomsolutions/openmrs-module-initializer) `jsonkeyvalues`' domain configuration. 

```bash
{
    ...
    ...,
    "report.orders.active" : "true"
}
```
`report.orders.active` activates the report to be usable when the module is loaded.
