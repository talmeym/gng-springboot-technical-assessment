Notes.

- Have excluded (to save time) an e2e test that starts the application via docker compose (using WireMock for IP API) and tests all interactions including those with PostGreSQL 
- Have not fully utilised all appropriate Spring Boot features. Some refamiliarization has been involved
- Have almost certainly done too much, or not enough, in some areas. Summiting as-is as conscious of time taken and spec stated completeness not neccessarily most important
- Read somewhere that all GCP IP addresses resolve to a US country code, so it might well be impossible to fail IP validation on invalid isp for GCP, if countrty code checked first
