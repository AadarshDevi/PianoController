# Building Project

1. pom.xml: set project <version>
2. install libs from pom.xml (do not download lib again if it exists)
   1. log4j-core 2.25.2
   2. javafx 23.0.1
3. mvn clean package
4. use jpackage
5. Put project in a release folder