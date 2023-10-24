# Clojure-React App

A simple project that combines a Clojure back-end and a React front-end to create a full-stack application.

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
- [Testing and Linting](#testing-and-linting)
  - [Back-End Testing](#back-end-testing)
  - [Back-End Linting](#back-end-linting)
  - [Front-End Testing](#front-end-testing)
  - [Front-End Linting](#front-end-linting)
- [Additional Resources](#additional-resources)

## Introduction

This repository contains a full-stack application that combines a Clojure-based back-end and a React-based front-end. The application is containerized using Docker for easy development and deployment.

## Requirements

Before you begin, ensure you have the following software installed on your computer:

- [Docker](https://docs.docker.com/engine/install/)
- [docker-compose](https://docs.docker.com/compose/install/)

To clone this repository with submodules, use the following command:

```shell
git clone --recurse-submodules <repo_url> <folder>
```

## Application Stack

This project consists of the following components:

- **app**: The Clojure-based back-end application configured using [kit-clj](https://kit-clj.github.io/) with additional development enhancements. Please ensure you've enabled the submodules cloning, otherwise this folder will be empty.

- **front-end**: A front-end application built with React using a functional approach. Ensure you've enabled the submodules cloning, otherwise this folder will be empty.

- **postgres**: A shell script for setting up the database container.

- **proxy**: An Nginx configuration for creating a reverse-proxy server.

- **.env**: An environment file for configuring docker-compose values.

- **docker-compose.yml**: The docker-compose YAML file for orchestrating the containers.

## Development workflow

### Running the Application

You can start the application using Docker Compose with the following commands:

```shell
docker compose up -d # Runs the containers in the background

# or

docker compose up # Runs the containers with output displayed

# or even

docker compose up app proxy # Starts selected containers and their dependencies. Press Ctrl+C to stop only the specified containers and leave others running in the background.
```

After starting the app, access the front-end at [localhost](http://localhost). Back-end requests are made to [localhost/api/v1](http://localhost/api/v1) endpoints. YExplore the available endpoints, parameters, and methods using the Swagger interface at [localhost/api/](http://localhost/api/).

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

Now you have a Clojure REPL for interacting with the app. The REPL starts without a database connection, to initiate it run:

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

Since it might look boring having to activate those commands everytime you start a repl, there's also a shortcut for them:

```clojure
(start-repl)
;; 2023-10-16 12:28:29,527 [main] INFO  kit.config - Reading config system.edn
;; 2023-10-16 12:28:29,537 [main] INFO  kit.config - Reading config system.edn
;; #object[kit.edge.db.sql.conman$eval16250$fn__16252$fn__16254 0x483f0877 "kit.edge.db.sql.conman$eval16250$fn__16252$fn__16254@483f0877"]

```

For more information, refer to [Deps and CLI Guide](https://clojure.org/guides/deps_and_cli) for REPL based development and [Integrant-REPL](https://github.com/weavejester/integrant-repl) for state/config and state/system.

### Populating the database

For development, it's a good idea to populate your database with sample data. To do this, access the back-end REPL and run:

```clojure
(seeds)
;; Seeds completed!

```

OBS\*: This function is only accessible on **dev** repl.

### Accessing the Front-End Container

To access the front-end container for running tests and linting, use the following command:

```bash
docker compose exec frontend /bin/sh
```

From there, you can run commands specific to the front-end.

## Production workflow

(Instructions for deploying the app will be provided in a future update.)

## Testing and Linting

### Back-end Testing

To test the application's back-end, access the app container and run the REPL in the test alias:

```bash
docker compose exec app /bin/sh
# /app#

clojure -X:test
```

### Back-End Linting

For linting the back-end, use the following command in the app terminal:

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
