# ntw_project project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `ntw_project-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/ntw_project-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/ntw_project-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

# RESTEasy JAX-RS

<p>A Hello World RESTEasy resource</p>

Guide: https://quarkus.io/guides/rest-json

# Call an endpoint 
## How to create a table
Query : POST http://localhost:8080/table/

Body :
``
{
"columns": 
[
{ "name": "nom", "type": "string" },
{ "name": "prenom", "type": "string" },
{ "name": "age", "type": "int"},
{ "name": "salaire", "type": "double"}
],
"name": "person"
}
``

## How to add a simple index
Index with one field in this example

Query : POST http://localhost:8080/index/add

Body : 
``
{
"table_name" : "person",
"columns" : [
{"name" : "nom"}
]
}
``

## How to add a multiple index
Index with two fields in this example

Query : POST http://localhost:8080/index/add

Body : 
``
{
"table_name" : "person",
"columns" : [
{"name" : "prenom"},
{"name" : "nom"}
]
}
``

## How to load data
Query : POST http://localhost:8080/data/load

Body : Select form-data tab
    Add file and table keys
    Select the CSV file with the data for the file key
    For table set the table's name




