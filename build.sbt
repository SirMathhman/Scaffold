name := "Scaffold"

version := "0.1"

scalaVersion := "2.13.2"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.+"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.0"

libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.+" % Test
libraryDependencies += "org.junit.jupiter" % "junit-jupiter-engine" % "5.+" % Test