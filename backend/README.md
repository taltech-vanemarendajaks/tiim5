### Backend folder ###

**copy .env.example values to your own .env file**

### Running the db

Run via compose up db configuration or

```sh
  docker compose -f docker-compose-development.yml up db
```

### Running the backend

From the backend directory:

With IntelliJ, use the run the bootRun development configuration or use launch.json in VSCode to pass env values.

```
./gradlew bootRun
```

or on Windows:
```
gradlew.bat bootRun
```
### Accessing the API documentation

When the backend is running, open:
http://localhost:8080/docs

### Running the backend via docker

#### For development HMR environment

**via** backend development configuration or...

```sh
    docker compose -f docker-compose-development.yml up --watch
```

### Running tests

**Unit tests:**
```
./gradlew.bat test
```
**Integration tests:**
```
./gradlew.bat intergationTest
```
**All tests:**
```
./gradlew.bat check
```

For code formatting please run the command:
```
./gradlew.bat spotlessApply
```