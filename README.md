# "Common Reports" OpenMRS module
Common Reports bundles reports defined and evaluated by the [OpenMRS Reporting module](https://github.com/openmrs/openmrs-module-reporting).

It bundles two groups of reports:
1. **All-use reports** that are activated by default or
1. **Use-case specific reports** that need to be explicitely activated through Initializer's ['jsonkeyvalues'](https://github.com/mekomsolutions/openmrs-module-initializer/blob/master/readme/jsonkeyvalues.md#domain-jsonkeyvalues).<br/>:warning: Common Reports holds a hard dependency on Initializer ⇒ both modules need to be installed for Common Reports to work properly.

## List of Embedded Reports:
### General use reports (always activated)
* Patient History - an easy-to-read report of the entire patient file, or sections of the patient file, rendered as a PDF and that is printer friendly.
* Data Extracts reports - those are flattened tables provided as CSV extracts and available for various domains of OpenMRS data model
  * Appointments
  * Concepts
  * Conditions
  * Diagnoses
  * Encounters
  * Observations
  * Orders
  * Patients
  * Programs
  * Visits

### Opt-in Reports
#### Cambodia
* [MoH HC1](https://docs.google.com/spreadsheets/d/1AD59mP88wzTeV9pe3YyrBri4X7AFNnnqik4l1pTSaUs/edit?usp=sharing) '_I. Outpatient Consultation (OPD)_' report
* [MPA Guidelines]([https://drive.google.com/file/d/0B5RYtMgBysYHOTk1cFpBNVpDdEE/view?usp=sharing](https://drive.google.com/file/d/0B5RYtMgBysYHOTk1cFpBNVpDdEE/view?resourcekey=0-aywPbI3lhr8bVV2Kvw5XFA)) for Health Center Development 2008 ~ 2015 '_Outpatient Record Book_'
#### Haiti
* [MSPP: New Episodes of Diseases](readme/newEpisodesOfDiseases.md) report
* [MSPP: Lab](readme/MSPPlab.md) report
* [MSPP: Visits](readme/MSPPvisits.md) report
* [MSPP: Emergency](readme/MSPPemergency.md) report
* [MSPP: Family Planning](readme/MSPPfamilyPlanning.md) report
* [MSPP: Chronic Illnesses](readme/MSPPchronicIllnesses.md) report
* [MSPP: Antenatal](readme/MSPPantenatal.md) report
* [MSPP: Vaccination](readme/MSPPvaccination.md) report
* [MSPP: Child Care](readme/MSPPchildCareDiseases.md) report
