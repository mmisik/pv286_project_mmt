# Panbyte

This project is our implementation for the homework in the PV286 course.
Our team - Marek Mišík, Jean Dossa Tsimbazafy and Daniel Múčka.
We implemented the Panbyte project in Java using Maven.

You can either run the project in IntelliJ Idea or using `mvn install` and then `java -jar target/panbyte.jar`.

The tests will be run automatically during the `mvn install` command, but you can explicitely test using `mvn test` or `mvn verify`.

You can run the integration tests using `python3 integration.py`, however this test will work only on Linux (it was made for the pipeline).

NOTE: You need to have at least Java 17 and Maven installed.