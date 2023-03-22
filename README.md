Create a RESTfull web service using SpringBoot, learn the basics of user authentication and authorization. Get to know the fundamentals of fraud detection and rule-based systems.

**Stage 1/6: Simple transaction validation**

Objectives:  
Create and run a SpringBoot application on the 28852 port;
Create the POST /api/antifraud/transaction endpoint that accepts data in the JSON format:  
`{  
    "amount": <Long>  
}`

Implement the following rules:  
Transactions with a sum of lower or equal to 200 are ALLOWED;  
Transactions with a sum of greater than 200 but lower or equal than 1500 require MANUAL_PROCESSING;  
Transactions with a sum of greater than 1500 are PROHIBITED.  
The transaction amount must be greater than 0.  
If the validation process was successful, the endpoint should respond with the status HTTP OK (200) and return the following JSON:  
`{  
    "result": "<String>"
}`  

In case of wrong data in the request, the endpoint should respond with the status HTTP Bad Request (400).

**Stage 2/6: Authentication**

Objectives

Add the Spring security to your project and configure the HTTP basic authentication;  
For storing users and passwords, add a JDBC implementation of UserDetailsService with an postgresql database. Usernames must be case-insensitive;  
Add the POST /api/auth/user endpoint. In this stage, It must be available to unauthorized users for registration and accept data in the JSON format:  
`{
    "name": "<String value, not empty>",
    "username": "<String value, not empty>",
    "password": "<String value, not empty>"
}`  

If a user has been successfully added, the endpoint must respond with the HTTP CREATED status (201) and the following body:  
`{
    "id": <Long value, not empty>,
    "name": "<String value, not empty>",
    "username": "<String value, not empty>"
}`  

If an attempt to register an existing user was a failure, the endpoint must respond with the HTTP CONFLICT status (409);  
If a request contains wrong data, the endpoint must respond with the BAD REQUEST status (400);  
Add the GET /api/auth/list endpoint. It must be available to all authorized users;  
The endpoint must respond with the HTTP OK status (200) and the body with an array of objects representing the users sorted by ID in ascending order. Return an empty JSON array if there's no information:  
`[
  {
    "id": <user1 id>,
    "name": "<user1 name>",
    "username": "<user1 username>"
  },
...
  {
    "id": <userN id>,
    "name": "<userN name>",
    "username": "<userN username>"
  }
]`  

Add the DELETE /api/auth/user/{username} endpoint, where {username} specifies the user that should be deleted. The endpoint must be available to all authorized users. The endpoint must delete the user and respond with the HTTP OK status (200) and the following body:  
`{
    "username": "<username>",
    "status": "Deleted successfully!"
}`  

If a user is not found, respond with the HTTP Not Found status (404);  
Change the POST /api/antifraud/transaction endpoint; it must be available only to all authorized users.  

**Stage 3/6: Authorization**

Objectives  

Add authorization to the service and implement the role model shown in the tale above. The first registered user should receive the ADMINISTRATOR role; the rest — MERCHANT. In case of authorization violation, respond with the HTTP Forbidden status (403). Mind that only one role can be assigned to a user;  
All users, except ADMINISTRATOR, must be locked immediately after registration; only ADMINISTRATOR can unlock users;  
Change the response for the POST /api/auth/user endpoint. It should respond with the HTTP Created status (201) and the body with the JSON object containing the information about a user. Add the role field in the response:  
`{
"id": <Long value, not empty>,
"name": "<String value, not empty>",
"username": "<String value, not empty>",
"role": "<String value, not empty>"
}`
  
Change the response for the GET /api/auth/list endpoint. Add the role field in the response:  
`[
    {
        "id": <user1 id>,
        "name": "<user1 name>",
        "username": "<user1 username>",
        "role": "<user1 role>"
    },
...
    {
        "id": <userN id>,
        "name": "<userN name>",
        "username": "<userN username>",
        "role": "<userN role>"
    }
]`  

Add the PUT /api/auth/role endpoint that changes user roles. It must accept the following JSON body:  
`{
    "username": "<String value, not empty>",
    "role": "<String value, not empty>"
}`
  
If successful, respond with the HTTP OK status (200) and the body like this:  

`{
    "id": <Long value, not empty>,
    "name": "<String value, not empty>",
    "username": "<String value, not empty>",
    "role": "<String value, not empty>"
}`
  
If a user is not found, respond with the HTTP Not Found status (404);  
If a role is not SUPPORT or MERCHANT, respond with HTTP Bad Request status (400);  
If you want to assign a role that has been already provided to a user, respond with the HTTP Conflict status (409);  
Add the PUT /api/auth/access endpoint that locks/unlocks users. It accepts the following JSON body:  
`{
    "username": "<String value, not empty>",
    "operation": "<[LOCK, UNLOCK]>"  // determines whether the user will be activated or deactivated
}`
  
If successful, respond with the HTTP OK status (200) and the following body:  

`{
    "status": "User <username> <[locked, unlocked]>!"
}`
  
For safety reasons, ADMINISTRATOR cannot be blocked. In this case, respond with the HTTP Bad Request status (400);  
If a user is not found, the endpoint must respond with HTTP Not Found status (404).  


**Stage 4/6: Stolen cards & suspicious IPs**

Objectives

Add the POST /api/antifraud/suspicious-ip endpoint that saves suspicious IP addresses to the database. It must accept the following JSON body:  
`{
    "ip": "<String value, not empty>"
}`
If successful, respond with the HTTP Created status (200) and the body like this:  

`{
    "id": "<Long value, not empty>",
    "ip": "<String value, not empty>"
}`
If an IP is already in the database, respond with the HTTP Conflict status (409).  

If an IP address has the wrong format, respond with the HTTP Bad Request status (400).  

Add the DELETE /api/antifraud/suspicious-ip/{ip} endpoint that deletes IP addresses from the database. If IP addresses are deleted successfully, respond with the HTTP OK status (200) and the body like this:  
`{
    "status": "IP <ip address> successfully removed!"
}`
If an IP is not found in the database, respond with the HTTP Not Found status (404).  

If an IP address has the wrong format (not following the Description section rules), respond with the HTTP Bad Request status (400)  

Add the GET /api/antifraud/suspicious-ip endpoint that shows IP addresses in the database. Endpoint must respond with the HTTP OK status (200) and body with an array of JSON objects representing IP address sorted by ID in ascending order (or an empty array if the database is empty):  
`[
    {
        "id": 1,
        "ip": "192.168.1.1"
    },
...
    {
        "id": 100,
        "ip": "192.168.1.254"
    }
]`
or  

`[]`  
Add the POST /api/antifraud/stolencard endpoint that saves stolen card data in the database. It must accept the following JSON body:  
`{
    "number": "<String value, not empty>"
}`
If successful, respond with the HTTP Created status (200) and the following body:  

`{
    "id": "<Long value, not empty>",
    "number": "<String value, not empty>"
}`
If the card number is already in the database, respond with the HTTP Conflict status (409).  

If a card number has the wrong format, respond with the HTTP Bad Request status (400).  

Add the DELETE /api/antifraud/stolencard/{number} endpoint that deletes a card number from the database. If a card number has been deleted, respond with the HTTP OK status (200) and the body below:  
`{
    "status": "Card <number> successfully removed!"
}`
If a card number is not found in the database, respond with the HTTP Not Found status (404).  

If a card number follows the wrong format (look at the Description section), respond with HTTP Bad Request status (400).  

Add the GET /api/antifraud/stolencard endpoint that shows card numbers stored in the database. The endpoint must respond with the HTTP OK status (200) and a body with an array of JSON objects that display the card numbers sorted by ID in ascending order (or an empty array if the database is empty):  
`[
    {
        "id": 1,
        "number": "4000008449433403"
    },
...
    {
        "id": 100,
        "number": "4000009455296122"
    }
]`
Change the POST /api/antifraud/transaction, now endpoint must accept data in the following JSON format:  
`{
    "amount": <Long>,
    "ip": "<String value, not empty>",
    "number": "<String value, not empty>"
}`
Implement the following rules in addition to the previous ones:  
If an IP address is in the blacklist, the transaction is PROHIBITED;  
If a card number is in the blacklist, the transaction is PROHIBITED.  
If the validation process was successful, the endpoint should respond with the status HTTP OK (200) and return the following JSON:  
`{
    "result": <String>,
    "info": <String>
}`
If the result is ALLOWED, the info field must be set to none (a string value).  

In the case of PROHIBITED or MANUAL_PROCESSING, the info field must contain the reason for rejecting the transaction; the reason must be separated by , and sorted alphabetically. For example, amount, card-number, ip.  

If a request contains wrong data, an IP address and a card number must be validated as described in the Description section, the endpoint should respond with the status HTTP Bad Request (400).  