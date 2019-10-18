# userauth
Microservice to manage users.

This microservice is developed using Java, Spring Boot, Maven, Lombok, Spring JPA, H2 in-memory database and Spring security crypto library.

## Create Jar

    mvn clean install

## Run the app

    mvn spring-boot:run

# REST API

The REST API to the service is described below.

## Register a new account

### Request

`POST /user`

    curl -d '{"email":"cegvinoth@gmail.com", "password": "test"}' -H "Content-Type: application/json" -X POST http://localhost:8080/user

### Response

    {
    "id": 1,
    "dateCreated": "2019-10-18T02:01:50.977256+02:00",
    "email": "cegvinoth@gmail.com",
    "password": "$2a$10$dr73tm1TXRcceus9nKwYK.qw.7r8oGE26mWwh7wNi0o6SJsbkRH8a"
    }

Password is saved using Bcrypt hashing. Hence passwords stored in database are well protected.
## Login to Account

### Request

`POST /login`
 
    curl -d '{"email":"cegvinoth@gmail.com", "password": "test"}' -H "Content-Type: application/json" -X POST http://localhost:8080/login

### Response

    {
    "id": 1,
    "user": {
        "id": 1,
        "dateCreated": "2019-10-18T02:01:50.977256+02:00",
        "email": "cegvinoth@gmail.com",
        "password": "$2a$10$dr73tm1TXRcceus9nKwYK.qw.7r8oGE26mWwh7wNi0o6SJsbkRH8a"
    },
    "authtoken": "2d8063ee-694d-4a25-a999-e80eb09df245",
    "dateCreated": "2019-10-18T02:02:55.629664+02:00"
    }

The above response returns a unique Authtoken. All protected API endpoints can use this token as a Bearer token header for authentication.

## Forgot Password, request reset.

### Request

`POST /forgotPassword`

    curl -d '{"email":"cegvinoth@gmail.com"}' -H "Content-Type: application/json" -X POST http://localhost:8080/forgotPassword

### Response

    "http://localhost:8080/resetPassword/28255528-3e47-4cf1-96c0-cf791a179f1d"

After users trigger a forgot password request (via `GET /forgotPassword`), reset URL is generally sent via email. For this task, API just returns the reset URL.

## Verify Reset URL

If users click the reset URL, it can be verified using this endpoint.

### Request

`GET /resetPassword/28255528-3e47-4cf1-96c0-cf791a179f1d`

    curl -H 'Accept: application/json' -X GET http://localhost:8080/resetPassword/28255528-3e47-4cf1-96c0-cf791a179f1d

### Response

    "Valid password reset token"

## Set a new password

### Request

`POST /resetPassword/28255528-3e47-4cf1-96c0-cf791a179f1d`

    curl -d '{"newpassword":"newtest"}' -H "Content-Type: application/json" -X POST http://localhost:8080/resetPassword/28255528-3e47-4cf1-96c0-cf791a179f1d

### Response

    "Password reset successfully"

After resetting, new password can be used for further logins.

