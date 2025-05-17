## Local development:
- override `application.yml` with:


```properties
#for db
url: jdbc:postgresql://localhost:5432/garagify
username: postgres
password: postgres
#for security
secret: 3IUIRg1kXZF9DrfhuBKU0+E8IHL+3yVcGVd0aE4z5y8=
clientId: 1234567890-abcde12345.apps.googleusercontent.com
```


## Enviromental variables example:

```env
SERVICE_DB_URL=jdbc:postgresql://postgresdb:5432/garagify
SERVICE_DB_LOGIN=postgres
SERVICE_DB_PASSWORD=postgres


JWT_SECRET=3IUIRg1kXZF9DrfhuBKU0+E8IHL+3yVcGVd0aE4z5y8=
GOOGLE_CLIENT_ID=1234567890-abcde12345.apps.googleusercontent.com
```