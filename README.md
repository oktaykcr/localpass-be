# Localpass Backend

### Description
**Localpass** is a web application that you can manage your credentials in a secure solution. 
Every fields of password is encrypted.
The project consist of two side. The backend side is written with Java Spring Boot, the [frontend](https://github.com/oktaykcr/localpass-fe) side is written with vueJs.

> Technologies & Frameworks
* Backend
    * Spring Boot: 2.1.3.RELEASE
        * *data-jpa*
        * *web*
        * *security*
        * *test*
        * *jsonwebtoken*
        * *postgreSQL*
        * *docker*
        * *maven-surefire*
        
> Project Technical Description

Project uses JPA for db operations(CRUD). 
There is an authentication and authorization system on the backend server using spring boot security. It uses custom filters.
There is a cors configuration for set allowed origins, methods and exposed headers.
The project has custom exceptions and exception handler for any api operations.
Lastly, Docker and CircleCI integrated into the be project.

> REST Routes

**Backend:**

| Name           | Path                    | HTTP Verb    | Purpose                                        |
|----------------|-------------------------|--------------|------------------------------------------------|
| register       | /api/v1/user/register   | POST         | register new user                              |
| login          | /api/v1/user/login      | POST         | login with username and password and get token |
| listPasswords  | /api/v1/password/list   | GET          | list all passwords                             |
| savePassword   | /api/v1/password/save   | POST         | save new password     	                       |
| updatePassword | /api/v1/password/update | PUT          | update specified password                      |
| deletePassword | /api/v1/password/delete | DELETE       | delete specified password                      |


### Installation

> Docker

1. At project root directory, execute `docker-compose build` to create *postgreSQL* and *backend* images for docker.
2. To start containers, execute `docker-compose up`
3. The container will start after these commands and you can reach to be server from ``http://locahost:8082``. You can change port from **Dockerfile** (at src/main/docker/Dockerfile).

> Local

1. Install dependencies with `mvn clean install`
2. Change postgreSQL configurations according to your server. Default configuration is local postgre server. (src/main/resources/application.properties)
3. **[OPTIONAL]** Run local postgre server on docker : `docker run -p 5432:5432 --name postgre_local -e POSTGRES_PASSWORD=password -e POSTGRES_USER=root -d postgres`
4. Run backend server with `mvn spring-boot:run` command or your existing IDE.
