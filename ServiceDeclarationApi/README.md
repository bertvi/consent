# Table of contents
1. [Introduction](#introduction)
2. [Installing db with docker](#db)
3. [Running the api](#running_api)
4. [Testing the api](#testing_api)
   1. [Testing with CURL](#testing_curl)
   2. [Testing with SWAGGER](#testing_swagger)


# 1 Introduction <a name="introduction"></a>
Servide Declaration API is an interface for the Service Provider and the Client. The function of the API is listing, registration and invalidating of Service Declarations

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

# 2 Installing db with docker <a name="db"></a>

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

## DB
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
  
# 3 Running the api <a name="running_api"></a>

    ./gradlew bootRun


# 4 Testing the api <a name="testing_api"></a>

## 4.1 Testing with CURL <a name="testing_curl"></a>

   ServiceDeclarationAPI uses TLS over HTTPS with a certificate consent.p12 (located in project resources). There are two ways to perform
   the CURL requests, either using an extra parameter "--insecure" or performing the request with an extra parameter "--cacert" following
   the name of a public certificate.
    
   Extracting the certificate from the keystore(keystore password etc is in the application.properties), using the keytool from your JRE bin folder:
    
       keytool -export -alias consent -keystore consent.p12 -rfc -file consent.crt
    
   Example of performing a CURL request with the "--insecure" parameter:
        
       curl -X GET --insecure "https://localhost:8443/api/heartbeat" -H "accept: application/json"
    
   Example of performing a CURL request with the "--cacert" parameter:
            
        curl -X GET --cacert consent.crt "https://localhost:8443/api/heartbeat" -H "accept: application/json"  
    
   In the testcase below the parameter "--insecure" has been used.     

### Getting the heartbeat of the system

       curl -X GET --insecure "https://localhost:8443/api/heartbeat" -H "accept: application/json"

### Server response

        {
          "databaseUp": true,
          "appName": "ServiceDeclarationApi",
          "appVersion": "0.2.1-SNAPSHOT",
          "systemTime": "2020-04-16T09:51:36.748311"
        }

### Requesting a JWT token

       curl -X POST --insecure "https://localhost:8443/api/auth" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"username\":\"consent\",\"password\":\"password\"}"

### Server response

       {
         "jwttoken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDM3NDQwLCJpYXQiOjE1ODcwMTk0NDB9.VyK6i65JnwZ_RCDncuaoZqdf6lqlxikYTm52vGlza0R8W2sjqbHGKHqxl1tPmGyJhCJkzkMd0klRPbJm07dbYw"
       }

### Requesting a JWT token with invalid credentials

       curl -X POST --insecure "https://localhost:8443/api/auth" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"password\"}"

### Server response

        {
          "message": "Server Error",
          "details": [
            "INVALID_CREDENTIALS"
          ]
        }      

### Adding a service declaration without a JWT token in the header

       curl -X POST --insecure "https://localhost:8443/api/addServiceDeclaration" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"AnotherServiceProvider\",\"serviceDeclarationIdentifier\":\"AnotherDeclaration\",\"serviceDeclarationName\":\"declaration999\",\"serviceDeclarationDescription\":\"decription456\",\"technicalDescription\":\"technical description456\",\"validUntil\":\"2020-04-26T06:05:18.370Z\",\"consentMaxDurationSeconds\":10,\"maxCacheSeconds\":10,\"needSignature\":true}"
       
### Server response

        {
          "timestamp":"2020-04-16T09:04:52.606+0000",
          "status":401,
          "error":"Unauthorized",
          "message":"Jwt authentication failed",
          "path":"/api/addServiceDeclaration"
        }

### Adding a service declaration

       curl -X POST --insecure "https://localhost:8443/api/addServiceDeclaration" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDM3NDQwLCJpYXQiOjE1ODcwMTk0NDB9.VyK6i65JnwZ_RCDncuaoZqdf6lqlxikYTm52vGlza0R8W2sjqbHGKHqxl1tPmGyJhCJkzkMd0klRPbJm07dbYw" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"serviceDeclarationName\":\"declaration456\",\"serviceDeclarationDescription\":\"decription456\",\"technicalDescription\":\"technical description456\",\"validUntil\":\"2020-04-26T06:05:18.370Z\",\"consentMaxDurationSeconds\":10,\"maxCacheSeconds\":10,\"needSignature\":true}"

### Server response

        {
          "response": "OK"
        }
      
### Adding a service declaration which already exists

       curl -X POST --insecure "https://localhost:8443/api/addServiceDeclaration" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDM3NDQwLCJpYXQiOjE1ODcwMTk0NDB9.VyK6i65JnwZ_RCDncuaoZqdf6lqlxikYTm52vGlza0R8W2sjqbHGKHqxl1tPmGyJhCJkzkMd0klRPbJm07dbYw" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"serviceDeclarationName\":\"declaration456\",\"serviceDeclarationDescription\":\"decription456\",\"technicalDescription\":\"technical description456\",\"validUntil\":\"2020-04-26T06:05:18.370Z\",\"consentMaxDurationSeconds\":10,\"maxCacheSeconds\":10,\"needSignature\":true}"

### Server response

        {
           "message": "Duplicate declaration",
           "details": [
             "There already exists a declaration with identifier: MyDeclaration"
           ]
        }
         
### Adding a service declaration which has its ValidUntil parameter in the past

    curl -X POST --insecure "https://localhost:8443/api/addServiceDeclaration" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration2\",\"serviceDeclarationName\":\"declaration2\",\"serviceDeclarationDescription\":\"another declaration\",\"technicalDescription\":\"some technical info\",\"validUntil\":\"2020-04-16T08:40:37.928Z\",\"consentMaxDurationSeconds\":60,\"maxCacheSeconds\":60,\"needSignature\":true}"

### Server response

        {
           "message": "Invalid request",
           "details": [
             "The validUntil of this declaration request is in the past"
           ]
        }         

### Adding a service declaration which maxCacheValue parameter has negative value

       curl -X POST --insecure "https://localhost:8443/api/addServiceDeclaration" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration2\",\"serviceDeclarationName\":\"declaration2\",\"serviceDeclarationDescription\":\"another declaration\",\"technicalDescription\":\"some technical info\",\"validUntil\":\"2020-04-26T08:40:37.928Z\",\"consentMaxDurationSeconds\":60,\"maxCacheSeconds\":-60,\"needSignature\":true}"
       
### Server response

        {
           "message": "Invalid request",
           "details": [
             "MaxCacheSeconds of service declaration must be positive"
           ]
        }       

### Listing service declarations for a given service provider without a JWT token in the header

       curl -X POST --insecure "https://localhost:8443/api/listServiceDeclarations" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyServiceDeclaration\",\"name\":\"a name\",\"description\":\"a description\",\"technicalDescription\":\"some technical description\",\"validAt\":\"2020-04-26T08:47:38.292Z\",\"details\":true}"
       
### Server response

        {
          "timestamp":"2020-04-16T09:11:14.972+0000",
          "status":401,
          "error":"Unauthorized",
          "message":"Jwt authentication failed",
          "path":"/api/listServiceDeclarations"
        }
        
### Listing service declarations for a given service provider

        curl -X POST --insecure "https://localhost:8443/api/listServiceDeclarations" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyServiceDeclaration\",\"name\":\"a name\",\"description\":\"a description\",\"technicalDescription\":\"some technical description\",\"validAt\":\"2020-04-26T08:47:38.292Z\",\"details\":true}"
       
### Server response

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

### Listing service declarations with a too broad query (not enough parameters)

       curl -X POST --insecure "https://localhost:8443/api/listServiceDeclarations" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyServiceDeclaration\",\"name\":\"\",\"description\":\"\",\"technicalDescription\":\"\",\"validAt\":\"2020-04-26T08:47:38.292Z\",\"details\":true}"
       
### Server response

        {
           "message": "Too broad query",
           "details": [
             "Consent Service refuses to fulfill the request/too many responses, please add more specific conditions and try again"
           ]
        } 
      
### Listing service declarations with a service provider which does not exist

       curl -X POST --insecure "https://localhost:8443/api/listServiceDeclarations" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider2\",\"serviceDeclarationIdentifier\":\"MyServiceDeclaration\",\"name\":\"a name\",\"description\":\"a description\",\"technicalDescription\":\"some technical description\",\"validAt\":\"2020-04-26T08:47:38.292Z\",\"details\":true}"
       
### Server response

        {
           "message": "Invalid request",
           "details": [
             "The provider with identifier MyServiceProvider2 does not exist"
           ]
        }

### Updating the ValidUntil of a given service declaration without a JWT token in the header

       curl -X PUT --insecure "https://localhost:8443/api/updateServiceDeclarationValidUntil" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"validUntil\":\"2020-04-26T08:56:13.745Z\"}"
       
### Server response

        {
          "timestamp":"2020-04-16T09:13:51.954+0000",
          "status":401,
          "error":"Unauthorized",
          "message":"Jwt authentication failed",
          "path":"/api/updateServiceDeclarationValidUntil"
        }

### Updating the ValidUntil of a given service declaration

        curl -X PUT --insecure "https://localhost:8443/api/updateServiceDeclarationValidUntil" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"validUntil\":\"2022-04-26T08:56:13.745Z\"}"
        
### Server response

        {
           "response": "OK"
        }

### Updating the ValidUntil for a service declaration which does not exist

        curl -X PUT --insecure "https://localhost:8443/api/updateServiceDeclarationValidUntil" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration2\",\"validUntil\":\"2022-04-26T08:56:13.745Z\"}"
        
### Server response

        {
           "message": "Invalid request",
           "details": [
             "The declaration with identifier MyDeclaration2 does not exist"
           ]
        }  
        
### Updating the ValidUntil for a given service declaration with the parameter ValidUntil in the past

        curl -X PUT --insecure "https://localhost:8443/api/updateServiceDeclarationValidUntil" -H "accept: application/json" -H "jwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25zZW50IiwiZXhwIjoxNTg3MDQ0NDI0LCJpYXQiOjE1ODcwMjY0MjR9.un3IPIwZwCTJCYp1PS-mE-h-CDVcKXR6z_QM7_QXSf1pcgndFnlPl-3oUk38ZgzyZMZ-UqCr_fQHGMPXkCzhoA" -H "Content-Type: application/json" -d "{\"serviceProviderIdentifier\":\"MyServiceProvider\",\"serviceDeclarationIdentifier\":\"MyDeclaration\",\"validUntil\":\"2020-04-16T08:56:13.745Z\"}"
        
### Server response

        {
           "message": "Invalid request",
           "details": [
             "The validUntil of this declaration request is in the past"
           ]
        } 
        
## 4.2 Testing with SWAGGER <a name="testing_swagger"></a>
      
  * Open url https://localhost:8443/api-docs in the browser
  
  - Swagger does not allow performing any requests without the JWT token in the header
  
  ![Service Declaration API Swagger](swagger.png)
  
### Getting the heartbeat of the system

  ![HeartBeat](swagger_heartbeat.png)

### Requesting a JWT token
    
  ![JWT](swagger_jwt.png)
  
### Requesting a JWT token with invalid credentials
     
  ![Invalid Credentials](swagger_invalid_credentials.png)

### Adding a service declaration

  ![Add declaration](swagger_add_declaration.png)
      
### Adding a service declaration which already exists

  ![Add declaration duplicate](swagger_add_declaration_duplicate.png)
  
### Adding a service declaration which has its ValidUntil parameter in the past

  ![Add declaration ValidUntil in past](swagger_add_declaration_validuntil_past.png)       

### Adding a service declaration which maxCacheValue parameter has negative value
    
  ![Add declaration MacCache negative](swagger_add_declaration_maxcache_negative.png) 
       
### Listing service declarations for a given service provider

  ![List declarations](swagger_list_declarations.png) 

### Listing service declarations with a too broad query (not enough parameters)

  ![List declarations missing provider](swagger_list_declarations_provider_missing.png) 
        
### Listing service declarations with a service provider which does not exist

  ![List declarations too borad query](swagger_list_declarations_too_broad.png) 
  
### Updating the ValidUntil of a given service declaration

  ![Update declarations](swagger_update_declaration.png) 
  
### Updating the ValidUntil for a service declaration which does not exist  

  ![Update declarations missing declaration](swagger_update_declaration_not_exists.png) 
          
### Updating the ValidUntil for a given service declaration with the parameter ValidUntil in the past

  ![Update declarations ValidUntil in past](swagger_update_declaration_validuntil_past.png) 