# Service Declaration API
Interface for the Service Provider and the Client. The function of the API is listing, registration and invalidating of Service Declarations

Machine interface, in this version of the protocol runs over X-Road.

The Service Provider uses the API to declare the Protected Services that it can offer, with all the necessary descriptions that help the Data Subject to understand the potential impact of the Consent.

The Consent Service has the right to not publish the submitted new declarations immediately but to apply some check procedure before publishing.

The Client uses the API to find out the details of existing Service Declarations to be able to use (to refer to) these in its own Purpose Declarations.

All the parties with access (via X-Road) to the Consent Service and this API can see all the Service Declarations. All the parties with access can submit Service Declarations and thus become Service Providers in the sense of this protocol once the Service Declaration is published. Only the Service Provider itself can invalidate a Service Declaration.

Using X-Road provides encrypted transport, mutual authentication of the parties and provable message log that helps to show from where the declaration actually came. Alternative implementations of the API have to take care of these properties by themselves.

The API provides three endpoints:
* /api/listServiceDeclarations - endpoint for listing all service declarations
* /api/addServiceDeclaration - endpoint for adding a new service declaration
* /api/updateServiceDeclarationValidUntil - endpoint for updating a service declaration