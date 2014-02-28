jboss-deployment-download
=======================

Application which enables the ability to download original artifacts of deployed applications on JBoss AS 7/EAP 6/WildFly servers.

Exposes deployments found within the [ContentRepository](https://github.com/wildfly/wildfly/blob/master/deployment-repository/src/main/java/org/jboss/as/repository/ContentRepository.java) of the current JBoss server this application is deployed on.

## Usage

* Clone into local machine
* Build project  
`mvn clean install`
* Deploy to JBoss Server
* Navigate to application: [http://localhost:8080/jboss-deployment-download](http://localhost:8080/jboss-deployment-download)

