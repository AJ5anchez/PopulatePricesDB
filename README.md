# PopulatePricesDB
# Summary
Spring Boot code to create a MongoDB [1] that contains the current price for each product id.
# Building | Running | Testing
Once you have cloned this repository, go to the PopulatePricesDB directory, and do any/all of the following, which assumes you have maven [2] installed in your system:

(1) `mvn spring-boot:run`

This just runs the application, and therefore creates a MongoDB located and named according to the attribute `spring.data.mongodb.uri` defined in the `config/application.properties` file. The current value points to a MongoDB server on a EC2 AWS node that only accepts connections from my laptop's IP address. If this line in the `config/application.properties` file is commented out (prefix the line with a "#")--or if the `config/application.properties` file is not present--Spring boot creates a MongoDB with URI `mongodb://localhost:27017/test`. Therefore, you would need to have a MongoDB server running locally (and listening to port 27017). Alternatively, you can re-define this property to fit your needs. This does not generate a jar file--see the next two options.

(2) `mvn clean package`

This does two things: (a) Runs the application and all tests in the project (currently only one test case); and (b) Generates a jar file--called `demo-1.2.2.jar`--in the directory `target`. Once the jar file is generated, the application can be executed as:

`java -jar target/demo-1.2.2.jar`

(3) `mvn -Dtest=PopulatePricesDbApplicationTests#checkPairs test`

This runs the application and then the test case--but does not generate the jar file.

#Additional Notes
To create this small project, I:
- Used Spring Tool Suite 3.6.4 [http://spring.io/tools/sts](http://spring.io/tools/sts)
- Used [https://spring.io/guides/gs/accessing-data-mongodb/](https://spring.io/guides/gs/accessing-data-mongodb/)
- Consulted [http://projects.spring.io/spring-boot/](http://projects.spring.io/spring-boot/)
- Installed MongoDB on Windows (7 pro) and on AWS EC2 (AWS Linux)

#References:
[1] [https://www.mongodb.org/](https://www.mongodb.org/)

[2] [http://maven.apache.org/](http://maven.apache.org/)
