# Service Declaration API
Interface for the Service Provider and the Client. The function of the API is listing, registration and invalidating of Service Declarations

Machine interface, in this version of the protocol runs over X-Road.

The Service Provider uses the API to declare the Protected Services that it can offer, with all the necessary descriptions that help the Data Subject to understand the potential impact of the Consent.

The Consent Service has the right to not publish the submitted new declarations immediately but to apply some check procedure before publishing.

The Client uses the API to find out the details of existing Service Declarations to be able to use (to refer to) these in its own Purpose Declarations.

All the parties with access (via X-Road) to the Consent Service and this API can see all the Service Declarations. All the parties with access can submit Service Declarations and thus become Service Providers in the sense of this protocol once the Service Declaration is published. Only the Service Provider itself can invalidate a Service Declaration.

Using X-Road provides encrypted transport, mutual authentication of the parties and provable message log that helps to show from where the declaration actually came. Alternative implementations of the API have to take care of these properties by themselves.

The API provides five endpoints:
* /api/auth - endpoint for authentication (in order to use other endpoints, this must be used first)
* /api/heartbeat - endpoint for indicating system state
* /api/listServiceDeclarations - endpoint for listing all service declarations
* /api/addServiceDeclaration - endpoint for adding a new service declaration
* /api/updateServiceDeclarationValidUntil - endpoint for updating a service declaration

Installing database with Docker
-----
1. install docker support for your OS
2. for start
       
       docker-compose build
       docker-compose up

3. for remove all images
       
       docker-compose rm
       
4. for remove all images and volumes - needed if want to recreate database content for example
       
       docker-compose rm -v
       
5. for reinitialize DB clean data

       docker-compose rm postgres
       docker-compose up

DB
--
* host: localhost
* port: 15434
* db: consent
* schema: service_declaration_api
  - main user:
    * user: service_declaration (all privileges within the database)
    * password: service_declaration
  - another user:
    * user: service_declaration_app (privileges only within service_declaration_api schema)
    * password: service_declaration_app
  
Running the api
--
- ./gradlew bootRun


Testing the api
--
1. Testing with CURL

1.1 Getting the heartbeat of the system

       curl -X GET "http://localhost:8082/api/heartbeat" -H "accept: application/json"

1.1.1 Server response

        {
          "databaseUp": true,
          "appName": "ServiceDeclarationApi",
          "appVersion": "0.2.1-SNAPSHOT",
          "systemTime": "2020-04-16T09:51:36.748311"
        }

1.2.1 Requesting a JWT token

       curl -X POST "http://localhost:8082/api/auth" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"username\":\"consent\",\"password\":\"password\"}"

1.2.1.1 Server response

       {
         "jwttoken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDM3NDQwLCJpYXQiOjE1ODcwMTk0NDB9.VyK6i65JnwZ_RCDncuaoZqdf6lqlxikYTm52vGlza0R8W2sjqbHGKHqxl1tPmGyJhCJkzkMd0klRPbJm07dbYw"
       }

1.2.2 Requesting a JWT token with invalid credentials

       curl -X POST "http://localhost:8082/api/auth" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"password\"}"

1.2.2.1 Server response

        {
          "message": "Server Error",
          "details": [
            "INVALID_CREDENTIALS"
          ]
        }      

1.3.1 Adding a service declaration without a JWT token in the header

       curl -X POST "http://localhost:8082/api/addServiceDeclaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"AnotherServiceProvider\",\"serviceDeclarationIdentifier\":\"AnotherDeclaration\",\"serviceDeclarationName\":\"declaration999\",\"serviceDeclarationDescription\":\"decription456\",\"technicalDescription\":\"technical description456\",\"validUntil\":\"2020-04-26T06:05:18.370Z\",\"consentMaxDurationSeconds\":10,\"maxCacheSeconds\":10,\"needSignature\":true}"
       
1.3.1.1 Server response

        {
          "timestamp":"2020-04-16T09:04:52.606+0000",
          "status":401,
          "error":"Unauthorized",
          "message":"Jwt authentication failed",
          "path":"/api/addServiceDeclaration"
        }

1.3.2 Adding a service declaration

       curl -X POST "http://localhost:8082/api/addServiceDeclaration" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDM3NDQwLCJpYXQiOjE1ODcwMTk0NDB9.VyK6i65JnwZ_RCDncuaoZqdf6lqlxikYTm52vGlza0R8W2sjqbHGKHqxl1tPmGyJhCJkzkMd0klRPbJm07dbYw" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"serviceDeclarationName\":\"declaration456\",\"serviceDeclarationDescription\":\"decription456\",\"technicalDescription\":\"technical description456\",\"validUntil\":\"2020-04-26T06:05:18.370Z\",\"consentMaxDurationSeconds\":10,\"maxCacheSeconds\":10,\"needSignature\":true}"

1.3.2.1 Server response

        {
          "response": "OK"
        }
      
1.3.3 Adding a service declaration which already exists

       curl -X POST "http://localhost:8082/api/addServiceDeclaration" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDM3NDQwLCJpYXQiOjE1ODcwMTk0NDB9.VyK6i65JnwZ_RCDncuaoZqdf6lqlxikYTm52vGlza0R8W2sjqbHGKHqxl1tPmGyJhCJkzkMd0klRPbJm07dbYw" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"serviceDeclarationName\":\"declaration456\",\"serviceDeclarationDescription\":\"decription456\",\"technicalDescription\":\"technical description456\",\"validUntil\":\"2020-04-26T06:05:18.370Z\",\"consentMaxDurationSeconds\":10,\"maxCacheSeconds\":10,\"needSignature\":true}"

1.3.3.1 Server response

        {
           "message": "Duplicate declaration",
           "details": [
             "There already exists a declaration with identifier: MyDeclaration"
           ]
        }
         
1.3.4 Adding a service declaration which has its ValidUntil parameter in the past

    curl -X POST "http://localhost:8082/api/addServiceDeclaration" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration2\",\"serviceDeclarationName\":\"declaration2\",\"serviceDeclarationDescription\":\"another declaration\",\"technicalDescription\":\"some technical info\",\"validUntil\":\"2020-04-16T08:40:37.928Z\",\"consentMaxDurationSeconds\":60,\"maxCacheSeconds\":60,\"needSignature\":true}"

1.3.4.1 Server response

        {
           "message": "Invalid request",
           "details": [
             "The validUntil of this declaration request is in the past"
           ]
        }         

1.3.5 Adding a service declaration which maxCacheValue parameter has negative value

       curl -X POST "http://localhost:8082/api/addServiceDeclaration" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration2\",\"serviceDeclarationName\":\"declaration2\",\"serviceDeclarationDescription\":\"another declaration\",\"technicalDescription\":\"some technical info\",\"validUntil\":\"2020-04-26T08:40:37.928Z\",\"consentMaxDurationSeconds\":60,\"maxCacheSeconds\":-60,\"needSignature\":true}"
       
1.3.5.1 Server response

        {
           "message": "Invalid request",
           "details": [
             "MaxCacheSeconds of service declaration must be positive"
           ]
        }       

1.4.1 Listing service declarations for a given service provider without a JWT token in the header

       curl -X POST "http://localhost:8082/api/listServiceDeclarations" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyServiceDeclaration\",\"name\":\"a name\",\"description\":\"a description\",\"technicalDescription\":\"some technical description\",\"validAt\":\"2020-04-26T08:47:38.292Z\",\"details\":true}"
       
1.4.1.1 Server response

        {
          "timestamp":"2020-04-16T09:11:14.972+0000",
          "status":401,
          "error":"Unauthorized",
          "message":"Jwt authentication failed",
          "path":"/api/listServiceDeclarations"
        }
        
1.4.2 Listing service declarations for a given service provider

        curl -X POST "http://localhost:8082/api/listServiceDeclarations" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyServiceDeclaration\",\"name\":\"a name\",\"description\":\"a description\",\"technicalDescription\":\"some technical description\",\"validAt\":\"2020-04-26T08:47:38.292Z\",\"details\":true}"
       
1.4.2.1 Server response

        {
           "declarations": [
             {
               "id": 61,
               "identifier": "MyDeclaration",
               "name": "declaration456",
               "description": "decription456",
               "valid": true,
               "provider": {
                 "id": 68,
                 "identifier": "MyServiceProvider"
               }
             }
           ],
           "serviceProviderIdentifier": "MyServiceProvider",
           "serviceDeclarationIdentifier": "MyServiceDeclaration"
        }

1.4.3 Listing service declarations with a too broad query (not enough parameters)

       curl -X POST "http://localhost:8082/api/listServiceDeclarations" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyServiceDeclaration\",\"name\":\"\",\"description\":\"\",\"technicalDescription\":\"\",\"validAt\":\"2020-04-26T08:47:38.292Z\",\"details\":true}"
       
1.4.3.1 Server response

        {
           "message": "Too broad query",
           "details": [
             "Consent Service refuses to fulfill the request/too many responses, please add more specific conditions and try again"
           ]
        } 
      
1.4.4 Listing service declarations with a service provider which does not exist

       curl -X POST "http://localhost:8082/api/listServiceDeclarations" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider2\",\"serviceDeclarationIdentifier\":\"MyServiceDeclaration\",\"name\":\"a name\",\"description\":\"a description\",\"technicalDescription\":\"some technical description\",\"validAt\":\"2020-04-26T08:47:38.292Z\",\"details\":true}"
       
1.4.4.1 Server response

        {
           "message": "Invalid request",
           "details": [
             "The provider with identifier MyServiceProvider2 does not exist"
           ]
        }

1.5.1 Updating the ValidUntil of a given service declaration without a JWT token in the header

       curl -X PUT "http://localhost:8082/api/updateServiceDeclarationValidUntil" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"validUntil\":\"2020-04-26T08:56:13.745Z\"}"
       
1.5.1.1 Server response

        {
          "timestamp":"2020-04-16T09:13:51.954+0000",
          "status":401,
          "error":"Unauthorized",
          "message":"Jwt authentication failed",
          "path":"/api/updateServiceDeclarationValidUntil"
        }

1.5.2 Updating the ValidUntil of a given service declaration

        curl -X PUT "http://localhost:8082/api/updateServiceDeclarationValidUntil" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"validUntil\":\"2022-04-26T08:56:13.745Z\"}"
        
1.5.2.1 Server response

        {
           "response": "OK"
        }

1.5.3 Updating the ValidUntil for a service declaration which does not exist

        curl -X PUT "http://localhost:8082/api/updateServiceDeclarationValidUntil" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration2\",\"validUntil\":\"2022-04-26T08:56:13.745Z\"}"
        
1.5.3.1 Server response

        {
           "message": "Invalid request",
           "details": [
             "The declaration with identifier MyDeclaration2 does not exist"
           ]
        }  
        
1.5.4 Updating the ValidUntil for a given service declaration with the parameter ValidUntil in the past

        curl -X PUT "http://localhost:8082/api/updateServiceDeclarationValidUntil" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"validUntil\":\"2020-04-16T08:56:13.745Z\"}"
        
1.5.4.1 Server response

        {
           "message": "Invalid request",
           "details": [
             "The validUntil of this declaration request is in the past"
           ]
        } 
        
2. Testing with Swagger
      
  * Open url http://localhost:8082/api-docs in the browser
  
  - Swagger does not allow performing any requests without the JWT token in the header
  
  ![Service Declaration API Swagger](swagger.png)
  
2.1 Getting the heartbeat of the system

  ![HeartBeat](swagger_heartbeat.png)

2.2.1 Requesting a JWT token
    
  ![JWT](swagger_jwt.png)
  
2.2.2 Requesting a JWT token with invalid credentials
     
  ![Invalid Credentials](swagger_invalid_credentials.png)

2.3.1 Adding a service declaration

  ![Add declaration](swagger_add_declaration.png)
      
2.3.2 Adding a service declaration which already exists

  ![Add declaration duplicate](swagger_add_declaration_duplicate.png)
  
2.3.3 Adding a service declaration which has its ValidUntil parameter in the past

  ![Add declaration ValidUntil in past](swagger_add_declaration_validuntil_past.png)       

2.3.4 Adding a service declaration which maxCacheValue parameter has negative value
    
  ![Add declaration MacCache negative](swagger_add_declaration_maxcache_negative.png) 
       
2.4.1 Listing service declarations for a given service provider

  ![List declarations](swagger_list_declarations.png) 

2.4.2 Listing service declarations with a too broad query (not enough parameters)

  ![List declarations missing provider](swagger_list_declarations_provider_missing.png) 
        
2.4.3 Listing service declarations with a service provider which does not exist

  ![List declarations too borad query](swagger_list_declarations_too_broad.png) 
  
2.5.1 Updating the ValidUntil of a given service declaration

  ![Update declarations](swagger_update_declaration.png) 
  
2.5.2 Updating the ValidUntil for a service declaration which does not exist  

  ![Update declarations missing declaration](swagger_update_declaration_not_exists.png) 
          
2.5.3 Updating the ValidUntil for a given service declaration with the parameter ValidUntil in the past

  ![Update declarations ValidUntil in past](swagger_update_declaration_validuntil_past.png) 