### Backend folder ###

### Running the backend

From the backend directory:

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