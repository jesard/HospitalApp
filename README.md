# Prototype of REST Server for hospital [Spring]
The app can be used as backend part of hospital website.
It accepts a JSON in specific format, make a record to MySQL DB and answers with a JSON.
  
## Request & response examples

**Patient registration:**
POST /api/patients

Request:
{
    "firstName": "John", 
    "lastName": "Smith", 
    "patronymic": "Murray",  // not required
    "email": "example@example.com", 
    "address": "123 Durana st", 
    "phone": "+79995461444", 
    "login": "Qwerty", 
    "password": "asdfg" 
}

Response:
cookie: JAVASESSIONID
{
       "id": 1, 
       "firstName": "John", 
       "lastName": "Smith", 
       "patronymic": "Murray", 
       "email": "example@example.com", 
       "address": "123 Durana st", 
       "phone": "79995461444"
  }
___

**Registration with existing login:**
POST /api/patients

Request:
{
    "firstName": "John", 
    "lastName": "Smith", 
    "patronymic": "Murray",  // not required
    "email": "example@example.com", 
    "address": "123 Durana st", 
    "phone": "+79995461444", 
    "login": "Qwerty", 
    "password": "asdfg" 
}

Response:
{
     "errors" : [
        {
            "errorCode": "LOGIN_ALREADY_EXISTS" ,
            "field": "login", 
            "message": "User Qwerty already exists" 
        }
  ]
}

___
Mikhail Anufriev 2020

