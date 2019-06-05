# Amazing Company

Implementation of amazing company challenge

### The problem

There is company with tree structure. There are two main requirements in this company:
1.	Get all children nodes of a given node (the given node can be anyone in the tree structure).
2.	Change the parent node of a given node (the given node can be anyone in the tree structure). 

Each node should have the following info:

   * node identification
   * who is the parent node
   * who is the root node
   * the height of the node.

### Brief Discussion

The main challenge of this application was about how to keep nodes of tree in DB and handle the performance of change parent of given node and update heights of it and all its subtree. After analyzing different solutions, I finally decided to use [nested set](https://en.wikipedia.org/wiki/Nested_set_model) to keep tree structure in relational DB and move a subtree in it. 
The nested set model is to number the nodes according to a traversal (DFS arrival and departure of vertices), which visits each node twice, assigning numbers in the order of visiting, and at both visits.

##### Reasons Of using nested set structure:

1.	Retrieve all children of a node in different levels by one simple query
2.	Update heights of all moved nodes efficient, without any recursion.

### Used Stack

   * Spring Boot 2.1.1
   * Java 8
   * maven
   * MySql
   * Swagger
   * Liquibase
 
### How to Run

1- This is a maven project. Use the following command to build required artifact:

```
mvn clean install
```

2- After run app with docker compose:

```
docker-compose up
```
##### Note at Run Time:
 * At first run, application might restart several times until MySql container would be available. 
### Swagger 
To access the swagger, simply run the application and enter the **/swagger-ui.html** path.
Example running local

```
swagger path: http://localhost:9090/swagger-ui.html
```


















