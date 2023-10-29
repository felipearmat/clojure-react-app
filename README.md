# Flexi Stack Clojure-React

## Introduction

Welcome to the Flexi Stack Clojure-React repository, a versatile and pluggable full-stack application that fuses the power of Clojure on the back-end with the dynamic capabilities of React on the front-end. This project is designed to provide you with an elegant and efficient development and deployment environment, simplifying the management of the entire application stack.

## Table of Contents

- [Introduction](#introduction)
- [Requirements](#requirements)
- [Application Stack](#application-stack)
- [Development Workflow](#development-workflow)
  - [Running the Application](#running-the-application)
  - [Accessing the Back-End REPL](#accessing-the-back-end-repl)
  - [Populating the Database](#populating-the-database)
  - [Accessing the Front-End Container](#accessing-the-front-end-container)
- [Production Workflow](#production-workflow)
  - [Accessing the Back-End REPL in Production](#accessing-the-back-end-repl-in-production)
- [Testing and Linting](#testing-and-linting)
  - [Back-End Testing](#back-end-testing)
  - [Back-End Linting](#back-end-linting)
  - [Front-End Testing](#front-end-testing)
  - [Front-End Linting](#front-end-linting)
- [Additional Resources](#additional-resources)

## Requirements

Before you dive into this exciting project, ensure that your computer is equipped with the following software:

- [Docker](https://docs.docker.com/engine/install/)
- [docker-compose](https://docs.docker.com/compose/install/)

To clone this repository, along with its submodules, use the following command:

```shell
git clone --recurse-submodules <repo_url> <folder>
```

## Application Stack

This project is composed of several key components:

- **app**: A robust Clojure-based back-end application meticulously configured using [kit-clj](https://kit-clj.github.io/) enhanced with additional development utilities. If submodule cloning is enabled, this folder should contain the back-end code.

- **front-end**: A well-structured front-end application built with React, adhering to a functional paradigm. If submodule cloning is enabled, this folder should contain the front-end code.

- **postgres**: A useful shell script for setting up the database container.

- **proxy**: An Nginx configuration that facilitates the creation of a reverse proxy server.

- **.env**: An environment file that allows you to configure Docker Compose values.

- **docker-compose.yml**: The central Docker Compose YAML file that orchestrates the various containers.

## Development workflow

### Running the Application

To get the application up and running, use Docker Compose with the following commands:

```shell
docker compose up -d # Launches the containers in the background

# or

docker compose up # Starts the containers with output displayed

# or even

docker compose up app proxy # Kicks off selected containers and their dependencies. Press Ctrl+C to halt only the specified containers, leaving others to run in the background.
```

After starting the application, you can access the front-end at [localhost](http://localhost). For back-end requests, the [localhost/api/v1](http://localhost/api/v1) endpoints are at your service. Explore the available endpoints, parameters, and methods using the Swagger interface at [localhost/api/](http://localhost/api/).

### Accessing the Back-End REPL

To access a Clojure REPL for the back-end, follow these steps:

1. Access the app container's shell:

```bash
docker compose exec app /bin/sh
```

2. Start a REPL:

```sh
clojure -M:dev
# Clojure 1.11.1
# user=>
```

Now you have a Clojure REPL for interacting with the app. The REPL starts without a database connection. To initiate it, run:

```clojure
(dev-prep!)
;; #object[user$dev_prep_BANG_$fn__39701 0x40ba4a5d "user$dev_prep_BANG_$fn__39701@40ba4a5d"]

(prep)
;; 2023-10-18 11:50:11,368 [main] INFO  kit.config - Reading config system.edn
;; :prepped

(use-system :db.sql/connection)
;; 2023-10-18 11:50:17,122 [main] INFO  kit.config - Reading config system.edn
;; #object[com.zaxxer.hikari.HikariDataSource 0x7d6548b4 "HikariDataSource (HikariPool-1)"]
```

To simplify these commands, you can use the following shortcut:

```clojure
(start-repl)
;; 2023-10-16 12:28:29,527 [main] INFO  kit.config - Reading config system.edn
;; 2023-10-16 12:28:29,537 [main] INFO  kit.config - Reading config system.edn
;; #object[kit.edge.db.sql.conman$eval16250$fn__16252$fn__16254 0x483f0877 "kit.edge.db.sql.conman$eval16250$fn__16252$fn__16254@483f0877"]

```

For more information, refer to the [Deps and CLI Guide](https://clojure.org/guides/deps_and_cli) for REPL-based development and [Integrant-REPL](https://github.com/weavejester/integrant-repl) for state/config and state/system.

### Populating the database

For development purposes, it's a good practice to populate your database with sample data. To do this, access the back-end REPL and run:

```clojure
(seeds)
;; Seeds completed!

```

Please note that this function is only accessible in the **development** REPL.

### Accessing the Front-End Container

To access the front-end container for running tests and linting, use the following command:

```bash
docker compose exec frontend /bin/sh
```

From there, you can run commands specific to the front-end.

## Production workflow

This project includes a docker-compose.prod.yml file that can be used to run the stack in a production environment. To achieve this, you'll need a server running [Apache](https://httpd.apache.org/) or [NGINX](https://www.nginx.com/) that is configured to work as a [reverse proxy](https://en.wikipedia.org/wiki/Reverse_proxy) so you can connect directly to your containers. The setup and configuration for any of these servers are beyond the scope of this README.

Once you've downloaded the repository to your server, create a new .env file (e.g., .env.prod) with the configuration specific to your production environment. In this file, set any variables used in the docker-compose.prod.yml configuration, like this:

```raw
DOCKER_ENV="true"

SUBNET="172.16.238.0/24"
FRONTEND_ADDRESS="172.16.238.5"
BACKEND_ADDRESS="172.16.238.6"
FRONTEND_PORT=8080
BACKEND_PORT=3000
FRONTEND_HOST=${FRONTEND_ADDRESS}:${FRONTEND_PORT}
BACKEND_HOST=${FRONTEND_ADDRESS}:${BACKEND_PORT}/api

DB_HOST="postgres"
DB_PORT=5432
DB_TABLE="production"
DB_USER="production"
DB_PASSWORD="production_password"

COOKIE_SECRET="mycookiesecrete!"
SECRET_KEY: "16charstring****"
```

You can then start the containers using the following command:

```shell
docker compose -f docker-compose.prod.yml --env-file .env.prod up -d
```

Once the containers are started, you can access your configured server to verify that everything is working as expected.

### Accessing the Back-End REPL in Production

To access the back-end REPL in a production environment, you'll need a secure connection to your server. Once you have a secure connection (e.g., through [SSH](https://www.digitalocean.com/community/tutorials/how-to-use-ssh-to-connect-to-a-remote-server)), you can access the container as follows:

```shell
docker compose -f docker-compose.prod.yml --env-file .env.prod exec app /bin/sh
```

Once you're inside the container's terminal, you can use telnet to connect into the REPL service:

```shell
telnet 0.0.0.0 7200
;; user=>
```

This will give you access to the backend REPL in a production environment.

## Testing and Linting

### Back-end Testing

To test the application's back-end, access the app container and run the REPL in the test alias:

```bash
docker compose exec app /bin/sh
# /app#

clojure -X:test
```

### Back-End Linting

For back-end linting, use the following command in the app terminal:

```clojure
clj-kondo --lint .
;; linting took 214ms, errors: 0, warnings: 0

```

### Front-end Testing

To run front-end tests, follow these steps:

1. [Access the front-end container](#accessing-the-front-end-container)
2. Run the test command:

```bash
npm test
# Test Suites: 0 failed, 7 passed, 7 total
# Tests:       0 failed, 53 passed, 53 total
# Snapshots:   0 total
# Time:        12.117 s
```

### Front-end Linting

Linting for the front-end is performed during each build in development environments. To run linting manually, use the following command:

```bash
npm run lint
# > front-end@0.1.0 lint
# > eslint ./src
```

## Additional Resources

Here are some additional resources and information to help you get the most out of this project:

1. **Official Documentation**:

   - [Clojure Documentation](https://clojure.org/)
   - [React Documentation](https://reactjs.org/)
   - [Docker Documentation](https://docs.docker.com/)
   - [PostgreSQL Documentation](https://www.postgresql.org/docs/)

2. **Tutorials and Learning Resources**:
   - [Learn Clojure on ClojureDocs](https://clojuredocs.org/community/tutorials)
   - [Clojure for the brave and true](https://www.braveclojure.com/foreword/)
   - [Docker Getting Started Guide](https://docs.docker.com/get-started/)
   - [PostgreSQL Tutorial](https://www.postgresqltutorial.com/)
