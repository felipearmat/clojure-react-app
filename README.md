# clojure-react-app

A simple project for a back-end Clojure and front-end React app.

## Index

- [Requirements](#requirements)
- [App Stack](#app-stack)
- [Development Workflow](#development-workflow)
  - [Running the Application](#running-the-application)
  - [Accessing the REPL](#accessing-the-repl)
- [Production Workflow](#production-workflow)
- [Testing](#testing)
  - [Backend Testing](#backend-testing)
  - [Frontend Testing](#frontend-testing)
- [Good to Know](#good-to-know)

## Requirements

First, make sure to enable the 'recurse-submodules' git tag when cloning this repository:

```shell
git clone --recurse-submodules <repo_url> <folder>
```

Then you'll need some applications and libs installed on your pc:

- [Docker](https://docs.docker.com/engine/install/)
- [docker-compose](https://docs.docker.com/compose/install/)

## App Stack

This project consists of the following components:

- **app**: A backend application written in Clojure. It's primarily configured using [kit-clj](https://kit-clj.github.io/) with some additional development enhancements. Please ensure you've enabled the submodules cloning, otherwise this folder will be empty.

- **frontend**: A frontend application built with React using a functional approach. Ensure you've enabled the submodules cloning, otherwise this folder will be empty.

- **postgres**: This folder contains a shell script that is responsible for setting up the database and executes the first time you start the postgres container.

- **proxy**: A simple Nginx configuration for creating a reverse-proxy server that makes backend and frontend act as a single application.

- **.env**: An environment file that configures the docker-compose values for the apps. You can adjust these configurations to better suit your needs.

- **docker-compose.yml**: The docker-compose YAML file for orchestrating the containers.

## Development workflow

### Running the Application

You can start the app using the following docker-compose commands:

```shell
docker compose up -d # Runs the containers in the background

# or

docker compose up # Runs the containers with output displayed

# or even

docker compose up app proxy # Starts selected containers and their dependencies. Press Ctrl+C to stop only the specified containers and leave others running in the background.
```

After starting the app, access the front-end at [localhost](http://localhost). Backend requests are made to [localhost/api/v1](http://localhost/api/v1) endpoints. You can access the Swagger interface at [localhost/api/](http://localhost/api/) to explore the available endpoints, parameters, and methods.

### Database values

You might want to create some default data for development. You can do this by running the (seeds) command on REPL on user namespace. This function will create some data for easy of development.

### Accessing the REPL

To access a REPL interface, follow these steps:

1. Access the app container's shell:

```bash
docker compose exec app /bin/sh
```

2. Start a REPL by running the following command:

```sh
clojure -M:dev
# Clojure 1.11.1
# user=>
```

Now you have a Clojure REPL for interacting with the app. You'll need to stablish database connection so you can make some queries:

```clojure
(prep)
;; 2023-10-13 00:00:01,000 [main] INFO  kit.config - Reading config system.edn
;; :prepped

(use-system :db.sql/query-fn)
;; #object[kit.edge.db.sql.conman$eval12560$fn__12562$fn__12564 0x1a173e0f "kit.edge.db.sql.conman$eval12560$fn__12562$fn__12564@1a173e0f"]
```

Since it might look boring having two commands that will commonly be used, there's also a shortcut for those commands:

```clojure
(start-repl)
;; 2023-10-16 12:28:29,527 [main] INFO  kit.config - Reading config system.edn
;; 2023-10-16 12:28:29,537 [main] INFO  kit.config - Reading config system.edn
;; #object[kit.edge.db.sql.conman$eval16250$fn__16252$fn__16254 0x483f0877 "kit.edge.db.sql.conman$eval16250$fn__16252$fn__16254@483f0877"]

```

For more information, refer to [Deps and CLI Guide](https://clojure.org/guides/deps_and_cli) for REPL starting and [Integrant-REPL](https://github.com/weavejester/integrant-repl) for state/config and state/system.

## Production workflow

(Instructions for deploying the app will be provided in a future update.)

## Testing

### Backend Testing

You can test the application's backend by accessing the app container and running the REPL in the test alias:

```bash
docker compose exec app /bin/sh
# /app#

clojure -X:test
```

### Frontend Testing

(Instructions for frontend testing will be provided in a future update.)

## Good to know

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
